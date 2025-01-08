package com.hazem.skyplus.constants;

import java.util.HashMap;
import java.util.Map;

public enum Location {
    HUB("hub"),
    PRIVATE_ISLAND("dynamic"),
    GARDEN("garden"),
    THE_FARMING_ISLAND("farming_1"),
    GOLD_MINE("mining_1"),
    DEEP_CAVERNS("mining_2"),
    DWARVEN_MINES("mining_3"),
    CRYSTAL_HOLLOWS("crystal_hollows"),
    GLACITE_MINESHAFT("mineshaft"),
    THE_PARK("foraging_1"),
    SPIDERS_DEN("combat_1"),
    THE_END("combat_3"),
    CRIMSON_ISLE("crimson_isle"),
    DUNGEON_HUB("dungeon_hub"),
    DUNGEON("dungeon"),
    KUUDRAS_HOLLOW("kuudra"),
    WINTER_ISLAND("winter"),
    THE_RIFT("rift"),
    DARK_AUCTION("dark_auction"),
    UNKNOWN("unknown");

    private static final Map<String, Location> LOOKUP_MAP = new HashMap<>();

    static {
        for (Location location : Location.values()) {
            LOOKUP_MAP.put(location.id, location);
        }
    }

    private final String id;

    Location(String id) {
        this.id = id;
    }

    public static Location from(String id) {
        return LOOKUP_MAP.getOrDefault(id, UNKNOWN);
    }
}
