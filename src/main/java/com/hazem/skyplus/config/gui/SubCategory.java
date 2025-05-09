package com.hazem.skyplus.config.gui;

import com.hazem.skyplus.utils.gui.Element;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public class SubCategory extends Element {
    private static final int NAME_COLOR = 0xAAAAAA;
    private static final int LINE_COLOR = 0xFFAAAAAA;
    private final Text name;
    private final List<Option> options;
    private int left, right;

    public SubCategory(Text name, List<Option> options) {
        this.name = name;
        this.options = options;
    }

    public void setLinePosition(int left, int right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y, this.textRenderer.getWidth(name), this.textRenderer.fontHeight);
    }

    @Override
    public void render(DrawContext context) {
        context.drawCenteredTextWithShadow(this.textRenderer, name, x, y, NAME_COLOR);
        context.drawHorizontalLine(left, x - width / 2 - 5, y + height / 2, LINE_COLOR);
        context.drawHorizontalLine(x + width / 2 + 3, right, y + height / 2, LINE_COLOR);
    }

    public List<Option> getOptions() {
        return options;
    }
}