package com.hazem.skyplus.utils.hud;

import com.hazem.skyplus.utils.hud.components.Component;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

/**
 * Abstract base class representing a widget in the HUD.
 * Subclasses must provide specific rendering logic and position updates.
 */
public abstract class Widget {
    private static final int BACKGROUND_COLOR = 0xc00c0c0c; // Color of the widget's background
    private static final int PADDING = 4; // Padding around components and within the widget
    private final ArrayList<Component> components = new ArrayList<>();
    private int w, h;

    public Widget() {
        HUDMaster.addWidget(this);
    }

    /**
     * Determines if the widget should render.
     * Subclasses must implement this to define their visibility conditions.
     *
     * @return true if the widget should render, false otherwise.
     */
    protected abstract boolean shouldRender();

    /**
     * Updates the state of the widget.
     * Subclasses must implement this to update their components and state.
     */
    protected abstract void update();

    protected abstract int getX();
    protected abstract int getY();

    /**
     * Determines if the widget's background should be drawn.
     * Can be overridden by subclasses to disable background rendering.
     *
     * @return true if the background should be drawn, false otherwise.
     */
    protected boolean shouldDrawBG() {
        return true;
    }

    /**
     * Updates the widget every frame
     */
    public void updateFrame() {
        components.clear();
        update();
        recalculateSize();
    }

    /**
     * Recalculates the size of the widget based on its components.
     */
    private void recalculateSize() {
        h = PADDING; // Start with initial padding
        w = 0; // Reset width
        for (Component c : components) {
            h += c.getHeight() + PADDING; // Add height of each component with padding
            w = Math.max(w, c.getWidth() + PADDING / 2); // Find the widest component
        }
        w += PADDING / 2; // Add padding to the width
    }

    /**
     * Adds a component to the widget.
     *
     * @param component the component to add.
     */
    public void addComponent(Component component) {
        components.add(component);
    }

    /**
     * Renders the widget and its components.
     * This method handles background rendering and delegates component rendering.
     *
     * @param context the draw context for rendering.
     */
    final void render(DrawContext context) {
        int x = getX();
        int y = getY();

        // Draw the widget's background if enabled
        if (shouldDrawBG()) {
            context.fill(x + 1, y, x + w - 1, y + h, BACKGROUND_COLOR); // Main background
            context.fill(x, y + 1, x + 1, y + h - 1, BACKGROUND_COLOR); // Left border
            context.fill(x + w - 1, y + 1, x + w, y + h - 1, BACKGROUND_COLOR); // Right border
        }

        int yOffs = y + PADDING; // Start offset for components

        // Render each component
        for (Component c : components) {
            c.update();
            c.render(context, x + PADDING, yOffs);
            yOffs += c.getHeight() + PADDING; // Increment Y offset by the component's height + padding
        }
    }
}