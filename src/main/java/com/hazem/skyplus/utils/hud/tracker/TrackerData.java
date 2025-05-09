package com.hazem.skyplus.utils.hud.tracker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;
import java.util.function.Supplier;

public class TrackerData {
    private final List<TrackedItem> lootItems;
    private final List<TrackedItem> consumables;
    private int counter;
    private long timer;
    private double profit;
    private double cost;

    public static final Supplier<TrackerData> DEFAULT = () -> new TrackerData(0, 0, new ArrayList<>(), new ArrayList<>());
    public static final Codec<TrackerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("counter").forGetter(stats -> stats.counter),
            Codec.LONG.fieldOf("timer").forGetter(stats -> stats.timer),
            TrackedItem.CODEC.fieldOf("items").forGetter(data -> data.lootItems),
            TrackedItem.CODEC.fieldOf("consumables").forGetter(data -> data.consumables)
    ).apply(instance, TrackerData::new));

    public TrackerData(int counter, long timer, List<TrackedItem> lootItems, List<TrackedItem> consumables) {
        this.counter = counter;
        this.timer = timer;
        this.lootItems = lootItems;
        this.consumables = consumables;
        this.profit = 0;
        this.cost = 0;
    }

    /**
     * Initializes loot items based on an array of predefined loot data.
     *
     * @param lootData The array of loot data.
     */
    public void initializeLoot(ItemLoot[] lootData) {
        lootItems.forEach(item ->
                Arrays.stream(lootData)
                        .filter(loot -> loot.getId().equals(item.getId()))
                        .findFirst()
                        .ifPresent(item::init)
        );
    }

    /**
     * Initializes consumable items based on an array of predefined consumable data.
     *
     * @param consumableData The array of consumable data.
     */
    public void initializeConsumables(ItemLoot[] consumableData) {
        consumables.forEach(item ->
                Arrays.stream(consumableData)
                        .filter(consumable -> consumable.getId().equals(item.getId()))
                        .findFirst()
                        .ifPresent(item::init)
        );
    }

    /**
     * Updates the list of consumable items based on new data.
     * If a consumable item already exists in the list, it updates its display name and color.
     * If the item does not exist, it is added to the list without modification.
     *
     * @param consumableData The list of new consumable items to be processed.
     */
    public void initializeConsumables(List<TrackedItem> consumableData) {
        for (TrackedItem item : consumableData) {
            Optional<TrackedItem> existing = consumables.stream()
                    .filter(consumable -> consumable.getId().equals(item.getId()))
                    .findFirst();

            if (existing.isPresent()) {
                existing.get().init(item.getDisplayName(), item.getColor());
            } else {
                consumables.add(item);
            }
        }
    }

    public void reset() {
        lootItems.clear();
        consumables.clear();
        counter = 0;
        timer = 0;
        profit = 0;
        cost = 0;
    }

    /**
     * Updates profit and cost based on item rules.
     *
     * @param itemRules A mapping of item IDs to filtering conditions.
     */
    public void updateProfit(Map<String, Supplier<Boolean>> itemRules) {
        cost = consumables.stream()
                .peek(TrackedItem::updatePrice)
                .mapToDouble(TrackedItem::getTotalPrice)
                .sum();

        profit = lootItems.stream()
                .filter(item -> itemRules.getOrDefault(item.getId(), () -> true).get())
                .peek(TrackedItem::updatePrice)
                .mapToDouble(TrackedItem::getTotalPrice)
                .sum() - cost;
    }

    public List<TrackedItem> getLootItems() {
        return lootItems;
    }

    public List<TrackedItem> getConsumables() {
        return consumables;
    }

    public int getCounter() {
        return counter;
    }

    public long getTimer() {
        return timer;
    }

    public void addTime(long amount) {
        timer += amount;
    }

    public void incrementCounter() {
        counter++;
    }

    public double getProfit() {
        return profit;
    }
}