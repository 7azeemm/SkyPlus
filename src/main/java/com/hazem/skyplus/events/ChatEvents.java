package com.hazem.skyplus.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Map;

public interface ChatEvents {

    /**
     * Event triggered when items are added to a Sack.
     * Provides a mapping of item names to their respective added amounts.
     */
    Event<SacksItemsAdded> SACKS_ITEMS_ADDED = EventFactory.createArrayBacked(SacksItemsAdded.class,
            callbacks -> (items) -> {
                for (SacksItemsAdded callback : callbacks) {
                    callback.onSacksItemsAdded(items);
                }
            }
    );

    /**
     * Event triggered when items are removed from a Sack.
     * Provides a mapping of item names to their respective removed amounts.
     */
    Event<SacksItemsRemoved> SACKS_ITEMS_REMOVED = EventFactory.createArrayBacked(SacksItemsRemoved.class,
            callbacks -> (items) -> {
                for (SacksItemsRemoved callback : callbacks) {
                    callback.onSacksItemsRemoved(items);
                }
            }
    );

    // Functional interfaces for the events

    @FunctionalInterface
    interface SacksItemsAdded {
        /**
         * Called when items are added to a Sack.
         *
         * @param items A map containing item names as keys and their added amounts as values.
         */
        void onSacksItemsAdded(Map<String, Integer> items);
    }

    @FunctionalInterface
    interface SacksItemsRemoved {
        /**
         * Called when items are removed from a Sack.
         *
         * @param items A map containing item names as keys and their removed amounts as values.
         */
        void onSacksItemsRemoved(Map<String, Integer> items);
    }
}