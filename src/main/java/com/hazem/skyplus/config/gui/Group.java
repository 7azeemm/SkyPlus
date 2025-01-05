package com.hazem.skyplus.config.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final Text name;
    private final List<Option> options;
    private int titleX, titleY;
    private int left, right;
    private int titleWidth;

    public Group(GroupBuilder builder) {
        this.name = builder.name;
        this.options = builder.options;
    }

    public static GroupBuilder createBuilder() {
        return new GroupBuilder();
    }

    public void setTitlePosition(int titleX, int titleY, int left, int right) {
        this.titleX = titleX;
        this.titleY = titleY;
        this.left = left;
        this.right = right;
        this.titleWidth = MinecraftClient.getInstance().textRenderer.getWidth(name);
    }

    public int getTitleWidth() {
        return titleWidth;
    }

    public int getTitleX() {
        return titleX;
    }

    public int getTitleY() {
        return titleY;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public Text getName() {
        return name;
    }

    public List<Option> getGroups() {
        return options;
    }

    public static class GroupBuilder {
        private final List<Option> options = new ArrayList<>();
        private Text name;

        public GroupBuilder name(Text name) {
            this.name = name;
            return this;
        }

        public GroupBuilder option(Option option) {
            this.options.add(option);
            return this;
        }

        public Group build() {
            return new Group(this);
        }
    }
}