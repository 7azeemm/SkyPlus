package com.hazem.skyplus.skyblock;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.data.PriceResult;
import com.hazem.skyplus.utils.ItemUtils;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

//Only for testing
public class Tooltip {
    //TODO: move to formatting utility class
    private static final NumberFormat NUMBER_FORMATTER_S = NumberFormat.getCompactNumberInstance(Locale.CANADA, NumberFormat.Style.SHORT);

    static {
        NUMBER_FORMATTER_S.setMaximumFractionDigits(2);
    }

    @Init
    public static void tooltip() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            if (!stack.isSkyblockItem()) return;

            String itemId = stack.getItemId();

            Optional<PriceResult> priceResult = ItemUtils.getItemPrice(itemId);
            priceResult.ifPresent(result -> lines.add(Text.literal("§aPrice: " + NUMBER_FORMATTER_S.format(result.price()) + " coins")));
        });
    }
}