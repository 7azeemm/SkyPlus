package com.hazem.skyplus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hazem.skyplus.Skyplus;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility class responsible for loading and saving the configuration for SkyPlus.
 */
public class ConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(Skyplus.NAMESPACE + ".json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Loads the SkyPlus configuration from the config file. If the file does not exist or cannot be read,
     * a default configuration is created and saved.
     *
     * @return The loaded or newly created {@link SkyPlusConfig}.
     */
    public static SkyPlusConfig loadConfig() {
        SkyPlusConfig config;
        try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
            config = GSON.fromJson(reader, SkyPlusConfig.class);
            LOGGER.info("[SkyPlus] Loaded SkyPlus Config.");
        } catch (IOException e) {
            config = new SkyPlusConfig();
            saveConfig(config);
            if (!(e instanceof FileNotFoundException)) {
                // FIXME: it shouldn't load the default (retry?) (or cache the corrupted file maybe (should be fixed with configDataFixer?))
                LOGGER.error("[SkyPlus] Error loading config, default config created: {}", e.getMessage());
            }
        }
        return config;
    }

    /**
     * Saves the given {@link SkyPlusConfig} to the config file.
     *
     * @param config The configuration object to save.
     */
    public static void saveConfig(SkyPlusConfig config) {
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            LOGGER.error("[SkyPlus] Failed to save SkyPlus config: {}", e.getMessage());
        }
    }
}