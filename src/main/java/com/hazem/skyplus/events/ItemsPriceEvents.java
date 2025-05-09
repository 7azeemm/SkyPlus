package com.hazem.skyplus.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ItemsPriceEvents {

    /**
     * Triggered when prices are updated.
     */
    Event<PriceUpdate> PRICE_UPDATE = EventFactory.createArrayBacked(PriceUpdate.class, callbacks -> () -> {
        for (PriceUpdate callback : callbacks) {
            callback.onPriceUpdate();
        }
    });

    // Functional interfaces for the events

    @FunctionalInterface
    interface PriceUpdate {
        void onPriceUpdate();
    }
}
