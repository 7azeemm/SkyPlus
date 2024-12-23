package com.hazem.skyplus.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class APIUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(APIUtils.class);

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
}