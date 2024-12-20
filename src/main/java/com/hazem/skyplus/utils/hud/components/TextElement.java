package com.hazem.skyplus.utils.hud.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TextElement implements Element {

    private static TextRenderer TEXT_RENDERER;
    private final Text text;

    public TextElement(Text text) {
        this.text = text;
        TEXT_RENDERER = MinecraftClient.getInstance().textRenderer;
    }

    @Override
    public void render(DrawContext context, int x, int y) {
        // Retrieve the color from the text style, defaulting to white (0xFFFFFF) if no color is set
        int color = text.getStyle().getColor() != null ? text.getStyle().getColor().getRgb() : 0xFFFFFF;
        context.drawText(TEXT_RENDERER, text, x, y, color, false);
    }

    @Override
    public int getWidth() {
        return TEXT_RENDERER.getWidth(text); // Get the width of the text using the renderer
    }

    @Override
    public int getHeight() {
        return TEXT_RENDERER.fontHeight; // Return the font height as the text height
    }
}