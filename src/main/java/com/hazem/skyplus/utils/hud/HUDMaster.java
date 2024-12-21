package com.hazem.skyplus.utils.hud;

import com.hazem.skyplus.annotations.Init;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.ArrayList;
import java.util.List;

public class HUDMaster {
    public static final List<Widget> widgets = new ArrayList<>();

    @Init(priority = Init.Priority.HIGH, ordinal = 2)
    public static void init() {
        HudRenderCallback.EVENT.register(HUDMaster::renderAll);
    }

    public static void addWidget(Widget widget) {
        widgets.add(widget);
    }

    private static void renderAll(DrawContext drawContext, RenderTickCounter tickDelta) {
        for (Widget widget : widgets) {
            if (widget.shouldRender()) {
                widget.updateFrame();
                widget.render(drawContext);
            }
        }
    }
}
