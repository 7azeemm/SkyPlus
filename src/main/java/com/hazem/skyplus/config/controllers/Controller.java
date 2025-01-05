package com.hazem.skyplus.config.controllers;

import com.hazem.skyplus.config.gui.Option;
import net.minecraft.client.gui.DrawContext;

public abstract class Controller {
    protected int x, y;
    protected int width, height;
    protected int right, bottom;

    protected abstract void render(DrawContext context, Option option);
    public abstract void init(Option option);
    public abstract boolean buttonClicked(double mouseX, double mouseY, float scrollOffset);
    public abstract int getMinWidth();

    public void render(DrawContext context, Option option, float scrollOffset, int mouseX, int mouseY) {
        render(context, option);

        if (isHovered(mouseX, mouseY, scrollOffset)) {
            context.fill(x, y, right, bottom, 0x20FFFFFF);
        }
    }

    public boolean isHovered(double mouseX, double mouseY, float scrollOffset) {
        return mouseX >= x && mouseX <= right && mouseY >= y - scrollOffset && mouseY <= bottom - scrollOffset;
    }
}