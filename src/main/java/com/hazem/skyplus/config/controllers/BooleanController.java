package com.hazem.skyplus.config.controllers;

import com.hazem.skyplus.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class BooleanController extends Controller {
    private static final int BUTTON_WIDTH = 22;
    private static final int BUTTON_HEIGHT = 12;
    private static final int SQUIRE_SIZE = BUTTON_HEIGHT - 2;
    private static final int SQUIRE_COLOR = 0xFF141414;
    private static final int TRUE_COLOR = 0xFF00cccc;
    private static final int FALSE_COLOR = 0xFF7c7c7c;
    private final Consumer<Boolean> consumer;
    private final boolean defaultValue;
    private boolean toggle;

    public BooleanController(Consumer<Boolean> consumer, boolean toggle) {
        this.consumer = consumer;
        this.toggle = toggle;
        this.defaultValue = toggle;
    }

    @Override
    public void initSize() {
        this.width = BUTTON_WIDTH;
        this.height = BUTTON_HEIGHT;
        this.minWidth = width + PADDING * 2;
    }

    @Override
    public void render(DrawContext context) {
        int bgColor = toggle ? TRUE_COLOR : FALSE_COLOR;
        int targetX = toggle ? x + width / 2 : x + 1;

        RenderHelper.drawBackground(context, x, y, right, bottom, bgColor);
        RenderHelper.drawBackground(context, targetX, y + 1, targetX + SQUIRE_SIZE, y + SQUIRE_SIZE + 1, SQUIRE_COLOR);
    }

    @Override
    public void onClick() {
        this.toggle = !toggle;
        this.consumer.accept(toggle);
    }
}