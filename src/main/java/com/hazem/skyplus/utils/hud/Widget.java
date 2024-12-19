package com.hazem.skyplus.utils.hud;

import com.hazem.skyplus.utils.hud.components.Component;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Supplier;

/**
 * Abstract base class representing a widget in the HUD.
 * Subclasses must provide specific rendering logic and position updates.
 */
public abstract class Widget {
    private static final int BACKGROUND_COLOR = 0xc00c0c0c; // Color of the widget's background
    private static final int PADDING = 4; // Padding around components and within the widget
    private final ObjectArrayList<Component> components = new ObjectArrayList<>();
    private final ObjectArrayList<Trackable<?>> trackables = new ObjectArrayList<>();
    private int w, h;

    public Widget() {
        HUDMaster.addWidget(this);
        trackables();
    }

    /**
     * Abstract method for adding trackables to the widget.
     * Subclasses should override this method to define the specific values to track.
     * This method should use {@link #addTrackable(Supplier)} to register each value
     * that needs to be monitored for changes.
     */
    public abstract void trackables();

    /**
     * Adds a trackable supplier to the list of items to be monitored for changes.
     *
     * @param supplier The supplier that provides the current value to track
     */
    public void addTrackable(Supplier<?> supplier) {
        trackables.add(new Trackable<>(supplier));  // Wrap the supplier in a Trackable object
    }

    /**
     * Checks if any of the trackables have changed.
     *
     * @return true if any tracked value has changed, false otherwise
     */
    private boolean needsUpdate() {
        if (trackables.isEmpty() && components.isEmpty()) return true;
        return trackables.stream().anyMatch(Trackable::isChanged);  // Check if any trackable has changed
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
        // If there are no changes, no need to update the widget
        if (!needsUpdate()) return;

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