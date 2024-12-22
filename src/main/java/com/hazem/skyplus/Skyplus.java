package com.hazem.skyplus;

import com.hazem.skyplus.annotations.processors.InitProcessor;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Skyplus implements ClientModInitializer {
    public static final String NAMESPACE = "skyplus";

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(Scheduler.getInstance()::tick);
        InitProcessor.process();
    }
}
