package com.hazem.skyplus;

import com.hazem.skyplus.config.ConfigManager;
import com.hazem.skyplus.skyblock.garden.JacobContestHUD;
import com.hazem.skyplus.utils.hud.HUDMaster;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class Skyplus implements ClientModInitializer {
    private static Skyplus INSTANCE;
    public static final String NAMESPACE = "skyplus";

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        INSTANCE = this;
        ConfigManager.init();
        //
        HUDMaster.init();
    }

    public void tick(MinecraftClient client) {

    }

    public static Skyplus getInstance() {
        return INSTANCE;
    }
}
