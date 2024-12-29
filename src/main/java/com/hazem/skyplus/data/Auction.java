package com.hazem.skyplus.data;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.constants.ItemRarity;
import com.hazem.skyplus.utils.APIUtils;
import com.hazem.skyplus.utils.schedular.Scheduler;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class Auction {
    private static final Logger LOGGER = LoggerFactory.getLogger(Auction.class);
    private static final String JSON_URL = "https://moulberry.codes/lowestbin.json";
    private static final int REFRESH_INTERVAL = 20 * 60 * 2;
    public static volatile Object2DoubleOpenHashMap<String> data;

    @Init
    public static void auctionRefresher() {
        Scheduler.getInstance().scheduleCyclicAsync(() -> APIUtils.fetchJson(JSON_URL)
                .thenAccept(json -> {
                    if (json == null) {
                        LOGGER.error("Failed to update Auction data. Received null JSON data from the API.");
                        return;
                    }

                    Object2DoubleOpenHashMap<String> parsedData = parseLowestBinData(json.toString());
                    if (!parsedData.isEmpty()) {
                        data = parsedData;
                    } else {
                        LOGGER.error("Failed to update Auction data. Parsed auction data is empty.");
                    }
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed fetching Auction data: {}", throwable.getMessage());
                    return null;
                }), 0, REFRESH_INTERVAL);
    }

    private static Object2DoubleOpenHashMap<String> parseLowestBinData(String json) {
        if (json == null || json.isEmpty()) {
            LOGGER.error("Auction data is null or empty.");
            return new Object2DoubleOpenHashMap<>();
        }

        // Convert JSON to a Map
        Object2ObjectOpenHashMap<String, Object> rawData = APIUtils.parseJsonToMap(json);
        Object2DoubleOpenHashMap<String> processedData = new Object2DoubleOpenHashMap<>(rawData.size());

        for (Map.Entry<String, Object> entry : rawData.entrySet()) {
            String rawKey = entry.getKey();
            double price = (double) entry.getValue();

            // Split the key into base and additional parts
            String[] plusParts = rawKey.split("\\+");
            String baseId = plusParts[0]; // The item ID is always before the first '+'
            String[] semicolonParts = baseId.split(";");

            // Handle Attribute Shards
            if (baseId.startsWith("ATTRIBUTE_SHARD")) {
                if (rawKey.contains(";")) {
                    if (plusParts.length == 2) continue; // Skip entries with "+" and ";"
                    rawKey = rawKey.replace(";", "_");
                    processedData.put(rawKey, price);
                } else {
                    // Handle the lowest price for the attribute shard type (e.g., "ATTRIBUTE_SHARD+ATTRIBUTE_MANA_POOL")
                    String shardType = baseId.replace("+ATTRIBUTE", ""); // Extract shard type without "+" (e.g., "ATTRIBUTE_SHARD_MANA_POOL")
                    processedData.put(shardType, price);
                }
                continue;
            }

            // Handle runes and potions
            if (baseId.contains("_RUNE") || baseId.startsWith("POTION")) {
                if (semicolonParts.length == 2) {
                    String name = semicolonParts[0];
                    int level = Integer.parseInt(semicolonParts[1]);
                    String id = name + "_" + level;
                    processedData.put(id, price);
                }
                continue;
            }

            // Handle pets
            if (baseId.contains(";")) { // Pets have a format like "PET_NAME;Rarity"
                String petName = semicolonParts[0];
                int rarityOrdinal = Integer.parseInt(semicolonParts[1]); // Rarity is always after the semicolon
                String rarity = Optional.ofNullable(ItemRarity.getRarityByOrdinal(rarityOrdinal)).map(ItemRarity::name).orElse("UNKNOWN");
                String petId = petName + "_" + rarity;
                if (plusParts.length == 2) petId += "_100";
                processedData.put(petId, price);
                continue;
            }

            // Items without attributes
            if (plusParts.length == 1) {
                processedData.put(baseId, price);
            }
        }

        return processedData;
    }
}