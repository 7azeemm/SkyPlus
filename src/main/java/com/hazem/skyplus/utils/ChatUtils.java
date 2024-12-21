package com.hazem.skyplus.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatUtils {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static void sendMessage(String text) {
        sendMessage(Text.of(text));
    }

    public static void sendMessage(Text text) {
        if (CLIENT.player != null) {
            CLIENT.player.sendMessage(text, false);
        }
    }

    public static void sendCommand(String text) {
        if (CLIENT.player != null) {
            CLIENT.player.networkHandler.sendCommand(text);
        }
    }
}
