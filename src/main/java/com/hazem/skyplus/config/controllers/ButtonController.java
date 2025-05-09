package com.hazem.skyplus.config.controllers;

import net.minecraft.client.gui.DrawContext;

public class ButtonController extends Controller {
    private final Runnable action;
    private final String name;

    public ButtonController(String name, Runnable action) {
        this.name = name;
        this.action = action;
    }

    @Override
    public void initSize() {
        this.width = textRenderer.getWidth(name) + 6;
        this.height = textRenderer.fontHeight + 6;
        this.minWidth = width + PADDING * 2;
    }

    @Override
    public void onClick() {
        action.run();
    }

    @Override
    public void render(DrawContext context) {
        context.drawBorder(x, y, width, height, 0xFF000000);
        context.drawText(textRenderer, name, x + 4, y + 4, 0xFFFFFFFF, false);
    }
}