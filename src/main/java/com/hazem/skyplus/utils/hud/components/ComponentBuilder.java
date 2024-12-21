package com.hazem.skyplus.utils.hud.components;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/**
 * A builder class for constructing a {@link Component}.
 * This class provides methods to add various elements (text or icons) to the component,
 * allowing for a flexible and readable way to construct a component with different content.
 */
public class ComponentBuilder {
    protected final ObjectArrayList<Element> elements = new ObjectArrayList<>();

    /**
     * Adds a {@link TextElement} to the builder's list of elements.
     * The text will be displayed within the component.
     *
     * @param text The text to be added as an element in the component.
     */
    public ComponentBuilder addText(Text text) {
        elements.add(new TextElement(text));
        return this;
    }

    /**
     * Adds an {@link ItemStackElement} to the builder's list of elements.
     * The icon (ItemStack) will be displayed within the component.
     *
     * @param itemStack The item stack to be added as an element in the component.
     */
    public ComponentBuilder addIcon(ItemStack itemStack) {
        elements.add(new ItemStackElement(itemStack));
        return this;
    }
}