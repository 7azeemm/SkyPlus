package com.hazem.skyplus.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

public class HypixelData {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final String PROFILE_ID_PREFIX = "Profile ID: ";
    private static final String PROFILE_DASHING_TEXT = "§e§lCLICK THIS TO SUGGEST IT IN CHAT";
    private static final int PROFILE_ID_REQUEST_DELAY = 20 * 8; // 8 seconds
    private static final int LOC_RAW_REQUEST_DELAY = 5; // 0.25 seconds
    private static boolean profileIdRequested = false;
    private static int messagesToSuppress = 0; // Tracks how many messages left to suppress

    public static boolean isInHypixel = false;
    public static boolean isInHypixelAlpha = false;
    public static boolean isInSkyblock = false;
    public static String server = "";
    public static String profileID = "";
    public static Location location = Location.UNKNOWN;

    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((handler, packetSender, client) -> onJoin(handler));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> onDisconnect());
        ClientReceiveMessageEvents.ALLOW_GAME.register(HypixelData::onChatMessage);
    }

    private static void onJoin(ClientPlayNetworkHandler handler) {
        messagesToSuppress = 0;
        ServerInfo serverInfo = handler.getServerInfo();
        if (serverInfo != null) {
            String serverAddress = serverInfo.address;
            if (serverAddress.contains("hypixel.net")) {
                isInHypixel = true;
                isInHypixelAlpha = serverAddress.contains("alpha.");
                sendLocrawRequest();
            }
        }
    }

    private static boolean onChatMessage(Text text, boolean overlay) {
        if (overlay) return true; // Skip overlay messages
        String message = text.getString();

        if (isInHypixel) {
            if (message.startsWith("{\"server\":") && message.endsWith("}")) {
                parseLocRaw(message);
                if (isInSkyblock) {
                    profileIdRequested = true;
                    scheduleProfileIdRequest();
                }
                return false; // Suppress /locraw output
            }

            if (isInSkyblock) {
                if (message.startsWith(PROFILE_ID_PREFIX)) {
                    profileID = message.substring(PROFILE_ID_PREFIX.length());
                    profileIdRequested = false;
                }

                // Suppress the two unwanted messages after PROFILE_ID_PREFIX (DASHES - NO DASHES)
                if (messagesToSuppress > 0 && message.startsWith(PROFILE_DASHING_TEXT)) {
                    messagesToSuppress--;
                    return false;
                }
            }
        }

        return true;
    }

    private static void sendLocrawRequest() {
        Scheduler.getInstance().schedule(() -> {
            if (CLIENT.player != null) {
                CLIENT.player.networkHandler.sendChatMessage("/locraw");
            }
        }, LOC_RAW_REQUEST_DELAY);
    }

    private static void scheduleProfileIdRequest() {
        Scheduler.getInstance().schedule(() -> {
            if (profileIdRequested && CLIENT.player != null) {
                messagesToSuppress = 2;
                CLIENT.player.networkHandler.sendChatMessage("/profileid");
            }
        }, PROFILE_ID_REQUEST_DELAY);// 8 seconds
    }

    private static void parseLocRaw(String text) {
        JsonObject locRaw = JsonParser.parseString(text).getAsJsonObject();

        isInSkyblock = locRaw.has("gametype") && locRaw.get("gametype").getAsString().equals("SKYBLOCK");
        server = locRaw.has("server") ? locRaw.get("server").getAsString() : "";
        location = locRaw.has("mode") ? Location.from(locRaw.get("mode").getAsString()) : Location.UNKNOWN;
    }

    private static void onDisconnect() {
        isInHypixel = false;
        isInHypixelAlpha = false;
        isInSkyblock = false;
        server = "";
        location = Location.UNKNOWN;
        messagesToSuppress = 0;
    }
}
