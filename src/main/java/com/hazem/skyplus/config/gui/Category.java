package com.hazem.skyplus.config.gui;

import com.hazem.skyplus.utils.gui.ClickableElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Category extends ClickableElement {
    private static final int SELECTION_COLOR = 0xFFFFFF;
    private static final int DEFAULT_COLOR = 0xAAAAAA;
    private final Text name;
    private final List<SubCategory> subCategories;
    private final List<Option> options;
    private boolean visible = false;

    public Category(Text name, List<SubCategory> subCategories, List<Option> options) {
        this.name = name;
        this.subCategories = subCategories;
        this.options = options;
    }

    @Override
    public void render(DrawContext context) {
        int color = isVisible() ? SELECTION_COLOR : DEFAULT_COLOR;
        Text categoryName = visible ? name.copyContentOnly().formatted(Formatting.UNDERLINE) : name;

        context.drawCenteredTextWithShadow(this.textRenderer, categoryName, x, y, color);
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y, this.textRenderer.getWidth(name), this.textRenderer.fontHeight);
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY) {
        int textWidth = width + 1;
        int textHeight = height + 1;
        int textX = x - textWidth / 2 - 1;
        int textY = y - 1;

        return mouseX >= textX && mouseX <= textX + textWidth && mouseY >= textY && mouseY <= textY + textHeight;
    }

    public Text getName() {
        return name;
    }

    @Override
    public void onClick() {
        visible = true;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public List<Option> getOptions() {
        return options;
    }

    public List<Option> getAllOptions() {
        return Stream.concat(
                options.stream(),
                subCategories.stream().flatMap(subCategory -> subCategory.getOptions().stream())
        ).collect(Collectors.toList());
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }
}