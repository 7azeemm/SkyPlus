package com.hazem.skyplus.config.gui;

import com.hazem.skyplus.config.controllers.Controller;
import com.hazem.skyplus.config.controllers.DropdownController;
import com.hazem.skyplus.utils.gui.Element;
import com.hazem.skyplus.utils.gui.Region;
import net.minecraft.client.gui.DrawContext;

public class View extends Element {
    private static final int LEFT_PADDING = 8;
    private static final int SCROLL_STEP = 20;
    private static final int PADDING = 4;
    private static final int LINE_OFFSET = 5;
    private final Scrollbar scrollbar;
    private Category category;
    private int maxScrollHeight;
    private double scrollPercentage;

    public View(Category category) {
        this.category = category;
        this.scrollbar = new Scrollbar();
    }

    public void addHeight(int height) {
        this.maxScrollHeight += height;
        if (scrollbar.isVisible()) scrollbar.update(maxScrollHeight, scrollPercentage);
    }

    public void init(Region element) {
        super.init(element.getX(), element.getY(), element.getWidth(), element.getHeight());

        maxScrollHeight = 0;
        for (Option option : category.getOptions()) {
            maxScrollHeight += PADDING;
            option.init(x + LEFT_PADDING, y + maxScrollHeight, width - LEFT_PADDING * 2);
            maxScrollHeight += option.getHeight();
        }

        for (SubCategory subCategory : category.getSubCategories()) {
            maxScrollHeight += PADDING * 2;
            subCategory.init(x + width / 2, y + maxScrollHeight);
            subCategory.setLinePosition(x + LINE_OFFSET, x + width - Scrollbar.SCROLLBAR_WIDTH - LINE_OFFSET);
            maxScrollHeight += this.textRenderer.fontHeight;

            for (Option option : subCategory.getOptions()) {
                maxScrollHeight += PADDING;
                option.init(x + LEFT_PADDING, y + maxScrollHeight, width - LEFT_PADDING * 2);
                maxScrollHeight += option.getHeight();
            }
        }

        scrollbar.init(x, y, width, height, maxScrollHeight, scrollPercentage);
    }

    public void onMouseScroll(double verticalAmount) {
        float scrollDelta = (float) verticalAmount * SCROLL_STEP / (maxScrollHeight - height + Scrollbar.SCROLLBAR_OFFSET * 2);
        scrollPercentage = Math.clamp(scrollPercentage - scrollDelta, 0.0f, 1.0f);
        scrollbar.update(maxScrollHeight, scrollPercentage);
    }

    public void onMouseDrag(double deltaY) {
        int scrollBarStartY = y + Scrollbar.SCROLLBAR_OFFSET;
        int scrollBarEndY = bottom - scrollbar.getHeight() - Scrollbar.SCROLLBAR_OFFSET;

        double newBarY = Math.max(scrollBarStartY, Math.min(scrollBarEndY, scrollbar.getY() + deltaY));

        double scrollRange = scrollBarEndY - scrollBarStartY;
        scrollPercentage = (newBarY - scrollBarStartY) / scrollRange;

        scrollbar.update(maxScrollHeight, scrollPercentage);
    }

    public void closeDropdowns(Controller controller) {
        for (Option option : category.getAllOptions()) {
            if (option.getController() != controller && option.getController() instanceof DropdownController dropdownController) {
                if (dropdownController.isExpanded()) {
                    dropdownController.close();
                    return;
                }
            }
        }
    }

    @Override
    public void render(DrawContext context) {
        //Components already rendered in ConfigScreen
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Scrollbar getScrollbar() {
        return scrollbar;
    }
}
