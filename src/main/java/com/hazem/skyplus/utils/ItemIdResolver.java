package com.hazem.skyplus.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class ItemIdResolver {
    private static final Map<String, Function<NbtCompound, String>> ITEM_ID_RESOLVERS = new HashMap<>();

    static {
        // Register handlers for specific IDs
        ITEM_ID_RESOLVERS.put("ENCHANTED_BOOK", customData -> {
            if (customData.contains("enchantments")) {
                NbtCompound enchants = customData.getCompound("enchantments");
                String enchant = enchants.getKeys().stream().findFirst().orElse("");
                return "ENCHANTMENT_" + enchant.toUpperCase(Locale.ENGLISH) + "_" + enchants.getInt(enchant);
            }
            return "ENCHANTED_BOOK";
        });

        ITEM_ID_RESOLVERS.put("ATTRIBUTE_SHARD", customData -> {
            if (customData.contains("attributes")) {
                NbtCompound shards = customData.getCompound("attributes");
                String shard = shards.getKeys().stream().findFirst().orElse("");
                return "ATTRIBUTE_SHARD_" + shard.toUpperCase(Locale.ENGLISH) + "_" + shards.getInt(shard);
            }
            return "ATTRIBUTE_SHARD";
        });

        ITEM_ID_RESOLVERS.put("PET", customData -> {
            if (customData.contains("petInfo")) {
                String petInfoJson = customData.getString("petInfo");
                JsonObject petInfo = JsonParser.parseString(petInfoJson).getAsJsonObject();
                String type = petInfo.get("type").getAsString();
                String tier = petInfo.get("tier").getAsString();
                return type + "_" + tier;
            }
            return "PET";
        });

        ITEM_ID_RESOLVERS.put("RUNE", customData -> {
            if (customData.contains("runes")) {
                NbtCompound runes = customData.getCompound("runes");
                String rune = runes.getKeys().stream().findFirst().orElse("");
                return rune + "_RUNE_" + runes.getInt(rune);
            }
            return "RUNE";
        });

        ITEM_ID_RESOLVERS.put("POTION", customData -> {
            if (customData.contains("potion") && customData.contains("potion_level")) {
                return "POTION_" + customData.getString("potion").toUpperCase(Locale.ENGLISH)
                        + "_" + customData.getInt("potion_level");
            }
            return "POTION";
        });
    }

    /**
     * Resolves a custom item ID based on the given NBT data.
     *
     * @param id         The base ID of the item.
     * @param customData The NBT data containing item details.
     * @return The resolved custom item ID.
     */
    public static String resolveItemId(String id, NbtCompound customData) {
        Function<NbtCompound, String> resolver = ITEM_ID_RESOLVERS.get(id);
        if (resolver != null) {
            return resolver.apply(customData);
        }
        return id; // Default to the raw ID if no specific resolver exists
    }
}