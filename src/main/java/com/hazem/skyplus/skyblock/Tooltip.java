package com.hazem.skyplus.skyblock;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.utils.ItemUtils;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

//Only for testing
public class Tooltip {
    private static final NumberFormat NUMBER_FORMATTER_S = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT);

    static {
        NUMBER_FORMATTER_S.setMaximumFractionDigits(2);
    }

    @Init
    public static void sellPrice() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            if (!stack.isSkyblockItem()) return;
            Optional<Double> price = ItemUtils.getBazaarPrice(stack.getItemId());

            price.ifPresent(p -> lines.add(Text.literal("§aBazaar Price: " + NUMBER_FORMATTER_S.format(p) + " coins")));
        });
    }
}