package com.hazem.skyplus.config.gui;

public class GuiArea {
    private final int x, y, right, bottom, width, height;

    public GuiArea(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.right = x + width;
        this.bottom = y + height;
    }

    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= right && mouseY >= y && mouseY <= bottom;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}