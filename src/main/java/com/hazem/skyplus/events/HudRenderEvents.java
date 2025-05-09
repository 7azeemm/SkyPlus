package com.hazem.skyplus.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public interface HudRenderEvents {
    Event<HudRenderEvents> EVENT = EventFactory.createArrayBacked(HudRenderEvents.class, (listeners) -> (context, tickDelta) -> {
                for (HudRenderEvents listener : listeners) {
                    listener.onRender(context, tickDelta);
                }
            });

    /**
     * Called after rendering the whole hud.
     *
     * @param context the {@link DrawContext} instance
     * @param tickDelta the {@link RenderTickCounter} instance
     */
    void onRender(DrawContext context, RenderTickCounter tickDelta);
}
