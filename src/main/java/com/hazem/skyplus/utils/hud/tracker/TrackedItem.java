package com.hazem.skyplus.utils.hud.tracker;

import com.hazem.skyplus.data.itemPrice.PriceResult;
import com.hazem.skyplus.data.itemPrice.PriceSource;
import com.hazem.skyplus.utils.ItemUtils;
import com.mojang.serialization.Codec;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.stream.Collectors;

public class TrackedItem {
    private final String id;
    private int amount;
    private String displayName;
    private Formatting color;
    private int priority;
    private PriceResult priceResult = new PriceResult(0, PriceSource.UNKNOWN);

    public static final Codec<List<TrackedItem>> CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT)
            .xmap(map -> map.entrySet().stream().map(entry -> new TrackedItem(entry.getKey(), entry.getValue())).collect(Collectors.toList()),
                    list -> list.stream().collect(Collectors.toMap(TrackedItem::getId, TrackedItem::getAmount)));

    public TrackedItem(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public TrackedItem(String id, String displayName, Formatting color, int priority, int amount) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.priority = priority;
        this.amount = amount;
    }

    public void init(ItemLoot itemLoot) {
        this.displayName = itemLoot.getDisplayName();
        this.color = itemLoot.getColor();
        this.priority = itemLoot.getPriority();
    }

    public void init(String displayName, Formatting color) {
        this.displayName = displayName;
        this.color = color;
    }

    public void updatePrice() {
        ItemUtils.getItemPrice(id).ifPresent(priceResult -> this.priceResult = priceResult);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getTotalPrice() {
        return priceResult.price() * amount;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getAmount() {
        return amount;
    }

    public Formatting getColor() {
        return color;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }
}
