package com.hazem.skyplus.utils;

import net.minecraft.client.gui.DrawContext;

public class RenderHelper {

    public static void drawBackground(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2, y2, color);
    }

    public static void drawBackgroundWithBorder(DrawContext context, int x1, int y1, int x2, int y2, int backgroundColor, int borderColor) {
        drawBackground(context, x1, y1, x2, y2, backgroundColor);
        drawBorder(context, x1, y1, x2, y2, borderColor);
    }

    public static void drawRoundedBackground(DrawContext context, int x1, int y1, int x2, int y2, int color, int radius) {
        context.fill(x1 + radius, y1, x2 - radius, y2, color); // Main background.

        for (int i = 0; i < radius; i++) {
            context.drawVerticalLine(x1 + i, y1 + radius - i - 1, y2 - radius + i, color); // Left border.
            context.drawVerticalLine(x2 - i - 1, y1 + radius - i - 1, y2 - radius + i, color); // Right border.
        }
    }

    public static void drawBorder(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.drawHorizontalLine(x1, x2, y1, color);
        context.drawHorizontalLine(x1, x2, y2, color);
        context.drawVerticalLine(x1, y1, y2, color);
        context.drawVerticalLine(x2, y1, y2, color);
    }
}