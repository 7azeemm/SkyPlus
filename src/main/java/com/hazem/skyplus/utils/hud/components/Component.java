package com.hazem.skyplus.utils.hud.components;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a UI component consisting of various elements (e.g., text, icons).
 * The component can have multiple elements arranged horizontally, and it handles rendering
 * and size calculations for those elements.
 */
public class Component {
    protected static final int PADDING = 4;
    protected int width, height;
    private List<Element> elements = new ArrayList<>();

    /**
     * Constructs a new Component using the provided {@link ComponentBuilder}.
     *
     * @param builder The builder containing the elements to be added to the component.
     */
    public Component(ComponentBuilder builder) {
        this.elements = builder.elements;
        update();
    }

    /**
     * Constructs a new Component with a single {@link TextElement}.
     * The element represents the text to be displayed within the component.
     *
     * @param text The text to be displayed as an element in the component.
     */
    public Component(Text text) {
        this.elements.add(new TextElement(text));
        update();
    }

    /**
     * Constructs a new Component with a single {@link ItemStackElement}.
     * The element represents an icon (ItemStack) to be displayed within the component.
     *
     * @param itemStack The icon to be displayed as an element in the component.
     */
    public Component(ItemStack itemStack) {
        this.elements.add(new ItemStackElement(itemStack));
        update();
    }

    /**
     * Renders the component's elements at the specified position.
     * Each element is rendered one after another, arranged horizontally.
     */
    public void render(DrawContext context, int x, int y) {
        for (Element element : elements) {
            element.render(context, x, y);
            x += element.getWidth(); // Move x-position by the width of the rendered element
        }
    }

    /**
     * Updates the dimensions of the component based on the size of the elements it contains.
     */
    public void update() {
        width = PADDING;
        height = 0;

        for (Element element : elements) {
            width += element.getWidth();
            height = Math.max(height, element.getHeight());
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}