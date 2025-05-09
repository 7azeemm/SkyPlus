package com.hazem.skyplus.mixins;

import com.hazem.skyplus.skyblock.end.dragon.DragonTracker;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onEntityTrackerUpdate", at = @At("TAIL"))
    private void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci, @Local Entity entity) {
        if (!(entity instanceof ArmorStandEntity armorStandEntity)) return;
        if (DragonTracker.INSTANCE != null && DragonTracker.INSTANCE.shouldRender()) {
            if (!armorStandEntity.getName().getString().equals("Armor Stand")) {
                DragonTracker.INSTANCE.processLoot(armorStandEntity.getName().getString(), armorStandEntity.getBlockPos().toCenterPos());
            }
        }
    }

    @WrapWithCondition(method = "onPlayerList", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false))
    private boolean suppressPlayerListWarning(Logger instance, String format, Object arg1, Object arg2) {
        return false;
    }

    @WrapWithCondition(method = "onEntityPassengersSet", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;)V"), remap = false)
    private boolean suppressUnknownEntityWarning(Logger logger, String message) {
        return false;
    }
}
