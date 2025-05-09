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

    public Option(Text name, Text description, Controller controller) {
        this.name = name;
        this.description = description;
        this.controller = controller;
    }

    @Override
    public void render(DrawContext context) {
        RenderHelper.drawBackgroundWithBorder(context, x, y, width, height, BACKGROUND_COLOR, BORDER_COLOR);
        context.drawWrappedText(this.textRenderer, name, nameX, nameY, getMaxWidth(), NAME_COLOR, false);
        if (hasDescription())
            context.drawWrappedTextWithShadow(this.textRenderer, description, descriptionX, descriptionY, getMaxWidth(), DESCRIPTION_COLOR);
    }

    @Override
    public void init(int x, int y, int width) {
        super.init(x, y, width);
        controller.initSize();
        height = calculateHeight();
        bottom = y + height;
        controller.init(this);
    }

    private int calculateHeight() {
        int height = PADDING + 1; // Top padding

        nameY = y + height;
        nameX = x + PADDING;
        height += this.textRenderer.getWrappedLinesHeight(name, getMaxWidth());

        if (hasDescription()) {
            descriptionY = y + height;
            descriptionX = x + PADDING;
            height += this.textRenderer.getWrappedLinesHeight(description, getMaxWidth());
        }

        height += PADDING; // Bottom padding

        return height;
    }

    public int getMaxWidth() {
        return width - controller.getMinWidth();
    }

    public boolean hasDescription() {
        return description != null;
    }

    public Controller getController() {
        return controller;
    }
}