package com.hazem.skyplus.config.controllers;

import com.hazem.skyplus.config.gui.Option;
import com.hazem.skyplus.utils.gui.ClickableElement;
import net.minecraft.client.gui.DrawContext;

public abstract class Controller extends ClickableElement {
    public abstract void init(Option option);
    public abstract int getMinWidth();

    public void render(DrawContext context, float scrollOffset, int mouseX, int mouseY) {
        render(context);

        if (isHovered(mouseX, mouseY, scrollOffset)) {
            context.fill(x, y, right, bottom, 0x20FFFFFF);
        }
    }
}