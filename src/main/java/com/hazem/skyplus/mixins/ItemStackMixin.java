package com.hazem.skyplus.mixins;

import com.hazem.skyplus.injections.SkyblockItem;
import com.hazem.skyplus.utils.ItemUtils;
import net.minecraft.component.ComponentHolder;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public class ItemStackMixin implements SkyblockItem {

    @Unique
    private String itemId;

    @Unique
    private Boolean isSkyblockItem;

    @Unique
    @Override
    public String getItemId() {
        if (itemId != null) return itemId;
        return itemId = ItemUtils.getItemId((ComponentHolder) this);
    }

    @Unique
    @Override
    public boolean isSkyblockItem() {
        if (isSkyblockItem != null) return isSkyblockItem;
        return isSkyblockItem = ItemUtils.isSkyblockItem((ComponentHolder) this);
    }
}