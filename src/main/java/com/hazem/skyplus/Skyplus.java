package com.hazem.skyplus;

import com.hazem.skyplus.config.ConfigManager;
import com.hazem.skyplus.utils.HypixelData;
import com.hazem.skyplus.utils.hud.HUDMaster;
import com.hazem.skyplus.utils.schedular.Scheduler;
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
        HUDMaster.init();
        HypixelData.init();
    }

    //TODO: Tick annotation for methods (makes them run every tick)
    public void tick(MinecraftClient client) {
        Scheduler.getInstance().tick(client);
    }

    public static Skyplus getInstance() {
        return INSTANCE;
    }
}
