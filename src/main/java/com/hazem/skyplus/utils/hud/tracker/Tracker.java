package com.hazem.skyplus.utils.hud.tracker;

import com.hazem.skyplus.ProfileDataManager;
import com.hazem.skyplus.Skyplus;
import com.hazem.skyplus.events.ItemsPriceEvents;
import com.hazem.skyplus.events.SkyBlockEvents;
import com.hazem.skyplus.utils.hud.AbstractWidget;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

/**
 * Abstract base class for tracking item-related data.
 * <p>
 * Supports tracking looted items, updating profits based on price changes,
 * managing tracker activity, and handling resets based on different modes.
 */
public abstract class Tracker extends AbstractWidget {
    private static final Path CONFIG_PATH = Path.of("config/" + Skyplus.NAMESPACE + "/trackers/");
    private static final int INACTIVITY_THRESHOLD = 20 * 60 * 2;
    protected final ProfileDataManager<TrackerData> data;
    private final List<String> itemsName = new ArrayList<>();
    protected final ItemLoot[] lootTable;
    private final Supplier<TrackerResetMode> resetModeSupplier;
    protected final Map<String, Supplier<Boolean>> itemRules = new HashMap<>();
    private final List<TrackedItem> consumables = new ArrayList<>();
    private final int inactivityTask;
    protected boolean active = false;
    protected int lastActionTime = 0;
    protected boolean firstPriceUpdate = false;
    private ItemLoot[] consumableTable;
    private boolean timerEnabled = true;

    public Tracker(ItemLoot[] lootTable, String fileName, Supplier<TrackerResetMode> resetModeSupplier) {
        this.lootTable = lootTable;
        this.resetModeSupplier = resetModeSupplier;
        this.data = new ProfileDataManager<>(TrackerData.CODEC, TrackerData.DEFAULT);

        Arrays.stream(lootTable).forEach(itemLoot -> itemsName.add(itemLoot.getDisplayName()));

        Path filePath = CONFIG_PATH.resolve(fileName);
        TrackerResetMode resetMode = resetModeSupplier.get();
        if (resetMode == TrackerResetMode.RESTART || resetMode == TrackerResetMode.WORLD_CHANGE) {
            data.save(filePath); // Clears JSON file
        } else {
            data.load(filePath);
        }

        // Initialize loot for each profile data
        data.getProfilesData().forEach(profileData -> profileData.initializeLoot(lootTable));

        // Set up scheduled task for inactivity timeout
        inactivityTask = Scheduler.getInstance().scheduleLazy(this::handleInactivityTimeout, INACTIVITY_THRESHOLD);
        Scheduler.getInstance().scheduleCyclic(this::updateTimer, 0, 20);

        ClientPlayConnectionEvents.DISCONNECT.register(this::onPlayerDisconnect);
        SkyBlockEvents.PROFILE_CHANGE.register(this::sortItems);
        SkyBlockEvents.LOBBY_CHANGE.register(this::onLobbyChange);
        ItemsPriceEvents.PRICE_UPDATE.register(this::onPriceUpdate);
    }

    /**
     * Activates the tracker and updates the profit if it is the first price update.
     */
    protected void activate() {
        active = shouldRender();
        if (!firstPriceUpdate) updateProfit();
    }

    /**
     * Updates the timer and adds time if the timer is enabled and the tracker is active.
     */
    private void updateTimer() {
        if (timerEnabled && active) {
            data.get().addTime(1000);
        }
    }

    /**
     * Disables the timer for this tracker.
     * Should be used in constructor if tracker shouldn't use a Timer.
     */
    protected void disableTimer() {
        timerEnabled = false;
        Scheduler.getInstance().deactivateTask(inactivityTask);
    }

