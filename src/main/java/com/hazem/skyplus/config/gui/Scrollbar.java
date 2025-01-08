package com.hazem.skyplus.config.gui;

import com.hazem.skyplus.utils.RenderHelper;
import com.hazem.skyplus.utils.gui.Element;
import net.minecraft.client.gui.DrawContext;

public class Scrollbar extends Element {
    private static final int BACKGROUND_COLOR = 0x80000000;
    private static final int BORDER_COLOR = 0xFF000000;
    private static final int FILL_COLOR = 0xFFAAAAAA;
    protected static final int SCROLLBAR_WIDTH = 3;
    protected static final int SCROLLBAR_OFFSET = 4;
    private int startY, endY;
    private int visibleHeight;
    private float scrollOffset = 0f;
    private boolean visible = false;

    public void init(int x, int y, int width, int height, int maxScrollHeight, double scrollPercentage) {
        startY = y + SCROLLBAR_OFFSET;
        endY = y + height - SCROLLBAR_OFFSET;
        visibleHeight = height - SCROLLBAR_OFFSET * 2;

        // Update only if there's content to scroll
        visible = maxScrollHeight - visibleHeight > 0;

        if (visible) {
            this.x = x + width - SCROLLBAR_WIDTH - 2;
            this.width = SCROLLBAR_WIDTH;
            update(maxScrollHeight, scrollPercentage);
        }
    }

    public void update(int maxScrollHeight, double scrollPercentage) {
        this.height = Math.max(10, (int) ((float) visibleHeight / maxScrollHeight * visibleHeight));
        this.y = startY + (int) (scrollPercentage * (visibleHeight - this.height));
        this.scrollOffset = (int) (scrollPercentage * (maxScrollHeight - visibleHeight));
    }

    @Override
    public void render(DrawContext context) {
        RenderHelper.drawBackgroundWithBorder(context, x - 1, startY - 1, x + SCROLLBAR_WIDTH, endY, BACKGROUND_COLOR, BORDER_COLOR);
        context.fill(x, y, x + SCROLLBAR_WIDTH, y + height, FILL_COLOR);
    }

    public float getScrollOffset() {
        return scrollOffset;
    }

    public boolean isVisible() {
        return visible;
    }
}
