package com.hazem.skyplus.config.gui;

import com.hazem.skyplus.config.controllers.Controller;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public class Option {
    private final static int PADDING = 4;
    private final Text name;
    private final Text description;
    private final Controller controller;
    private int x, y;
    private int right, bottom;
    private int width, height;
    private int nameX, nameY;
    private int descriptionX, descriptionY;
    private int maxWidth;

    public Option(OptionBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.controller = builder.controller;
    }

    public static OptionBuilder createBuilder() {
        return new OptionBuilder();
    }

    public void setPosition(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.maxWidth = width - controller.getMinWidth();
        this.height = calculateHeight();
        this.right = x + width;
        this.bottom = y + height;
        this.controller.init(this);
    }

    private int calculateHeight() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int height = 0;

        height += 2; // Border's lines (Top and Bottom)
        height += PADDING; // Top padding

        this.nameY = y + height;
        this.nameX = x + PADDING;
        height += textRenderer.getWrappedLinesHeight(name, maxWidth);

        if (hasDescription()) {
            this.descriptionY = y + height;
            this.descriptionX = x + PADDING;
            height += textRenderer.getWrappedLinesHeight(description, maxWidth);
        }

        height += PADDING; // Bottom padding

        return height;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getNameY() {
        return nameY;
    }

    public int getDescriptionY() {
        return descriptionY;
    }

    public int getDescriptionX() {
        return descriptionX;
    }

    public int getNameX() {
        return nameX;
    }

    public Text getName() {
        return name;
    }

    public Text getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public Controller getController() {
        return controller;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public int getHeight() {
        return height;
    }

    public static class OptionBuilder {
        private Text name;
        private Text description;
        private Controller controller;

        public OptionBuilder name(Text name) {
            this.name = name;
            return this;
        }

        public OptionBuilder description(Text description) {
            this.description = description;
            return this;
        }

        public OptionBuilder controller(Controller controller) {
            this.controller = controller;
            return this;
        }

        public Option build() {
            return new Option(this);
        }
    }
}