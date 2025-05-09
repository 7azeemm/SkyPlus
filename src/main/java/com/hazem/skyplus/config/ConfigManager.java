package com.hazem.skyplus.config;

import com.hazem.skyplus.Skyplus;
import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.config.gui.Category;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigManager {
    private static SkyPlusConfig config;
    private static SkyPlusConfig defaultConfig;

    @Init(priority = Init.Priority.HIGH, ordinal = 1)
    public static void loadConfig() {
        config = ConfigLoader.loadConfig();
        defaultConfig = new SkyPlusConfig();

        // Register the command to open the configuration GUI
        // TODO: Move this to a more appropriate utility or command class
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal(Skyplus.NAMESPACE)
                        .executes(Scheduler.openScreen(() -> createGUI(null))))));
    }

    /**
     * Creates the configuration GUI using the loaded configuration categories.
     *
     * @param parent The parent screen for the GUI (can be null).
     * @return A new instance of the {@link ConfigScreen} with the loaded configuration categories.
     */
    public static Screen createGUI(@Nullable Screen parent) {
        List<Category> categories = ConfigProcessor.createCategoriesFromConfig(config);
        return new ConfigScreen(parent, categories);
    }

    /**
     * Saves the current configuration to the config file.
     */
    public static void saveConfig() {
        ConfigLoader.saveConfig(config);
    }

    /**
     * Retrieves the current configuration.
     *
     * @return The current {@link SkyPlusConfig}.
     */
    public static SkyPlusConfig getConfig() {
        return config;
    }

    /**
     * Retrieves the default configuration.
     *
     * @return The default {@link SkyPlusConfig}.
     */
    public static SkyPlusConfig getDefaultConfig() {
        return defaultConfig;
    }
}