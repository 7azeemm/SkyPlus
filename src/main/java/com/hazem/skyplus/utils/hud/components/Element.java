package com.hazem.skyplus.utils.hud.components;

import net.minecraft.client.gui.DrawContext;

/**
 * An interface representing an element that can be rendered within a component.
 * Elements may be text, icons, or other graphical components that can be rendered
 * on the screen. Each element is responsible for rendering itself and providing
 * its own dimensions (width and height).
 */
public interface Element {

    /**
     * Renders the element at the specified position.
     */
    void render(DrawContext context, int x, int y);

    /**
     * Gets the width of the element.
     */
    int getWidth();

    /**
     * Gets the height of the element.
     */
    int getHeight();
}