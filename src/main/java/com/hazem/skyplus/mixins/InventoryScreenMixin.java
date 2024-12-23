package com.hazem.skyplus.mixins;

import com.hazem.skyplus.config.ConfigManager;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/StatusEffectsDisplay;drawStatusEffects(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    private boolean shouldRenderStatusEffects(StatusEffectsDisplay instance, DrawContext context, int mouseX, int mouseY, float tickDelta) {
        return !ConfigManager.getHandler().misc.disableStatusEffectsOverlay;
    }
}