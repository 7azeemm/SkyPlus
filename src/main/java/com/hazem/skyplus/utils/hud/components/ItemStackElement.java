package com.hazem.skyplus.utils.hud.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class ItemStackElement implements Element {
    private static final int ICON_SIZE = 16; // Standard icon dimensions (Minecraft item icon size)
    private static final int SCALED_ICON_SIZE = 14; // Desired height for the rendered icon
    private static final float SCALE_FACTOR = (float) SCALED_ICON_SIZE / ICON_SIZE; // Scaling factor for resizing the icon
    private final TextRenderer textRenderer;
    private final ItemStack itemStack;

    public ItemStackElement(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    @Override
    public void render(DrawContext context, int x, int y) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y - 3, 0); // Apply a small vertical offset for padding
        context.getMatrices().scale(SCALE_FACTOR, SCALE_FACTOR, 1.0f); // Scale the icon to the desired height
        context.drawItem(itemStack, 0, 0); // Draw the item icon
        context.getMatrices().pop(); // Restore the matrix state
    }

    @Override
    public int getWidth() {
        return SCALED_ICON_SIZE;
    }

    @Override
    public int getHeight() {
        return textRenderer.fontHeight + 1; // Return the font height as the icon height plus some padding
    }
}