package com.hazem.skyplus.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class APIUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(APIUtils.class);
    private static final Gson GSON = new Gson();

    /**
     * Fetches the JSON data from the given API URL and parses it into a JsonObject asynchronously.
     *
     * @param uriString The API URL as a string.
     * @return A CompletableFuture with the parsed JsonObject from the API response.
     */
    public static CompletableFuture<JsonObject> fetchJson(String uriString) {
        // Start a new asynchronous task
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Open a connection to the API
                URL url = new URI(uriString).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // Avoid being blocked by servers
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);

                // Read the response
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    // Parse the response into a JsonObject using Gson
                    return JsonParser.parseString(responseBuilder.toString()).getAsJsonObject();
                } finally {
                    connection.disconnect(); // Ensure the connection is closed
                }
            } catch (Exception e) {
                LOGGER.error("Error fetching JSON from URL: {}", uriString, e);
                return null;
            }
        });
    }

    /**
     * Parses a JSON string into the specified class type.
     *
     * @param json      The JSON string.
     * @param classType The class type to parse into.
     * @param <T>       The type of the class.
     * @return The parsed object, or null if parsing fails.
     */
    public static <T> T parseJsonToClass(JsonObject json, Class<T> classType) {
        if (json == null) {
            LOGGER.error("Cannot parse null JSON to class {}", classType.getName());
            return null;
        }
        try {
            return GSON.fromJson(json.toString(), classType);
        } catch (JsonSyntaxException e) {
            LOGGER.error("Error parsing JSON to class {}", classType.getName(), e);
            return null;
        }
    }

    /**
     * Parses a JSON string into an Object2ObjectOpenHashMap.
     *
     * @param json the JSON string to be parsed.
     * @return an Object2ObjectOpenHashMap containing the parsed key-value pairs.
     * @throws com.google.gson.JsonSyntaxException if the JSON is not well-formed.
     */
    public static Object2ObjectOpenHashMap<String, Object> parseJsonToMap(String json) {
        Type mapType = new TypeToken<Object2ObjectOpenHashMap<String, Object>>() {}.getType();
        return GSON.fromJson(json, mapType);
    }
}