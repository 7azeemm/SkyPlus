package com.hazem.skyplus.data.itemPrice;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.utils.APIUtils;
import com.hazem.skyplus.utils.schedular.Scheduler;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Npc {
    private static final Logger LOGGER = LoggerFactory.getLogger(Npc.class);
    private static final String JSON_URL = "https://hysky.de/api/npcprice";
    private static final int RETRY_INTERVAL = 20 * 30;
    private static final AtomicInteger RETRIES = new AtomicInteger(3);
    public static volatile Object2DoubleOpenHashMap<String> data;

    @Init(priority = Init.Priority.MEDIUM)
    public static void loadNpcPricesData() {
        Scheduler.getInstance().scheduleAsync(() -> APIUtils.fetchJson(JSON_URL)
                .thenAccept(json -> {
                    if (json == null) {
                        LOGGER.error("Failed to load NPC prices data. Received null JSON data from the API.");
                        scheduleRetry();
                        return;
                    }

                    Object2DoubleOpenHashMap<String> parsedData = new Object2DoubleOpenHashMap<>();
                    json.asMap().forEach((string, jsonElement) -> parsedData.addTo(string, jsonElement.getAsDouble()));

                    if (!parsedData.isEmpty()) {
                        data = parsedData;
                    } else {
                        LOGGER.error("Failed to load NPC prices data. Parsed auction data is empty.");
                        scheduleRetry();
                    }
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed fetching NPC Prices data: {}", throwable.getMessage());
                    scheduleRetry();
                    return null;
                }), 0);
    }

    private static void scheduleRetry() {
        int remainingRetries = RETRIES.decrementAndGet();
        if (remainingRetries > 0) {
            LOGGER.error("Retrying to fetch NPC prices data.");
            Scheduler.getInstance().scheduleAsync(Npc::loadNpcPricesData, RETRY_INTERVAL);
        }
    }
}