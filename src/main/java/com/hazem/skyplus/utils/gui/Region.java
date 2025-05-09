package com.hazem.skyplus.utils.gui;

public class Region {
    protected int x, y, right, bottom, width, height;

    public Region() {
    }

    public Region(int x, int y, int width, int height) {
        init(x, y, width, height);
    }

    public void init(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.right = x + width;
        this.bottom = y + height;
    }

    public void init(int x, int y) {
        this.x = x;
        this.y = y;
        this.right = x + width;
        this.bottom = y + height;
    }

    // For Text elements.
    public void init(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.right = x + width;
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