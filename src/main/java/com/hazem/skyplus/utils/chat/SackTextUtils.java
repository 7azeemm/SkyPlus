package com.hazem.skyplus.utils.chat;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.events.ChatEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SackTextUtils {

    @Init(priority = Init.Priority.LOW)
    public static void registerEvent() {
        //TODO: idk if i should use ALLOW_GAME or GAME or custom event (custom event with condition maybe? also separete overlay and non-overlay to different events, also one can cancel and one no).
        ClientReceiveMessageEvents.GAME.register(SackTextUtils::onChatMessage);
    }

    private static void onChatMessage(Text text, boolean overlay) {
        if (overlay || !text.getString().startsWith("[Sacks]")) return;

        List<Text> siblings = text.getSiblings();
        if (siblings.isEmpty()) return;

        // Maps to store added and removed items separately
        Map<String, Integer> addedItems = new HashMap<>();
        Map<String, Integer> removedItems = new HashMap<>();

        // Determine items based on sibling count
        if (siblings.size() == 4) {
            TextColor color = siblings.getFirst().getStyle().getColor();
            if (color != null) {
                extractItems(siblings.getFirst(), color.getName().equals("#55FF55") ? addedItems : removedItems);
            }
        } else if (siblings.size() == 7) {
            extractItems(siblings.getFirst(), addedItems);
            extractItems(siblings.get(4), removedItems);
        }

        // Fire the events only if there are changes
        if (!addedItems.isEmpty()) {
            ChatEvents.SACKS_ITEMS_ADDED.invoker().onSacksItemsAdded(addedItems);
        }
        if (!removedItems.isEmpty()) {
            ChatEvents.SACKS_ITEMS_REMOVED.invoker().onSacksItemsRemoved(removedItems);
        }
    }

    /**
     * Extracts item names and their amounts from the hover text.
     *
     * @param sibling The sibling text containing the hover event.
     * @param targetMap The map where extracted items will be stored.
     */
    private static void extractItems(Text sibling, Map<String, Integer> targetMap) {
        HoverEvent hoverEvent = sibling.getStyle().getHoverEvent();
        if (hoverEvent == null) return;

        Text hoverText = hoverEvent.getValue(HoverEvent.Action.SHOW_TEXT);
        if (hoverText != null) {
            parseSacksMessage(hoverText.getSiblings(), targetMap);
        }
    }

    /**
     * Parses the hover text to extract items and their amounts.
     *
     * @param lines The list of text lines containing item information.
     * @param targetMap The map where extracted items will be stored.
     */
    private static void parseSacksMessage(List<Text> lines, Map<String, Integer> targetMap) {
        int lastAmount = 0;

        for (Text lineText : lines.subList(0, lines.size() - 2)) {
            String line = lineText.getString().trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("+") || line.startsWith("-")) {
                try {
                    lastAmount = Integer.parseInt(line.substring(1).replace(",", "").trim());
                } catch (NumberFormatException ignored) {
                    lastAmount = 0;
                }
            } else if (lastAmount != 0) {
                targetMap.put(line, lastAmount);
                lastAmount = 0;
            }
        }
    }
}
