package com.hazem.skyplus.skyblock.end.dragon;

import com.hazem.skyplus.annotations.Widget;
import com.hazem.skyplus.config.ConfigManager;
import com.hazem.skyplus.events.ChatEvents;
import com.hazem.skyplus.events.SkyBlockEvents;
import com.hazem.skyplus.utils.ChatUtils;
import com.hazem.skyplus.utils.FormattingUtils;
import com.hazem.skyplus.utils.HypixelData;
import com.hazem.skyplus.utils.hud.components.ComponentBuilder;
import com.hazem.skyplus.utils.hud.tracker.TrackedItem;
import com.hazem.skyplus.utils.hud.tracker.Tracker;
import com.hazem.skyplus.utils.hud.tracker.TrackerData;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Widget
public class DragonTracker extends Tracker {
    public static DragonTracker INSTANCE;
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("x([\\d,]+)");
    private final List<String> lootTexts = new ArrayList<>();
    private final Integer savingTaskId;
    private final Integer savingTask2Id;
    private boolean dragonSlain = false;
    private final Set<String> toRegister = new HashSet<>();
    private final Map<String, Vec3d> armorStandLoot = new HashMap<>();

    public DragonTracker() {
        super(DragonLoot.values(), "dragon.json", () -> ConfigManager.getConfig().end.dragonTracker.resetMode);
        INSTANCE = this;

        savingTaskId = Scheduler.getInstance().scheduleLazy(this::dragonSlainAndLooted, 20 * 5);
        savingTask2Id = Scheduler.getInstance().scheduleLazy(this::schedule, 20 * 20);

        SkyBlockEvents.AREA_CHANGE.register(this::activate);
        ChatEvents.SACKS_ITEMS_ADDED.register(this::onSacksItemsAdded);
        ClientReceiveMessageEvents.GAME.register(this::onChatMessage);
        SkyBlockEvents.LOBBY_CHANGE.register(armorStandLoot::clear);
    }

    private void onSacksItemsAdded(Map<String, Integer> items) {
        if (!shouldRender() && toRegister.isEmpty()) return;

        items.forEach((name, amount) -> {
            String lootText = formatLootText(name, amount);
            if (toRegister.remove(lootText) && !lootTexts.contains(lootText)) {
                lootTexts.add(lootText);
                addLoot(name, amount);
                Scheduler.getInstance().rescheduleTask(savingTaskId, 100);
                ChatUtils.sendMessage("Added loot from sack: " + lootText);
            }
        });
    }

    private void onChatMessage(Text text, boolean overlay) {
        if (overlay || !shouldRender()) return;
        String string = text.getString();

        if (string.contains("Dragon has spawned!")) {
            lootTexts.clear();
            toRegister.clear();
            dragonSlain = false;
        } else if (string.contains("DRAGON DOWN!")) {
            dragonSlain = true;
        } else if (dragonSlain && string.contains("Your Damage: ")) {
            if (!string.contains(": 0")) {
                data.get().incrementCounter();
                addLoot("Dragon Essence", 5);
            }
        } else if (string.contains("☬ You destroyed an Ender Crystal!")) {
            addLoot("Crystal Fragment", 1);
        }
    }

    public void onItemPickUp(ItemStack itemStack, int amount) {
        if (toRegister.isEmpty() || !isLootItem(itemStack)) return;

        String lootText = formatLootText(itemStack.getName().getString(), amount);
        if (toRegister.remove(lootText) && !lootTexts.contains(lootText)) {
            lootTexts.add(lootText);
            addLoot(itemStack.getName().getString(), amount);
            Scheduler.getInstance().rescheduleTask(savingTaskId, 100);
            ChatUtils.sendMessage("Added loot: " + lootText);
        }
    }

    public void processLoot(String text, Vec3d pos) {
        if (lootTexts.contains(text) || toRegister.contains(text)) return;
        String itemName = getLootFromMessage(text);
        if (itemName != null && text.startsWith(itemName)) {
            Vec3d oldLoot = armorStandLoot.get(text);
            if (oldLoot != null && oldLoot.equals(pos)) return;
            armorStandLoot.put(text, pos);
            toRegister.add(text);
            Scheduler.getInstance().rescheduleTask(savingTask2Id, 100);
            ChatUtils.sendMessage("Detected loot: " + text);
        }
    }

    public void schedule() {
        Scheduler.getInstance().deactivateTask(savingTask2Id);
        if (toRegister.isEmpty()) return;

        ChatUtils.sendMessage("Saving Not picked up loot: ");
        toRegister.forEach(loot -> {
            String itemName = getLootFromMessage(loot);
            int amount = extractAmount(loot);
            addLoot(itemName, amount);
            lootTexts.add(loot);
            ChatUtils.sendMessage(itemName + " x" + amount);
        });
        dragonSlainAndLooted();
    }

    public void dragonSlainAndLooted() {
        Scheduler.getInstance().deactivateTask(savingTaskId);
        ChatUtils.sendMessage("Saving Loot");
        save();
    }

    private String formatLootText(String name, int amount) {
        return name + (amount > 1 ? " x" + amount : "");
    }

    @Override
    public boolean shouldRender() {
        return ConfigManager.getConfig().end.dragonTracker.enableHUD && HypixelData.isInArea("Dragon's Nest");
    }

    @Override
    protected boolean shouldDrawBackground() {
        return ConfigManager.getConfig().end.dragonTracker.enableBackgroundDisplay;
    }

    private int extractAmount(String message) {
        Matcher matcher = AMOUNT_PATTERN.matcher(message);
        if (matcher.find()) {
            String number = matcher.group(1).replace(",", "");
            return Integer.parseInt(number);
        }
        return 1;
    }

    @Override
    protected void update() {
        addText(Text.literal("Dragon Tracker").formatted(Formatting.DARK_PURPLE, Formatting.BOLD));
        TrackerData trackerData = data.get();

        if (trackerData.getCounter() > 0) {
            for (TrackedItem item : trackerData.getLootItems()) {
                if (item.getAmount() > 0) {
                    Text price = ConfigManager.getConfig().end.dragonTracker.showAllPrices && item.getTotalPrice() > 0 ? Text.literal(" (").formatted(Formatting.GRAY)
                            .append(Text.literal(FormattingUtils.formatPrice(item.getTotalPrice())).formatted(Formatting.GOLD))
                            .append(Text.literal(")").formatted(Formatting.GRAY)) : Text.empty();

                    addComponent(new ComponentBuilder()
                            .addText(Text.literal(item.getDisplayName() + ": ").formatted(item.getColor()))
                            .addText(Text.literal(FormattingUtils.formatNumber(item.getAmount())).formatted(Formatting.AQUA))
                            .addText(price)
                    );
                }
            }

            addPadding(3);
            addText(Text.literal("Counter: ").formatted(Formatting.AQUA).append(Text.literal(String.valueOf(trackerData.getCounter())).formatted(Formatting.AQUA)));

            Formatting profitColor = trackerData.getProfit() >= 0 ? Formatting.GREEN : Formatting.RED;
            addText(Text.literal("Profit: ").formatted(Formatting.AQUA).append(Text.literal(FormattingUtils.formatPrice(trackerData.getProfit()) + " Coins").formatted(profitColor)));
        }
    }

    @Override
    protected int getX() {
        return 10;
    }

    @Override
    protected int getY() {
        return 150;
    }
}
