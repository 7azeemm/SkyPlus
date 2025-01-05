package com.hazem.skyplus.config.gui;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final Text name;
    private final List<Group> groups;
    private final List<Option> options; // Standalone options not tied to a group
    private int titleX, titleY;

    public Category(CategoryBuilder builder) {
        this.name = builder.name;
        this.groups = builder.groups;
        this.options = builder.options;
    }

    public static CategoryBuilder createBuilder() {
        return new CategoryBuilder();
    }

    public void setTitlePosition(int titleX, int titleY) {
        this.titleX = titleX;
        this.titleY = titleY;
    }

    public int getTitleX() {
        return titleX;
    }

    public int getTitleY() {
        return titleY;
    }

    public Text getName() {
        return name;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Option> getOptions() {
        return options;
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