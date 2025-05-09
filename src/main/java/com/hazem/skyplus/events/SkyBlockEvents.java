package com.hazem.skyplus.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface SkyBlockEvents {
    /**
     * Triggered when the player joins SkyBlock for the first time.
     */
    Event<FirstJoin> FIRST_JOIN = EventFactory.createArrayBacked(FirstJoin.class, callbacks -> () -> {
        for (FirstJoin callback : callbacks) {
            callback.onFirstJoin();
        }
    });

    /**
     * Triggered when the player's SkyBlock profile ID is detected.
     */
    Event<ProfileIdDetected> PROFILE_ID_DETECTED = EventFactory.createArrayBacked(ProfileIdDetected.class, callbacks -> () -> {
        for (ProfileIdDetected callback : callbacks) {
            callback.onProfileIdDetected();
        }
    });

    /**
     * Triggered when the player switches their SkyBlock profile.
     */
    Event<ProfileChange> PROFILE_CHANGE = EventFactory.createArrayBacked(ProfileChange.class, callbacks -> () -> {
        for (ProfileChange callback : callbacks) {
            callback.onProfileChange();
        }
    });

    /**
     * Triggered when the player switches to a different lobby.
     */
    Event<LobbyChange> LOBBY_CHANGE = EventFactory.createArrayBacked(LobbyChange.class, callbacks -> () -> {
        for (LobbyChange callback : callbacks) {
            callback.onLobbyChange();
        }
    });

    /**
     * Triggered when the player's location changes.
     */
    Event<LocationChange> LOCATION_CHANGE = EventFactory.createArrayBacked(LocationChange.class, callbacks -> () -> {
        for (LocationChange callback : callbacks) {
            callback.onLocationChange();
        }
    });

    /**
     * Triggered when the player's area changes.
     */
    Event<AreaChange> AREA_CHANGE = EventFactory.createArrayBacked(AreaChange.class, callbacks -> () -> {
        for (AreaChange callback : callbacks) {
            callback.onAreaChange();
        }
    });

    /**
     * Triggered when the player joins SkyBlock.
     */
    Event<Join> JOIN = EventFactory.createArrayBacked(Join.class, callbacks -> () -> {
        for (Join callback : callbacks) {
            callback.onJoin();
        }
    });

    /**
     * Triggered when the player leaves Skyblock.
     */
    Event<Leave> LEAVE = EventFactory.createArrayBacked(Leave.class, callbacks -> () -> {
        for (Leave callback : callbacks) {
            callback.onLeave();
        }
    });

    // Functional interfaces for the events

    @FunctionalInterface
    interface FirstJoin {
        void onFirstJoin();
    }

    @FunctionalInterface
    interface ProfileIdDetected {
        void onProfileIdDetected();
    }

    @FunctionalInterface
    interface ProfileChange {
        void onProfileChange();
    }

    @FunctionalInterface
    interface LobbyChange {
        void onLobbyChange();
    }

    @FunctionalInterface
    interface LocationChange {
        void onLocationChange();
    }

    @FunctionalInterface
    interface AreaChange {
        void onAreaChange();
    }

    @FunctionalInterface
    interface Join {
        void onJoin();
    }

    @FunctionalInterface
    interface Leave {
        void onLeave();
    }
}