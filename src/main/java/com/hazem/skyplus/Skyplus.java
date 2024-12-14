package com.hazem.skyplus;

import com.hazem.skyplus.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class Skyplus implements ClientModInitializer {
    private static Skyplus INSTANCE;
    public static final String NAMESPACE = "skyplus";

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        ConfigManager.init();
        int a = 5;
    }

    public static Skyplus getInstance() {
        return INSTANCE;
    }
}
