package com.hazem.skyplus.skyblock;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.data.itemPrice.PriceResult;
import com.hazem.skyplus.utils.FormattingUtils;
import com.hazem.skyplus.utils.ItemUtils;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;

import java.util.Optional;

//Only for testing
public class Tooltip {

    @Init
    public static void tooltip() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            String itemId = stack.getItemId();

            Optional<PriceResult> priceResult = ItemUtils.getItemPrice(itemId);
            priceResult.ifPresent(result -> lines.add(Text.literal("§aPrice: " + FormattingUtils.formatPrice(result.price()) + " coins")));

            Optional<PriceResult> npcPrice = ItemUtils.getNPCPrice(itemId);
            npcPrice.ifPresent(result -> lines.add(Text.literal("§aNPC Price: " + FormattingUtils.formatPrice(result.price()) + " coins")));
        });
    }
}