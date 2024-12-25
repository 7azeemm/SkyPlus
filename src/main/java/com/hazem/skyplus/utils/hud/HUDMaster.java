package com.hazem.skyplus.utils.hud;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.ArrayList;
import java.util.List;

public class HUDMaster {
    private static final int TICK_INTERVAL = 1;
    private static final List<AbstractWidget> widgets = new ArrayList<>();

    @Init(priority = Init.Priority.HIGH, ordinal = 2)
    private static void init() {
        Scheduler.getInstance().scheduleCyclic(HUDMaster::updateAll, 0, TICK_INTERVAL);
        HudRenderCallback.EVENT.register(HUDMaster::renderAll);
    }

    public static void addWidget(AbstractWidget widget) {
        widgets.add(widget);
    }

    private static void updateAll() {
        for (AbstractWidget widget : widgets) {
            if (widget.shouldRender()) {
                widget.updateFrame();
            }
        }
    }

    private static void renderAll(DrawContext drawContext, RenderTickCounter tickDelta) {
        for (AbstractWidget widget : widgets) {
            if (widget.shouldRender()) {
                widget.render(drawContext);
            }
        }
    }
}
/*
After finishing your screen, in order to avoid potential issues, please check:

whether the screen returns to the last screen (parent screen) when you press “Esc”
whether these classes exist only on client (which means they will not be loaded in the dedicated server)
whether elements are focused in the correct order when you press “Tab” to select them
whether the behaviors are correct when you resize
whether the narrations are correct when you use “Tab” or mouse cursor to select element while narration enabled
 */