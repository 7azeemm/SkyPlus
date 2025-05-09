package com.hazem.skyplus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * A generic class to handle loading and saving data to a file using Codecs.
 *
 * @param <T> The type of data to be serialized/deserialized.
 */
public class PersistentData<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistentData.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Codec<Map<String, T>> codec;
    private Path filePath;

    /**
     * Constructs a PersistentData instance with a specified file path and codec.
     *
     * @param filePath The file path where the data will be stored.
     * @param codec    The codec for encoding and decoding the data.
     */
    protected PersistentData(Path filePath, Codec<T> codec) {
        this.filePath = filePath;
        this.codec = Codec.unboundedMap(Codec.STRING, codec);
    }

    /**
     * Loads the data from the file.
     * If the file doesn't exist, an empty map is returned.
     *
     * @return A mutable map containing the loaded data.
     */
    public final Map<String, T> load() {
        Map<String, T> loadedData = new HashMap<>();

        try {
            if (!Files.exists(filePath)) return loadedData;

            String json = Files.readString(filePath, StandardCharsets.UTF_8);
            DataResult<Map<String, T>> dataResult = codec.parse(JsonOps.INSTANCE, JsonParser.parseString(json));

            dataResult.ifError(mapError -> LOGGER.error("[SkyPlus] Error parsing data: {}", mapError.message()));
            dataResult.ifSuccess(loadedData::putAll);
        } catch (IOException e) {
            LOGGER.error("[SkyPlus] Error reading or parsing file: {}", filePath, e);
        }
        return loadedData;
    }

    /**
     * Saves the given data to the file.
     *
     * @param data The data to be saved.
     */
    public final void save(Map<String, T> data) {
        try {
            Files.createDirectories(filePath.getParent());

            JsonElement jsonElement = codec.encodeStart(JsonOps.INSTANCE, data).result().orElseThrow();
            String jsonString = GSON.toJson(jsonElement);

            Files.writeString(filePath, jsonString, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("[SkyPlus] Error writing to file: {}", filePath, e);
        }
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }
}