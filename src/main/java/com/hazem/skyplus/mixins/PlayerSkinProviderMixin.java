package com.hazem.skyplus.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.texture.PlayerSkinProvider$1")
public class PlayerSkinProviderMixin {

    @WrapWithCondition(method = "method_54647", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false))
    private static boolean suppressInvalidSignatureWarnings(Logger logger, String message, Object profileId) {
        return false;
    }
}
