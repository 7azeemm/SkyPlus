package com.hazem.skyplus.skyblock.mining.powderMining;

import com.hazem.skyplus.annotations.Widget;
import com.hazem.skyplus.config.ConfigManager;
import com.hazem.skyplus.constants.Location;
import com.hazem.skyplus.events.ChatEvents;
import com.hazem.skyplus.utils.FormattingUtils;
import com.hazem.skyplus.utils.HypixelData;
import com.hazem.skyplus.utils.hud.components.ComponentBuilder;
import com.hazem.skyplus.utils.hud.tracker.TrackedItem;
import com.hazem.skyplus.utils.hud.tracker.Tracker;
import com.hazem.skyplus.utils.hud.tracker.TrackerData;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Widget
public class PowderMiningTracker extends Tracker {
    private static final String TREASURE_CHEST_OPEN_TEXT = "CHEST LOCKPICKED";
    private static final String END_OF_LOOT_TEXT = "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("x([\\d,]+)");
    private boolean isCollectingLoot = false;
    private boolean includesHardStone;

    public PowderMiningTracker() {
        super(TreasureChestLoot.values(), "powder.json", () -> ConfigManager.getConfig().mining.powderMiningTracker.resetMode);

        registerItemRule("ENCHANTED_HARD_STONE", () -> ConfigManager.getConfig().mining.powderMiningTracker.includeHardStone);
        registerItemRule("HARD_STONE", () -> ConfigManager.getConfig().mining.powderMiningTracker.includeHardStone);
        includesHardStone = ConfigManager.getConfig().mining.powderMiningTracker.includeHardStone;

        ClientReceiveMessageEvents.GAME.register(this::onChatMessage);
        ChatEvents.SACKS_ITEMS_ADDED.register(this::onSacksItemsAdded);
    }

    private void onSacksItemsAdded(Map<String, Integer> items) {
        if (active && ConfigManager.getConfig().mining.powderMiningTracker.includeHardStone) {
            processSackLoot(items, "Enchanted Hard Stone");
            processSackLoot(items, "Hard Stone");
        }
    }

    private void processSackLoot(Map<String, Integer> items, String itemName) {
        items.computeIfPresent(itemName, (name, amount) -> {
            addLoot(itemName, amount);
            return amount;
        });
    }

    private void onChatMessage(Text text, boolean overlay) {
        if (overlay || !isEnabled()) return;

        if (text.getString().contains(TREASURE_CHEST_OPEN_TEXT)) {
            isCollectingLoot = true;
        } else if (isCollectingLoot) {
            String message = text.getString();

            String loot = getLootFromMessage(message);
            if (loot != null) {
                int amount = extractAmount(message);
                addLoot(loot, amount);
                return;
            }

            // Stop collecting loot if the message indicates the end of the rewards
            if (message.equals(END_OF_LOOT_TEXT)) {
                isCollectingLoot = false;
                markActive(false);
            }
        }
    }

    //TODO: move
    private int extractAmount(String message) {
        Matcher matcher = AMOUNT_PATTERN.matcher(message);
        if (matcher.find()) {
            String number = matcher.group(1).replace(",", "");
            return Integer.parseInt(number);
        }
        return 1;
    }

    private boolean isEnabled() {
        return ConfigManager.getConfig().mining.powderMiningTracker.enableHUD && HypixelData.isInIsland(Location.CRYSTAL_HOLLOWS);
    }

    @Override
    protected boolean shouldRender() {
        return isEnabled() && active;
    }

    @Override
    protected boolean shouldDrawBackground() {
        return ConfigManager.getConfig().mining.powderMiningTracker.enableBackgroundDisplay;
    }

    @Override
    protected void update() {
        if (active) {
            boolean newIncludesHardStone = ConfigManager.getConfig().mining.powderMiningTracker.includeHardStone;
            if (newIncludesHardStone != includesHardStone) {
                updateProfit();
                includesHardStone = newIncludesHardStone;
            }

            addText(Text.literal("Powder Mining Tracker").formatted(Formatting.DARK_AQUA, Formatting.BOLD));
            TrackerData trackerData = data.get();

            if (trackerData.getCounter() > 0) {
                for (TrackedItem item : trackerData.getLootItems()) {
                    if (item.getAmount() > 0 && shouldTrack(item.getId())) {
                        Text price = ConfigManager.getConfig().mining.powderMiningTracker.showAllPrices && item.getTotalPrice() > 0 ? Text.literal(" (").formatted(Formatting.GRAY)
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
                addText(Text.literal("Chests: ").formatted(Formatting.AQUA).append(Text.literal(String.valueOf(trackerData.getCounter())).formatted(Formatting.AQUA)));
                addText(Text.literal("Timer: ").formatted(Formatting.AQUA).append(Text.literal(FormattingUtils.formatTime(trackerData.getTimer())).formatted(Formatting.AQUA)));
                addText(Text.literal("Profit: ").formatted(Formatting.AQUA).append(Text.literal(FormattingUtils.formatPrice(trackerData.getProfit()) + " Coins").formatted(Formatting.GREEN)));
            }
        }
    }

    @Override
    protected int getX() {
        return 600;//10
    }

    @Override
    protected int getY() {
        return 50;//150
    }
}
