package com.hazem.skyplus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hazem.skyplus.Skyplus;
import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.config.categories.GardenCategory;
import com.hazem.skyplus.config.categories.MiscCategory;
import com.hazem.skyplus.config.gui.Category;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(Skyplus.NAMESPACE + ".json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static SkyPlusConfig config;
    private static SkyPlusConfig defaultConfig;

    @Init(priority = Init.Priority.HIGH, ordinal = 1)
    public static void loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            defaultConfig = new SkyPlusConfig();
            config = GSON.fromJson(reader, SkyPlusConfig.class);
            LOGGER.info("[SkyPlus] Loaded SkyPlus Config.");
        } catch (IOException e) {
            // If file doesn't exist or fails to load, create a default config
            defaultConfig = new SkyPlusConfig();
            config = new SkyPlusConfig();
            saveConfig();
            if (e instanceof FileNotFoundException) {
                LOGGER.info("[SkyPlus] Created default SkyPlus config file due to file not found: {}", e.getMessage());
            } else {
                //FIXME: it shouldn't load the default (retry?)
                LOGGER.error("[SkyPlus] Created default SkyPlus config file: {}", e.getMessage());
            }
        }

        //TODO: move to commands Utility or smth
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal(Skyplus.NAMESPACE).then(ClientCommandManager.literal("config")
                .executes(Scheduler.openScreen(() -> createGUI(null))
                )))));
    }

    private static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            LOGGER.error("[SkyPlus] Failed to save SkyPlus config: {}", e.getMessage());
        }
    }

    //TODO: Maybe move to configScreen class
    public static Screen createGUI(Screen parent) {
        List<Category> categories = new ArrayList<>();
        categories.add(GardenCategory.create());
        categories.add(MiscCategory.create());

        return new ConfigScreen(parent, categories);
    }

    public static SkyPlusConfig getConfig() {
        return config;
    }

    public static SkyPlusConfig getDefaultConfig() {
        return defaultConfig;
    }
}
