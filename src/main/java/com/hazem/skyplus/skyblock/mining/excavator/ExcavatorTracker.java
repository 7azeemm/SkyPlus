package com.hazem.skyplus.skyblock.mining.excavator;

import com.hazem.skyplus.annotations.Widget;
import com.hazem.skyplus.config.ConfigManager;
import com.hazem.skyplus.events.SkyBlockEvents;
import com.hazem.skyplus.utils.FormattingUtils;
import com.hazem.skyplus.utils.HypixelData;
import com.hazem.skyplus.utils.hud.components.ComponentBuilder;
import com.hazem.skyplus.utils.hud.tracker.TrackedItem;
import com.hazem.skyplus.utils.hud.tracker.Tracker;
import com.hazem.skyplus.utils.hud.tracker.TrackerData;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Widget
public class ExcavatorTracker extends Tracker {
    private static final String EXCAVATION_COMPLETE_TEXT = "EXCAVATION COMPLETE";
    private static final String NO_LOOT_TEXT = "You didn't find anything. Maybe next time!";
    private static final String END_OF_LOOT_TEXT = "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("x([\\d,]+)");
    private boolean isCollectingLoot = false;

    public ExcavatorTracker() {
        super(ExcavatorLoot.values(), "excavator.json", () -> ConfigManager.getConfig().mining.excavatorTracker.resetMode);
        setConsumable("SUSPICIOUS_SCRAP", "Suspicious Scrap", Formatting.BLUE);
        disableTimer();
        ClientReceiveMessageEvents.GAME.register(this::onChatMessage);
        SkyBlockEvents.AREA_CHANGE.register(this::activate);
    }

    private void onChatMessage(Text text, boolean overlay) {
        if (overlay || !shouldRender()) return;

        if (text.getString().contains(EXCAVATION_COMPLETE_TEXT)) {
            isCollectingLoot = true;
            addConsumable("Suspicious Scrap", 1);
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
        } else if (text.getString().equals(NO_LOOT_TEXT)) {
            addConsumable("Suspicious Scrap", 1);
            markActive(false);
        }
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
    protected boolean shouldRender() {
        return ConfigManager.getConfig().mining.excavatorTracker.enableHUD && HypixelData.isInArea("Fossil Research Center");
    }

    @Override
    protected boolean shouldDrawBackground() {
        return ConfigManager.getConfig().mining.excavatorTracker.enableBackgroundDisplay;
    }

    @Override
    protected void update() {
        addText(Text.literal("Fossil Excavator Tracker").formatted(Formatting.GOLD, Formatting.BOLD));

        TrackerData trackerData = data.get();

        if (trackerData.getCounter() > 0) {
            for (TrackedItem item : trackerData.getLootItems()) {
                if (item.getAmount() > 0) {
                    Text price = ConfigManager.getConfig().mining.excavatorTracker.showAllPrices && item.getTotalPrice() > 0 ? Text.literal(" (").formatted(Formatting.GRAY)
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
            addText(Text.literal("counter: ").formatted(Formatting.AQUA).append(Text.literal(String.valueOf(trackerData.getCounter())).formatted(Formatting.AQUA)));

            Formatting profitColor = trackerData.getProfit() >= 0 ? Formatting.GREEN : Formatting.RED;
            addText(Text.literal("Profit: ").formatted(Formatting.AQUA).append(Text.literal(FormattingUtils.formatPrice(trackerData.getProfit()) + " Coins").formatted(profitColor)));
        }
    }

    @Override
    protected int getX() {
        return 350;
    }

    @Override
    protected int getY() {
        return 150;
    }
}
