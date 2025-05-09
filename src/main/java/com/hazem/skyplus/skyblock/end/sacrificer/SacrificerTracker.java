package com.hazem.skyplus.skyblock.end.sacrificer;

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

//TODO: add an option to use craftCost data instead of lowest BIN for consumables.
@Widget
public class SacrificerTracker extends Tracker {
    private static final Pattern SACRIFICE_PATTERN = Pattern.compile("SACRIFICE! You turned (.+) into (\\d+) Dragon Essence!");
    private static final Pattern BONUS_LOOT_PATTERN = Pattern.compile("BONUS LOOT! You also received (?:(\\d+)x )?(.+) from your sacrifice!");

    public SacrificerTracker() {
        super(SacrificerLoot.values(), "sacrificer.json", () -> ConfigManager.getConfig().end.sacrificerTracker.resetMode);
        setConsumableTable(Sacrificables.values());
        disableTimer();
        ClientReceiveMessageEvents.GAME.register(this::onChatMessage);
        SkyBlockEvents.AREA_CHANGE.register(this::activate);
    }

    private void onChatMessage(Text text, boolean overlay) {
        if (overlay || !shouldRender()) return;

        Matcher sacrificeMatcher = SACRIFICE_PATTERN.matcher(text.getString());

        if (sacrificeMatcher.matches()) {
            addConsumable(sacrificeMatcher.group(1), 1);
            addLoot("Dragon Essence", Integer.parseInt(sacrificeMatcher.group(2)));
            markActive(true);
        } else {
            Matcher bonusLootMatcher = BONUS_LOOT_PATTERN.matcher(text.getString());
            if (bonusLootMatcher.matches()) {
                int amount = bonusLootMatcher.group(1) != null ? Integer.parseInt(bonusLootMatcher.group(1)) : 1;
                String itemName = bonusLootMatcher.group(2);
                addLoot(itemName, amount);
            }
        }
    }

    @Override
    protected boolean shouldRender() {
        return ConfigManager.getConfig().end.sacrificerTracker.enableHUD && HypixelData.isInArea("Void Slate");
    }

    @Override
    protected boolean shouldDrawBackground() {
        return ConfigManager.getConfig().end.sacrificerTracker.enableBackgroundDisplay;
    }

    @Override
    protected void update() {
        addText(Text.literal("Sacrificer Tracker").formatted(Formatting.RED, Formatting.BOLD));
        TrackerData trackerData = data.get();

        if (trackerData.getCounter() > 0) {
            for (TrackedItem item : trackerData.getLootItems()) {
                if (item.getAmount() > 0) {
                    Text price = ConfigManager.getConfig().end.sacrificerTracker.showAllPrices && item.getTotalPrice() > 0 ? Text.literal(" (").formatted(Formatting.GRAY)
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