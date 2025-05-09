package com.hazem.skyplus.utils.hud.components;

import net.minecraft.client.gui.DrawContext;

public class PaddingElement implements Element {
    private final int padding;

    public PaddingElement(int padding) {
        this.padding = padding;
    }

    @Override
    public void render(DrawContext context, int x, int y) {
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return padding;
    }
}
