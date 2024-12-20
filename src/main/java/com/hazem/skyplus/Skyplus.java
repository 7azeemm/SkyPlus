package com.hazem.skyplus;

import com.hazem.skyplus.annotations.InitProcessor;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Skyplus implements ClientModInitializer {
    private static Skyplus INSTANCE;
    public static final String NAMESPACE = "skyplus";

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(Scheduler.getInstance()::tick);
        INSTANCE = this;

        InitProcessor.process();
        //https://github.com/hannibal002/SkyHanni/blob/beta/src/main/java/at/hannibal2/skyhanni/features/garden/GardenNextJacobContest.kt
    }

    //TODO: Tick annotation for methods (makes them run every tick)

    public static Skyplus getInstance() {
        return INSTANCE;
    }
}
