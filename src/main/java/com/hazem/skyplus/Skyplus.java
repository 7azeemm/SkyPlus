package com.hazem.skyplus;

import com.hazem.skyplus.annotations.processors.InitProcessor;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class Skyplus implements ClientModInitializer {
    public static final String NAMESPACE = "skyplus";

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(Scheduler.getInstance()::tick);
        InitProcessor.process();
    }

    public static String getModVersion() {
        return FabricLoader.getInstance()
                .getModContainer(NAMESPACE)
                .map(ModContainer::getMetadata)
                .map(metadata -> metadata.getVersion().getFriendlyString())
                .orElse("Unknown");
    }
}
//TODO: Catalogue compatibility