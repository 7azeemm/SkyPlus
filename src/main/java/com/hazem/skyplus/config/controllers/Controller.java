package com.hazem.skyplus.config.controllers;

import com.hazem.skyplus.config.gui.Option;
import com.hazem.skyplus.utils.gui.ClickableElement;
import net.minecraft.client.gui.DrawContext;

public abstract class Controller extends ClickableElement {
    protected static final int PADDING = 10;
    protected Option option;
    int minWidth;

    public abstract void initSize();

    public void init(Option option) {
        super.init(option.getRight() - width - PADDING, option.getY() + (int) Math.ceil((double) option.getHeight() / 2) - height / 2);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float scrollOffset) {
        render(context);
        hover(context, mouseX, mouseY, scrollOffset);
    }

    public void hover(DrawContext context, int mouseX, int mouseY, float scrollOffset) {
        if (isHovered(mouseX, mouseY, scrollOffset)) {
            context.fill(x, y, right, bottom, 0x20FFFFFF);
        }
    }

    public int getMinWidth() {
        return minWidth;
    }
}