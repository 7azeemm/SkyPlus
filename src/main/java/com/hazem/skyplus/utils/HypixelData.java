package com.hazem.skyplus.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.constants.Location;
import com.hazem.skyplus.events.SkyBlockEvents;
import com.hazem.skyplus.utils.schedular.Scheduler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.scoreboard.*;
import net.minecraft.text.Text;

import java.util.List;

public class HypixelData {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final ObjectArrayList<String> SCOREBOARD_LINES = new ObjectArrayList<>();
    private static final String PROFILE_ID_PREFIX = "Profile ID: ";
    private static final String PROFILE_DASHING_TEXT = "§e§lCLICK THIS TO SUGGEST IT IN CHAT";
    private static final int PROFILE_ID_REQUEST_DELAY = 20 * 8; // 8 seconds
    private static final int LOC_RAW_REQUEST_DELAY = 5; // 0.25 seconds
    private static boolean profileIdRequested = false;
    private static int messagesToSuppress; // Tracks how many messages left to suppress

    public static boolean isInHypixel = false;
    public static boolean isInHypixelAlpha = false;
    public static boolean isInSkyblock = false;
    public static String server = "";
    public static String profileID = "";
    public static Location location = Location.UNKNOWN;
    public static String area = "";

    @Init(priority = Init.Priority.HIGH)
    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((handler, packetSender, client) -> onJoin(handler));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> onDisconnect());
        ClientReceiveMessageEvents.ALLOW_GAME.register(HypixelData::onChatMessage);
        Scheduler.getInstance().scheduleCyclic(HypixelData::getScoreboardLines, 0,20);
    }

    public static boolean isInArea(String area) {
        return HypixelData.area.equals(area);
    }

    public static boolean isInIsland(List<Location> islands) {
        return islands.contains(location);
    }

    public static boolean isInIsland(Location island) {
        return location.equals(island);
    }

    private static void onJoin(ClientPlayNetworkHandler handler) {
        SkyBlockEvents.LOBBY_CHANGE.invoker().onLobbyChange();
        SkyBlockEvents.LOCATION_CHANGE.invoker().onLocationChange();

        messagesToSuppress = 0;
        ServerInfo serverInfo = handler.getServerInfo();
        if (serverInfo != null) {
            String serverAddress = serverInfo.address;
            if (serverAddress.contains(".hypixel.")) {
                isInHypixel = true;
                isInHypixelAlpha = serverAddress.startsWith("alpha.");
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
                    String oldId = profileID;

                    profileID = message.substring(PROFILE_ID_PREFIX.length());
                    profileIdRequested = false;

                    if (oldId.isEmpty()) SkyBlockEvents.FIRST_JOIN.invoker().onFirstJoin();
                    if (!oldId.equals(profileID)) SkyBlockEvents.PROFILE_CHANGE.invoker().onProfileChange();

                    SkyBlockEvents.PROFILE_ID_DETECTED.invoker().onProfileIdDetected();
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

    private static void getScoreboardLines() {
        if (CLIENT.player == null) return;
        SCOREBOARD_LINES.clear();
        Scoreboard scoreboard = CLIENT.player.getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.FROM_ID.apply(1));
        boolean detectedArea = false;

        if (objective != null) SCOREBOARD_LINES.add(objective.getDisplayName().getString());
        for (ScoreHolder scoreHolder : scoreboard.getKnownScoreHolders()) {
            if (scoreboard.getScoreHolderObjectives(scoreHolder).containsKey(objective)) {
                Team team = scoreboard.getScoreHolderTeam(scoreHolder.getNameForScoreboard());
                if (team != null) {
                    String strLine = team.getPrefix().getString() + team.getSuffix().getString();
                    if (!strLine.isBlank()) {
                        SCOREBOARD_LINES.add(strLine);
                        if (isInSkyblock) {
                            if (strLine.contains("⏣") || strLine.contains("ф")) {
                                String oldArea = area;
                                area = strLine.replaceAll("[⏣ф]", "").trim();
                                detectedArea = true;

                                if (!oldArea.equals(area)) SkyBlockEvents.AREA_CHANGE.invoker().onAreaChange();
                            }
                        }
                    }
                }
            }
        }

        if (!detectedArea) area = "";
    }

    private static void sendLocrawRequest() {
        Scheduler.getInstance().schedule(() -> ChatUtils.sendCommand("locraw"), LOC_RAW_REQUEST_DELAY);
    }

    //TODO: remove from command history + request id + also too many messages to suppress
    private static void scheduleProfileIdRequest() {
        Scheduler.getInstance().schedule(() -> {
            if (profileIdRequested) {
                messagesToSuppress = 2;
                ChatUtils.sendCommand("profileid");
            }
        }, PROFILE_ID_REQUEST_DELAY);
    }

    private static void parseLocRaw(String text) {
        JsonObject locRaw = JsonParser.parseString(text).getAsJsonObject();

        boolean inSkyblock = isInSkyblock;

        isInSkyblock = locRaw.has("gametype") && locRaw.get("gametype").getAsString().equals("SKYBLOCK");
        server = locRaw.has("server") ? locRaw.get("server").getAsString() : "";
        location = locRaw.has("mode") ? Location.from(locRaw.get("mode").getAsString()) : Location.UNKNOWN;

        if (isInSkyblock && !inSkyblock) {
            SkyBlockEvents.JOIN.invoker().onJoin();
        } else if (!isInSkyblock && inSkyblock) {
            SkyBlockEvents.LEAVE.invoker().onLeave();
        }
    }

    private static void onDisconnect() {
        isInHypixel = false;
        isInHypixelAlpha = false;
        isInSkyblock = false;
        server = "";
        location = Location.UNKNOWN;
        area = "";
        messagesToSuppress = 0;
    }
}
