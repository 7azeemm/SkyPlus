package com.hazem.skyplus.mixins;

import com.hazem.skyplus.config.ConfigManager;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Redirect(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z"))
    private boolean disablePotionOverlay(Collection<?> collection) {
        return ConfigManager.getConfig().misc.disableStatusEffectsOverlay;
    }
}