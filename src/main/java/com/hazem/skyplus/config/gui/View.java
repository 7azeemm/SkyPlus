package com.hazem.skyplus.config.gui;

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

    public void init(Region element) {
        super.init(element.getX(), element.getY(), element.getWidth(), element.getHeight());

        maxScrollHeight = 0;
        for (Option option : category.getOptions()) {
            maxScrollHeight += PADDING;
            option.init(x + LEFT_PADDING, y + maxScrollHeight, width - LEFT_PADDING * 2);
            maxScrollHeight += option.getHeight();
        }

        for (Group group : category.getGroups()) {
            maxScrollHeight += PADDING * 2;
            group.init(x + width / 2, y + maxScrollHeight);
            group.setLinePosition(x + LINE_OFFSET, x + width - Scrollbar.SCROLLBAR_WIDTH - LINE_OFFSET);
            maxScrollHeight += this.textRenderer.fontHeight;

            for (Option option : group.getGroups()) {
                maxScrollHeight += PADDING;
                option.init(x + LEFT_PADDING, y + maxScrollHeight, width - LEFT_PADDING * 2);
                maxScrollHeight += option.getHeight();
            }
        }

        // Adjust scrollPercentage to remain valid after resizing
        if (maxScrollHeight > height) {
            scrollPercentage = Math.max(0, Math.min(scrollPercentage, 1.0));
        }

        scrollbar.init(x, y, width, height, maxScrollHeight, scrollPercentage);
    }

    public void onMouseScroll(double verticalAmount) {
        float scrollDelta = (float) verticalAmount * SCROLL_STEP / (maxScrollHeight - height);
        scrollPercentage = Math.clamp(scrollPercentage - scrollDelta, 0.0f, 1.0f);
        scrollbar.update(maxScrollHeight, scrollPercentage);
    }

    public void onMouseDrag(double deltaY) {
        double newBarY = scrollbar.getY() + deltaY;
        newBarY = Math.max(y + Scrollbar.SCROLLBAR_OFFSET, Math.min(y + height - scrollbar.getHeight() - Scrollbar.SCROLLBAR_OFFSET, newBarY));

        double scrollRange = height - scrollbar.getHeight() - Scrollbar.SCROLLBAR_OFFSET * 2;
        scrollPercentage = Math.max(0.0, Math.min(1.0, (newBarY - (y + Scrollbar.SCROLLBAR_OFFSET)) / scrollRange));

        scrollbar.update(maxScrollHeight, scrollPercentage);
    }

    @Override
    public void render(DrawContext context) {

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
