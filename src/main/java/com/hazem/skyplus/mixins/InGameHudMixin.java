package com.hazem.skyplus.mixins;

import com.hazem.skyplus.config.ConfigManager;
import com.hazem.skyplus.events.HudRenderEvents;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Redirect(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z"))
    private boolean disablePotionOverlay(Collection<?> collection) {
        return ConfigManager.getConfig().misc.disableStatusEffectsOverlay;
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDrawer;addLayer(Lnet/minecraft/client/gui/LayeredDrawer$Layer;)Lnet/minecraft/client/gui/LayeredDrawer;", ordinal = 2))
    private LayeredDrawer.Layer injectCustomHudLayer(LayeredDrawer.Layer mainHudLayer) {
        return (context, tickCounter) -> {
            mainHudLayer.render(context, tickCounter);
            HudRenderEvents.EVENT.invoker().onRender(context, tickCounter);
        };
    }
}