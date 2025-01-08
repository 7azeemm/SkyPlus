package com.hazem.skyplus.config.controllers;

import com.hazem.skyplus.config.gui.Option;
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
    private static final int PADDING = 10;
    private static final int MIN_WIDTH = BUTTON_WIDTH + PADDING * 2;
    private final boolean defaultValue;
    private final Consumer<Boolean> consumer;
    private boolean toggle;

    public BooleanController(Consumer<Boolean> consumer, boolean toggle) {
        this.consumer = consumer;
        this.toggle = toggle;
        this.defaultValue = toggle;
    }

    @Override
    public void init(Option option) {
        x = option.getX() + option.getMaxWidth() + PADDING;
        y = option.getY() + (int) Math.ceil((double) option.getHeight() / 2) - BUTTON_HEIGHT / 2;
        width = BUTTON_WIDTH;
        height = BUTTON_HEIGHT;
        right = x + width;
        bottom = y + height;
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

    @Override
    public int getMinWidth() {
        return MIN_WIDTH;
    }
}