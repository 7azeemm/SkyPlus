package com.hazem.skyplus.utils.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class Element extends Region {
    protected final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    protected Element() {
        super();
    }

    public abstract void render(DrawContext context);

    public boolean isHovered(double mouseX, double mouseY, float scrollOffset) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y - scrollOffset && mouseY <= y + height - scrollOffset;
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
