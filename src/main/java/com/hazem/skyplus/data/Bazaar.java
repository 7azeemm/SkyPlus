package com.hazem.skyplus.data;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.utils.APIUtils;
import com.hazem.skyplus.utils.schedular.Scheduler;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bazaar {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bazaar.class);
    private static final String JSON_URL = "https://api.hypixel.net/v2/skyblock/bazaar";
    private static final int REFRESH_INTERVAL = 20 * 60 * 2;
    public static volatile BazaarResponse data;

    @Init
    public static void bazaarRefresher() {
        Scheduler.getInstance().scheduleCyclicAsync(() -> APIUtils.fetchJson(JSON_URL)
                .thenApply(json -> {
                    if (json == null) {
                        LOGGER.error("Failed to update Bazaar data. Received null JSON data from the API.");
                        return null;
                    }
                    return APIUtils.parseJsonToClass(json, BazaarResponse.class);
                })
                .thenAccept(response -> {
                    if (response != null && response.success()) {
                        data = response;
                    } else {
                        LOGGER.error("Failed to update Bazaar data. Response was either null or unsuccessful.");
                    }
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Failed fetching Bazaar data: {}", throwable.getMessage());
                    return null;
                }), 0, REFRESH_INTERVAL);
    }

    public record BazaarResponse(
            boolean success,
            long lastUpdated,
            Object2ObjectOpenHashMap<String, Product> products
    ) {
    }

    public record Product(
            String product_id,
            ObjectArrayList<Summary> sell_summary,
            ObjectArrayList<Summary> buy_summary,
            QuickStatus quick_status
    ) {
    }

    public record Summary(
            int amount,
            double pricePerUnit,
            int orders
    ) {
    }

    public record QuickStatus(
            String productId,
            double sellPrice,
            int sellVolume,
            long sellMovingWeek,
            int sellOrders,
            double buyPrice,
            int buyVolume,
            long buyMovingWeek,
            int buyOrders
    ) {
    }
}