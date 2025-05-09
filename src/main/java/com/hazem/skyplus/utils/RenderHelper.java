package com.hazem.skyplus.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;

public class RenderHelper {

    public static void drawBackground(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2, y2, color);
    }

    public static void drawBackgroundWithBorder(DrawContext context, int x, int y, int width, int height, int backgroundColor, int borderColor) {
        drawBackground(context, x, y, x + width, y + height, backgroundColor);
        context.drawBorder(x, y, width, height, borderColor);
    }

    public static void drawRoundedBackground(DrawContext context, int x1, int y1, int x2, int y2, int color, int radius) {
        context.fill(x1 + radius, y1, x2 - radius, y2, color); // Main background.

        for (int i = 0; i < radius; i++) {
            context.drawVerticalLine(x1 + i, y1 + radius - i - 1, y2 - radius + i, color); // Left border.
            context.drawVerticalLine(x2 - i - 1, y1 + radius - i - 1, y2 - radius + i, color); // Right border.
        }
    }

    public static void drawLine(DrawContext context, float x1, float y1, float x2, float y2, int color) {
        Matrix4f transformationMatrix = context.getMatrices().peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();

        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);

        buffer.vertex(transformationMatrix, 20, 20, 0).color(0xFFFFFFFF);
        buffer.vertex(transformationMatrix, 5, 40, 0).color(0xFFFFFFFF);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}