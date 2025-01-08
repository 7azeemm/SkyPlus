package com.hazem.skyplus.config.gui;

import com.hazem.skyplus.utils.gui.ClickableElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class Category extends ClickableElement {
    private static final int SELECTION_COLOR = 0xFFFFFF;
    private static final int DEFAULT_COLOR = 0xAAAAAA;
    private final Text name;
    private final List<Group> groups;
    private final List<Option> options;
    private boolean visible = false;

    public Category(CategoryBuilder builder) {
        this.name = builder.name;
        this.groups = builder.groups;
        this.options = builder.options;
    }

    public static CategoryBuilder createBuilder() {
        return new CategoryBuilder();
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

    public List<Group> getGroups() {
        return groups;
    }

    public List<Option> getOptions() {
        return options;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    public static class CategoryBuilder {
        private final List<Group> groups = new ArrayList<>();
        private final List<Option> options = new ArrayList<>();
        private Text name;

        public CategoryBuilder name(Text name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder group(Group group) {
            this.groups.add(group);
            return this;
        }

        public CategoryBuilder option(Option option) {
            this.options.add(option);
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}