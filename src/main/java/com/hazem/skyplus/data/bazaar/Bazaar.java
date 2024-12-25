package com.hazem.skyplus.data.bazaar;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.utils.APIUtils;
import com.hazem.skyplus.utils.schedular.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class Bazaar {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bazaar.class);
    private static final String JSON_URL = "https://api.hypixel.net/v2/skyblock/bazaar";
    private static final int REFRESH_INTERVAL = 20 * 60 * 2;
    private static BazaarData.BazaarResponse cachedData;

    @Init
    public static void bazaarRefresher() {
        Scheduler.getInstance().scheduleCyclicAsync(() -> fetchBazaarData(JSON_URL)
                .thenAccept(data -> {
                    if (data != null && data.success()) {
                        cachedData = data;
                    } else {
                        LOGGER.error("Failed to update Bazaar data.");
                    }
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Error fetching Bazaar data: {}", throwable.getMessage());
                    return null;
                }), 0, REFRESH_INTERVAL);
    }

    public static CompletableFuture<BazaarData.BazaarResponse> fetchBazaarData(String uriString) {
        return APIUtils.fetchJson(uriString).thenApply(json -> APIUtils.parseJsonToClass(json, BazaarData.BazaarResponse.class));
    }

    public static BazaarData.BazaarResponse getCachedData() {
        return cachedData;
    }
}