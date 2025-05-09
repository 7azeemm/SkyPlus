package com.hazem.skyplus.utils.hud;

import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.events.HudRenderEvents;
import com.hazem.skyplus.utils.schedular.Scheduler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HUDMaster {
    private static final int TICK_INTERVAL = 1;
    private static final List<AbstractWidget> WIDGETS = new ArrayList<>();

    @Init(priority = Init.Priority.HIGH, ordinal = 2)
    private static void init() {
        Scheduler.getInstance().scheduleCyclic(HUDMaster::updateAll, 0, TICK_INTERVAL);
        HudRenderEvents.EVENT.register(HUDMaster::renderAll);
    }

    public static void addWidget(AbstractWidget widget) {
        WIDGETS.add(widget);
    }

    private static void updateAll() {
        for (AbstractWidget widget : WIDGETS) {
            if (widget.shouldRender()) {
                widget.updateFrame();
            }
        }
    }

    private static void renderAll(DrawContext drawContext, RenderTickCounter tickDelta) {
        for (AbstractWidget widget : WIDGETS) {
            if (widget.shouldRender()) {
                widget.render(drawContext);
            }
        }
    }

    public static <T extends AbstractWidget> Optional<T> getWidgetOfType(Class<T> widgetClass) {
        for (AbstractWidget widget : WIDGETS) {
            if (widgetClass.isInstance(widget)) {
                return Optional.of(widgetClass.cast(widget));
            }
        }
        return Optional.empty();
    }
}