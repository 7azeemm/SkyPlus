package com.hazem.skyplus.config.gui;

import com.hazem.skyplus.config.controllers.Controller;
import com.hazem.skyplus.utils.RenderHelper;
import com.hazem.skyplus.utils.gui.Element;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class Option extends Element {
    private final static int BACKGROUND_COLOR = 0x80141414;
    private final static int BORDER_COLOR = 0xFF000000;
    private final static int NAME_COLOR = 0xFFFFFF;
    private final static int DESCRIPTION_COLOR = 0x737373;
    private final static int PADDING = 4;
    private final Text name;
    private final Text description;
    private final Controller controller;
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

    @Override
    public void render(DrawContext context) {
        RenderHelper.drawBackgroundWithBorder(context, x, y, right, bottom, BACKGROUND_COLOR, BORDER_COLOR);
        context.drawWrappedText(this.textRenderer, name, nameX, nameY, maxWidth, NAME_COLOR, false);
        if (hasDescription())
            context.drawWrappedTextWithShadow(this.textRenderer, description, descriptionX, descriptionY, maxWidth, DESCRIPTION_COLOR);
    }

    @Override
    public void init(int x, int y, int width) {
        this.maxWidth = width - controller.getMinWidth();
        super.init(x, y, width);
        height = calculateHeight();
        bottom = y + height;
        controller.init(this);
    }

    private int calculateHeight() {
        int height = 0;

        height += 2; // Border's lines (Top and Bottom)
        height += PADDING; // Top padding

        nameY = y + height;
        nameX = x + PADDING;
        height += this.textRenderer.getWrappedLinesHeight(name, maxWidth);

        if (hasDescription()) {
            descriptionY = y + height;
            descriptionX = x + PADDING;
            height += this.textRenderer.getWrappedLinesHeight(description, maxWidth);
        }

        height += PADDING; // Bottom padding

        return height;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public Controller getController() {
        return controller;
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