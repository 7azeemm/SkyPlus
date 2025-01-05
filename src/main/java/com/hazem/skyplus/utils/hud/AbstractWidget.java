package com.hazem.skyplus.utils.hud;

import com.hazem.skyplus.utils.hud.components.Component;
import com.hazem.skyplus.utils.hud.components.ComponentBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/**
 * Represents a HUD Widget that can display various components (e.g., text, icons) on the screen.
 */
public abstract class AbstractWidget {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final int BACKGROUND_COLOR = 0xc00c0c0c; // Widget's background color.
    private static final int PADDING = 4; // Padding around and within the widget.
    private static final int BASE_WIDTH = 1366;
    private static final int BASE_HEIGHT = 768;
    private final ObjectArrayList<Component> components = new ObjectArrayList<>();
    private int width, height;

    /**
     * Determines whether the widget should render on the screen.
     *
     * @return true if the widget should render, false otherwise.
     */
    protected abstract boolean shouldRender();

    /**
     * Updates the widget's state (e.g., components or internal data) before rendering.
     * This method should be implemented by subclasses to define specific behavior.
     */
    protected abstract void update();

    /**
     * Gets the X-coordinate where the widget should be rendered.
     *
     * @return the X-coordinate of the widget.
     */
    protected abstract int getX();

    /**
     * Gets the Y-coordinate where the widget should be rendered.
     *
     * @return the Y-coordinate of the widget.
     */
    protected abstract int getY();

    /**
     * Indicates whether the widget should draw its background.
     * Subclasses can override this to disable the background rendering.
     *
     * @return true if the background should be drawn, false otherwise.
     */
    protected boolean shouldDrawBackground() {
        return true;
    }

    /**
     * Updates the widget for the current frame by clearing old components,
     * running the update logic, and recalculating the widget's size.
     */
    final void updateFrame() {
        components.clear(); // Clear previous components.
        update(); // Update widget logic.
        recalculateSize(); // Adjust size based on components.
    }

    /**
     * Recalculates the size of the widget based on the dimensions of its components.
     */
    final void recalculateSize() {
        height = PADDING / 2; // Initialize with top padding.
        width = 0;

        for (Component component : components) {
            height += component.getHeight() + PADDING; // Add component height and padding.
            width = Math.max(width, component.getWidth() + PADDING - 1); // Adjust to the widest component.
        }
    }

    /**
     * Adds a custom-built component to the widget.
     *
     * @param builder the builder used to create the component.
     */
    public void addComponent(ComponentBuilder builder) {
        components.add(new Component(builder));
    }

    /**
     * Adds a text component to the widget.
     *
     * @param text the text to be displayed in the widget.
     */
    public void addText(Text text) {
        components.add(new Component(text));
    }

    /**
     * Adds an itemStack to the widget.
     *
     * @param itemStack the ItemStack to be displayed as an icon.
     */
    public void addIcon(ItemStack itemStack) {
        components.add(new Component(itemStack));
    }

    //TODO: fix size calculations (should be in init method)
    final void render(DrawContext context) {
        // Get the current window size
        int windowWidth = CLIENT.getWindow().getWidth();
        int windowHeight = CLIENT.getWindow().getHeight();
        double scaleFactor = CLIENT.getWindow().getScaleFactor();

        // Calculate the x and y positions based on the base resolution and window size
        int x = (int) Math.round((getX() / scaleFactor) * windowWidth / BASE_WIDTH);
        int y = (int) Math.round((getY() / scaleFactor) * windowHeight / BASE_HEIGHT);

        // Ensure the widget doesn't go off the right edge of the window
        if ((x + width) * scaleFactor > windowWidth) {
            x = (int) Math.ceil((windowWidth - (width * scaleFactor)) / scaleFactor); // Adjust x so the widget fits within the window width
        }

        // Draw the background if enabled.
        if (shouldDrawBackground()) {
            drawBackground(context, x, y, width, height);
        }

        // Render each component with proper padding.
        int yOffset = y + PADDING; // Start below the top padding.
        for (Component component : components) {
            component.render(context, x + PADDING, yOffset); // Render the component.
            yOffset += component.getHeight() + PADDING; // Move to the next component position.
        }
    }

    private void drawBackground(DrawContext context, int x, int y, int width, int height) {
        context.fill(x + 1, y, x + width - 1, y + height, BACKGROUND_COLOR); // Main background.
        context.fill(x, y + 1, x + 1, y + height - 1, BACKGROUND_COLOR); // Left border.
        context.fill(x + width - 1, y + 1, x + width, y + height - 1, BACKGROUND_COLOR); // Right border.
    }
}