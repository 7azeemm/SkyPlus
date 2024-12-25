package com.hazem.skyplus.data.bazaar;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class BazaarData {
    public record BazaarResponse(
            boolean success,
            long lastUpdated,
            Object2ObjectOpenHashMap<String, Product> products
    ) {}

    public record Product(
            String product_id,
            ObjectArrayList<Summary> sell_summary,
            ObjectArrayList<Summary> buy_summary,
            QuickStatus quick_status
    ) {}

    public record Summary(
            int amount,
            double pricePerUnit,
            int orders
    ) {}

    public record QuickStatus(
            String productId,
            double sellPrice,
            int sellVolume,
            int sellMovingWeek,
            int sellOrders,
            double buyPrice,
            int buyVolume,
            int buyMovingWeek,
            int buyOrders
    ) {}
}