    /**
     * Stops the tracking when the player leaves the server.
     */
    private void onPlayerDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        active = false;
        Scheduler.getInstance().deactivateTask(inactivityTask);
    }

    /**
     * Handles lobby changes by resetting the tracker if necessary.
     */
    private void onLobbyChange() {
        active = false;
        Scheduler.getInstance().deactivateTask(inactivityTask);
        if (resetModeSupplier.get() == TrackerResetMode.WORLD_CHANGE) {
            data.get().reset();
        }
    }

    /**
     * Updates profits when item prices change.
     */
    private void onPriceUpdate() {
        if (active) {
            data.getProfilesData().forEach(trackerData -> trackerData.updateProfit(itemRules));
            sortItems();
        }
    }

    /**
     * Registers an activity to prevent auto-deactivation due to inactivity.
     * This method also triggers item sorting and data saving. 
     * If {@code isBulkSave} is true, a delayed save operation will be scheduled to avoid frequent disk writes
     * otherwise the save happens immediately.
     * The {@code isBulkSave} flag should be used if there is a chance that the data will change in the next few milliseconds.
     *
     * @param isBulkSave Indicates whether a bulk save should be triggered.
     */
    //TODO: change name
    protected void markActive(Boolean isBulkSave) {
        active = true;
        data.get().incrementCounter();
        lastActionTime = Scheduler.getInstance().getCurrentTick();
        if (timerEnabled) {
            Scheduler.getInstance().rescheduleTask(inactivityTask, INACTIVITY_THRESHOLD);
        }
        if (isBulkSave) {
            Scheduler.getInstance().schedule(this::save, 20);
        } else {
            save();
        }
    }

    protected void markActive() {
        markActive(false);
    }

    /**
     * Handles inactivity timeout and deactivates the tracker if necessary.
     */
    private void handleInactivityTimeout() {
        if (active && timerEnabled && Scheduler.getInstance().getCurrentTick() - lastActionTime >= INACTIVITY_THRESHOLD) {
            active = false;
            Scheduler.getInstance().deactivateTask(inactivityTask);
        }
    }

    /**
     * Sets the consumable table for this tracker.
     *
     * @param consumableTable The array of consumable items to track.
     */
    protected void setConsumableTable(ItemLoot[] consumableTable) {
        this.consumableTable = consumableTable;
        data.getProfilesData().forEach(profileData -> profileData.initializeConsumables(consumableTable));
    }

    /**
     * Registers a consumable item to be tracked by this tracker.
     *
     * @param id          The unique identifier of the consumable item.
     * @param displayName The display name of the consumable item.
     * @param color       The text formatting color for the item display.
     */
    protected void setConsumable(String id, String displayName, Formatting color){
        consumables.add(new TrackedItem(id, displayName, color, Integer.MAX_VALUE, 0));
    }

    /**
     * Updates the profit for all tracked items based on the current item rules.
     */
    protected void updateProfit() {
        data.get().updateProfit(itemRules);
    }

    /**
     * Registers a tracking rule for a specific item.
     * In order to work, {@link #shouldTrack(String)} should be called in the update method.
     *
     * @param itemId The ID of the item.
     * @param rule   A condition that determines whether the item should be tracked.
     */
    protected void registerItemRule(String itemId, Supplier<Boolean> rule) {
        itemRules.put(itemId, rule);
    }

    /**
     * Determines whether a given item should be tracked based on the registered rules.
     *
     * @param itemId The item ID to check.
     * @return {@code true} if the item should be tracked, otherwise {@code false}.
     */
    protected boolean shouldTrack(String itemId) {
        return !itemRules.containsKey(itemId) || itemRules.get(itemId).get();
    }

    /**
     * Saves tracker data if allowed by the current reset mode.
     */
    protected void save() {
        sortItems();
        if (resetModeSupplier.get() == TrackerResetMode.NEVER) data.save();
    }

    /**
     * Resets and saves the tracker data.
     */
    public void reset() {
        data.get().reset();
        data.save();
    }

    /**
     * Searches for a lootable item in a given message.
     *
     * @param message The message to analyze.
     * @return The name of the found item, or {@code null} if no match is found.
     */
    protected String getLootFromMessage(String message) {
        //TODO: is itemName needed?
        return itemsName.stream()
                .filter(message::contains)
                .findFirst()
                .orElse(null);
    }

    protected boolean isLootItem(ItemStack itemStack) {
        return Arrays.stream(lootTable).anyMatch(item -> item.getId().equals(itemStack.getItemId()));
    }

    protected boolean isLootItem(String itemName) {
        return Arrays.stream(lootTable).anyMatch(item -> item.getDisplayName().equals(itemName));
    }

    /**
     * Adds a consumable item to the tracker.
     *
     * @param displayName The display name of the consumable item.
     * @param amount      The amount of the consumable item.
     */
    protected void addConsumable(String displayName, int amount) {
        TrackedItem item = data.get().getConsumables().stream()
                .filter(i -> i.getDisplayName().equals(displayName))
                .findFirst()
                .orElseGet(() -> createItem(displayName, false));

        if (item != null) {
            item.addAmount(amount);
            data.get().updateProfit(itemRules);
        }
    }

    /**
     * Adds loot to the tracker.
     *
     * @param displayName The display name of the loot item.
     * @param amount      The amount of loot collected.
     */
    protected void addLoot(String displayName, int amount) {
        TrackedItem item = data.get().getLootItems().stream()
                .filter(i -> i.getDisplayName().equals(displayName))
                .findFirst()
                .orElseGet(() -> createItem(displayName, true));

        if (item != null) {
            item.addAmount(amount);
            data.get().updateProfit(itemRules);
        }
    }

    /**
     * Creates a new tracked item if it exists in the loot or consumable table.
     * If the item is a consumable and already exists, it triggers re-initialization
     * for all profiles before returning the existing item. Otherwise, it creates
     * and registers a new item from the loot or consumable table.
     *
     * @param name   The display name of the item.
     * @param isLoot {@code true} if the item should be considered loot, {@code false} if it's a consumable.
     * @return The created {@code TrackedItem} if found, or {@code null} if it does not exist in the relevant table.
     */
    protected TrackedItem createItem(String name, boolean isLoot) {
        // If it's a consumable, check if it already exists
        if (!isLoot) {
            TrackedItem existingConsumable = consumables.stream()
                    .filter(consumable -> consumable.getDisplayName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (existingConsumable != null) {
                // Initialize all profiles' consumables data
                data.getProfilesData().forEach(profileData -> profileData.initializeConsumables(consumables));

                // Return the tracked item from the stored consumables
                return data.get().getConsumables().stream()
                        .filter(item -> item.getDisplayName().equals(name))
                        .findFirst()
                        .orElse(null);
            }

            // If consumableTable is null, no new item can be created
            if (consumableTable == null) return null;
        }

        // Search for the item in the appropriate table (loot or consumable)
        return Arrays.stream(isLoot ? lootTable : consumableTable)
                .filter(item -> item.getDisplayName().equals(name))
                .findFirst()
                .map(item -> {
                    // Create a new TrackedItem
                    TrackedItem newItem = new TrackedItem(
                            item.getId(),
                            item.getDisplayName(),
                            item.getColor(),
                            item.getPriority(),
                            0
                    );

                    // Add the new item to the correct list (loot or consumables)
                    (isLoot ? data.get().getLootItems() : data.get().getConsumables()).add(newItem);
                    return newItem;
                })
                .orElse(null);
    }

    /**
     * Sorts tracked items by priority (highest first) and then by total price (highest first).
     */
    private void sortItems() {
        data.get().getLootItems().sort(
                Comparator.comparingInt(TrackedItem::getPriority).reversed() // Sort by priority
                        .thenComparingDouble(TrackedItem::getTotalPrice).reversed() // Sort by price (highest first)
        );
    }
}