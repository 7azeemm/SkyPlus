package com.hazem.skyplus.utils.hud.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TextElement implements Element {
    private final TextRenderer textRenderer;
    private final Text text;

    public TextElement(Text text) {
        this.text = text;
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    @Override
    public void render(DrawContext context, int x, int y) {
        // Retrieve the color from the text style, defaulting to white (0xFFFFFF) if no color is set
        int color = text.getStyle().getColor() != null ? text.getStyle().getColor().getRgb() : 0xFFFFFF;
        context.drawText(textRenderer, text, x, y, color, true);
    }

    @Override
    public int getWidth() {
        return textRenderer.getWidth(text); // Get the width of the text using the renderer
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight; // Return the font height as the text height
    }
}