package com.hazem.skyplus.config.gui;

import com.hazem.skyplus.utils.gui.ClickableElement;
import net.minecraft.client.gui.DrawContext;

public class Selection extends ClickableElement {
    private static final int TEXT_COLOR = 0xFFC0C0C0;
    private final String text;
    private final Enum<?> enumConstant;
    private boolean selected = false;

    public Selection(Enum<?> enumConstant, String text) {
        this.enumConstant = enumConstant;
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getText() {
        return text;
    }

    public void setSelection(boolean selected){
        this.selected = selected;
    }

    public Enum<?> getEnumConstant() {
        return enumConstant;
    }

    @Override
    public void onClick() {

    }

    @Override
    public void render(DrawContext context) {
        context.drawText(textRenderer, text, x + 3, y + 3, TEXT_COLOR, false);
    }
}
