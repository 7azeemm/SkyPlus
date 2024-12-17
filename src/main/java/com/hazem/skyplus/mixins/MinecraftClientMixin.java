package com.hazem.skyplus.mixins;

import com.hazem.skyplus.utils.gui.EmptyScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ReconfiguringScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public abstract ClientPlayNetworkHandler getNetworkHandler();

    @ModifyVariable(at = @At("HEAD"), method = "setScreen", ordinal = 0, argsOnly = true)
    public Screen BypassLoadingScreen(Screen screen) {
        return switch (screen) {
            case DownloadingTerrainScreen ignored -> null;
            case ReconfiguringScreen ignored when this.getNetworkHandler() != null ->
                    new EmptyScreen(this.getNetworkHandler().getConnection());

            case null, default -> screen;
        };
    }

    @ModifyArg(method = "joinWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"), index = 0)
    private Screen modifyJoinWorldScreen(Screen screen) {
        return new EmptyScreen();
    }
}
