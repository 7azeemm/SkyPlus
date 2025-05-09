package com.hazem.skyplus.config.controllers;

import com.hazem.skyplus.config.ConfigScreen;
import com.hazem.skyplus.config.gui.Option;
import com.hazem.skyplus.config.gui.Selection;
import com.hazem.skyplus.utils.FormattingUtils;
import com.hazem.skyplus.utils.RenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DropdownController extends Controller {
    private final Consumer<Object> valueSetter;
    private final Enum<?> defaultValue;
    private boolean expanded = false;
    private final List<Selection> selections = new ArrayList<>();
    private Selection currentSelection;
    private int menuHeight;
    private int menuWidth;
    private Selection hoveredSelection = null;

    // Constructor for DropdownController
    public DropdownController(Consumer<Object> valueSetter, Enum<?> defaultValue, Enum<?>[] enumConstants) {
        this.valueSetter = valueSetter;
        this.defaultValue = defaultValue;
        for (Enum<?> enumConstant : enumConstants) {
            String text = FormattingUtils.formatEnumName(enumConstant);
            Selection selection = new Selection(enumConstant, text);
            if (enumConstant == defaultValue) currentSelection = selection;

            selections.add(selection);
        }
    }

    public void update() {
        initSize();
        super.init(option);
        updateSelectionMenu();
    }

    @Override
    public void initSize() {
        this.width = textRenderer.getWidth(currentSelection.getText()) + 13;
        this.height = textRenderer.fontHeight + 5;
        this.minWidth = width + PADDING * 2;
    }

    @Override
    public void init(Option option) {
        this.option = option;
        expanded = false;
        update();
    }

    public void updateSelectionMenu() {
        int yOffset = 2;
        int widestSelection = 0;
        for (Selection selection : selections) {
            if (currentSelection == selection) continue;
            int textWidth = textRenderer.getWidth(selection.getText());
            widestSelection = Math.max(textWidth, widestSelection);
        }
        widestSelection = Math.max(width - 8, widestSelection);

        for (Selection selection : selections) {
            if (currentSelection == selection) continue;
            selection.init(x + 1, bottom + yOffset, widestSelection + 6, textRenderer.fontHeight + 4);
            yOffset += textRenderer.fontHeight + 5;
        }
        menuWidth = widestSelection + 8;
        menuHeight = yOffset;
    }

    @Override
    public void render(DrawContext context) {
        // Draw the dropdown box
        context.drawBorder(x, y, width, height, 0xFF000000);
        context.drawText(textRenderer, currentSelection.getText(), x + 4, y + 3, 0xFFFFFFFF, false);

        if (!expanded) {
            drawArrowDown(context, right - 8, y + 6, 1, 0xFF05FFFF);
        } else {
            drawArrowTop(context, right - 8, y + 6, 1, 0xFF05FFFF);
            renderDropdownMenu(context);
        }
    }

    public void renderDropdownMenu(DrawContext context) {
        context.getMatrices().push();
        context.getMatrices().translate(0f, 0f, 1f);

        RenderHelper.drawBackgroundWithBorder(context, x, bottom + 1, menuWidth, menuHeight - 1, 0x80141414, 0xFF000000);

        for (Selection selection : selections) {
            if (currentSelection == selection) continue;
            selection.render(context);
        }

        context.getMatrices().pop();
    }

    public void drawArrowTop(DrawContext context, int x, int y, int size, int color) {
        context.fill(x, y + 3 * size, x + size, y + 2 * size, color);
        context.fill(x + size, y + 2 * size, x + 2 * size, y + size, color);
        context.fill(x + 2 * size, y + size, x + 3 * size, y, color);
        context.fill(x + 3 * size, y + 2 * size, x + 4 * size, y + size, color);
        context.fill(x + 4 * size, y + 3 * size, x + 5 * size, y + 2 * size, color);
    }

    public void drawArrowDown(DrawContext context, int x, int y, int size, int color) {
        context.fill(x, y, x + size, y + size, color);
        context.fill(x + size, y + size, x + 2 * size, y + 2 * size, color);
        context.fill(x + 2 * size, y + 2 * size, x + 3 * size, y + 3 * size, color);
        context.fill(x + 3 * size, y + size, x + 4 * size, y + 2 * size, color);
        context.fill(x + 4 * size, y, x + 5 * size, y + size, color);
    }

    public void close() {
        expanded = false;
        ConfigScreen.currentView.addHeight(-menuHeight + 6);
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY, float scrollOffset) {
        if (super.isHovered(mouseX, mouseY, scrollOffset)) {
            hoveredSelection = null;
            return true;
        }
        if (expanded) {
            for (Selection selection : selections) {
                if (selection.isHovered(mouseX, mouseY, scrollOffset)) {
                    if (currentSelection == selection) continue;
                    hoveredSelection = selection;
                    return true;
                }
            }
        }
        hoveredSelection = null;
        return false;
    }

    @Override
    public void hover(DrawContext context, int mouseX, int mouseY, float scrollOffset) {
        if (super.isHovered(mouseX, mouseY, scrollOffset)) {
            context.fill(x + 1, y + 1, right - 1, bottom - 1, 0x20FFFFFF);
        } else if (expanded) {
            for (Selection selection : selections) {
                if (currentSelection == selection) continue;
                if (selection.isHovered(mouseX, mouseY, scrollOffset)) {
                    context.getMatrices().push();
                    context.getMatrices().translate(0f, 0f, 1f);
                    context.fill(selection.getX(), selection.getY(), selection.getRight(),  selection.getBottom(), 0x20FFFFFF);
                    context.getMatrices().pop();
                    return;
                }
            }
        }
    }

    public void toggle() {
        expanded = !expanded;
        ConfigScreen.currentView.addHeight(expanded ? menuHeight - 6 : -menuHeight + 6);
    }

    @Override
    public void onClick() {
        toggle();
        if (hoveredSelection != null) {
            currentSelection = hoveredSelection;
            update();
            valueSetter.accept(currentSelection.getEnumConstant());
        }
    }
}
