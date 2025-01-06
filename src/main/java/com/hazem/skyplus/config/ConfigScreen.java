package com.hazem.skyplus.config;

import com.hazem.skyplus.Skyplus;
import com.hazem.skyplus.config.gui.*;
import com.hazem.skyplus.utils.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {
    private static final Pair<Identifier, String> MODRINTH = new Pair<>(Identifier.of(Skyplus.NAMESPACE, "textures/socials/modrinth.png"), "");
    private static final Pair<Identifier, String> DISCORD = new Pair<>(Identifier.of(Skyplus.NAMESPACE, "textures/socials/discord.png"), "");
    private static final Pair<Identifier, String> GITHUB = new Pair<>(Identifier.of(Skyplus.NAMESPACE, "textures/socials/github.png"), "");
    private static final float WIDTH_PERCENTAGE = 0.65f;
    private static final float HEIGHT_PERCENTAGE = 0.8f;
    private static final int BACKGROUND_COLOR = 0x90000000;
    private static final int BORDER_COLOR = 0xFF000000;
    private static final int LEFT_PADDING = 8;
    private static final int PADDING = 4;
    private static final int SCROLL_STEP = 20;
    private final int SCROLLBAR_WIDTH = 3;
    private final int SCROLLBAR_OFFSET = 4;
    private static int selectedCategory = 0;
    private final List<SocialButton> socialButtons = new ArrayList<>();
    private final Screen parent;
    private final List<Category> categories;
    private int x, y;
    private int guiWidth, guiHeight;
    private GuiArea topArea;
    private GuiArea leftArea;
    private GuiArea rightArea;
    private float titleX, titleY;
    private int versionX, versionY;
    private float scrollOffset = 0;
    private double scrollPercentage = 0.0;
    private int maxScrollHeight;
    private int barX, barY, barHeight;
    private boolean scrollbarVisible = false;
    private boolean isDragging = false;
    //CHECK NAME CONVERSTIONS

    protected ConfigScreen(Screen parent, List<Category> categories) {
        super(Text.of("SkyPlus Config"));
        this.parent = parent;
        this.categories = categories;
    }

    @Override
    protected void init() {
        // Calculate new GUI dimensions and areas
        guiWidth = (int) (this.width * WIDTH_PERCENTAGE);
        guiHeight = (int) (this.height * HEIGHT_PERCENTAGE);

        x = (this.width - guiWidth) / 2;
        y = (this.height - guiHeight) / 2;

        topArea = new GuiArea(x, y, guiWidth, guiHeight / 8);
        leftArea = new GuiArea(x, y + guiHeight / 8, guiWidth / 4, guiHeight - guiHeight / 8);
        rightArea = new GuiArea(x + guiWidth / 4, y + guiHeight / 8, guiWidth - guiWidth / 4, guiHeight - guiHeight / 8);

        titleX = topArea.getX() + (float) topArea.getWidth() / 2;
        titleY = topArea.getY() + (float) topArea.getHeight() / 2 - this.textRenderer.fontHeight + 2;

        versionX = topArea.getX() + topArea.getWidth() / 2 + this.textRenderer.getWidth(this.title) + 4;
        versionY = topArea.getY() + topArea.getHeight() / 2 + this.textRenderer.fontHeight / 2;

        socialButtons.clear();
        socialButtons.add(new SocialButton(MODRINTH, topArea.getRight() - 18, topArea.getY() + 4, 16, 16));
        socialButtons.add(new SocialButton(DISCORD, topArea.getRight() - 18 * 2, topArea.getY() + 4, 16, 16));
        socialButtons.add(new SocialButton(GITHUB, topArea.getRight() - 18 * 3, topArea.getY() + 4, 16, 16));

        // Update categories and options
        int categoriesHeight = 0;
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            categoriesHeight += 12;
            category.setTitlePosition(leftArea.getX() + leftArea.getWidth() / 2 + 1, leftArea.getY() + categoriesHeight);
            if (selectedCategory == i) initCategory(category);
        }
    }

    private void initCategory(Category category) {
        int height = 0;
        for (Option option : category.getOptions()) {
            height += PADDING;
            option.setPosition(rightArea.getX() + LEFT_PADDING, rightArea.getY() + height, rightArea.getWidth() - LEFT_PADDING * 2);
            height += option.getHeight();
        }

        for (Group group : category.getGroups()) {
            height += PADDING * 2;
            group.setTitlePosition(rightArea.getX() + rightArea.getWidth() / 2, rightArea.getY() + height, rightArea.getX() + 5, rightArea.getRight() - SCROLLBAR_WIDTH - 5);
            height += this.textRenderer.fontHeight;
            for (Option option : group.getGroups()) {
                height += PADDING;
                option.setPosition(rightArea.getX() + LEFT_PADDING, rightArea.getY() + height, rightArea.getWidth() - LEFT_PADDING * 2);
                height += option.getHeight();
            }
        }

        maxScrollHeight = height;

        // Adjust scrollPercentage to remain valid after resizing
        if (maxScrollHeight > rightArea.getHeight()) {
            scrollPercentage = Math.max(0, Math.min(scrollPercentage, 1.0));
        }

        updateScrollbarProperties();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Render Title
        context.getMatrices().push();
        context.getMatrices().translate(titleX, titleY, 0);
        context.getMatrices().scale(2f, 2f, 1.0f);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, 0, 0, 0xFFFFFF);
        context.getMatrices().pop();

        // Render Version
        context.drawTextWithShadow(this.textRenderer, Skyplus.getModVersion(), versionX, versionY, 0xAAAAAA);

        // Render Categories name and content of the selected one
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);

            int color = i == selectedCategory ? 0xFFFFFF : 0xAAAAAA;
            Text categoryName = i == selectedCategory ? category.getName().copyContentOnly().formatted(Formatting.UNDERLINE) : category.getName();

            context.drawCenteredTextWithShadow(this.textRenderer, categoryName, category.getTitleX(), category.getTitleY(), color);
            if (i == selectedCategory) renderCategory(context, category, mouseX, mouseY);
        }

        // Render Scrollbar
        if (scrollbarVisible) {
            RenderHelper.drawBackgroundWithBorder(context, barX - 1, rightArea.getY() + 3, barX + SCROLLBAR_WIDTH, rightArea.getBottom() - 4, 0x80000000, 0xFF000000);
            context.fill(barX, barY, barX + SCROLLBAR_WIDTH, barY + barHeight, 0xFFAAAAAA);
        }
    }

    private void renderCategory(DrawContext context, Category category, int mouseX, int mouseY) {
        context.enableScissor(rightArea.getX(), rightArea.getY(), rightArea.getRight(), rightArea.getBottom());

        context.getMatrices().push();
        context.getMatrices().translate(0, -scrollOffset, 0);

        // Render options within the category
        for (Option option : category.getOptions()) {
            renderOption(context, option, mouseX, mouseY);
        }

        // Render sections within the category
        for (Group group : category.getGroups()) {
            renderGroup(context, group, mouseX, mouseY);
        }

        context.getMatrices().pop();
        context.disableScissor();
    }

    private void renderGroup(DrawContext context, Group group, int mouseX, int mouseY) {
        context.drawCenteredTextWithShadow(this.textRenderer, group.getName(), group.getTitleX(), group.getTitleY(), 0xAAAAAA);
        context.drawHorizontalLine(group.getLeft(), group.getTitleX() - group.getTitleWidth() / 2 - 5, group.getTitleY() + this.textRenderer.fontHeight / 2, 0xFFAAAAAA);
        context.drawHorizontalLine(group.getTitleX() + group.getTitleWidth() / 2 + 3, group.getRight(), group.getTitleY() + this.textRenderer.fontHeight / 2, 0xFFAAAAAA);

        for (Option option : group.getGroups()) {
            renderOption(context, option, mouseX, mouseY);
        }
    }

    private void renderOption(DrawContext context, Option option, int mouseX, int mouseY) {
        RenderHelper.drawBackgroundWithBorder(context, option.getX(), option.getY(), option.getRight(), option.getBottom(), 0x80141414, 0xFF000000);
        context.drawWrappedText(this.textRenderer, option.getName(), option.getNameX(), option.getNameY(), option.getMaxWidth(), 0xFFFFFF, false);
        if (option.hasDescription()) context.drawWrappedTextWithShadow(this.textRenderer, option.getDescription(), option.getDescriptionX(), option.getDescriptionY(), option.getMaxWidth(), 0x737373);

        option.getController().render(context, option, scrollOffset, mouseX, mouseY);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);

        RenderHelper.drawBackground(context, topArea.getX(), topArea.getY(), topArea.getRight(), topArea.getBottom(), BACKGROUND_COLOR);
        RenderHelper.drawBackground(context, leftArea.getX(), leftArea.getY(), leftArea.getRight(), leftArea.getY() + leftArea.getHeight(), BACKGROUND_COLOR);
        RenderHelper.drawBackground(context, rightArea.getX(), rightArea.getY(), rightArea.getRight(), rightArea.getBottom(), BACKGROUND_COLOR);

        context.drawHorizontalLine(x, x + guiWidth, topArea.getBottom(), BORDER_COLOR);
        context.drawVerticalLine(leftArea.getRight(), leftArea.getY() - 1, leftArea.getBottom(), BORDER_COLOR);

        RenderHelper.drawBorder(context, x, y, x + guiWidth, y + guiHeight, BORDER_COLOR);

        // Render icons
        for (SocialButton socialButton : socialButtons) {
            socialButton.render(context);
        }
    }

    private void updateScrollbarProperties() {
        int visibleHeight = rightArea.getHeight() - SCROLLBAR_OFFSET * 2;
        int scrollableHeight = Math.max(maxScrollHeight - visibleHeight, 0);

        // Update only if there's content to scroll
        scrollbarVisible = scrollableHeight > 0;
        if (scrollbarVisible) {
            barHeight = Math.max(10, (int) ((float) visibleHeight / maxScrollHeight * visibleHeight));
            barY = rightArea.getY() + (int) (scrollPercentage * (visibleHeight - barHeight)) + SCROLLBAR_OFFSET;
            barX = rightArea.getRight() - SCROLLBAR_WIDTH - 2;
            scrollOffset = (int) (scrollPercentage * (maxScrollHeight - rightArea.getHeight()));
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (rightArea.contains(mouseX, mouseY)) {
            float scrollDelta = (float) verticalAmount * SCROLL_STEP / (maxScrollHeight - rightArea.getHeight());
            scrollPercentage = Math.clamp(scrollPercentage - scrollDelta, 0.0f, 1.0f);
            updateScrollbarProperties();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging) {
            double newBarY = barY + deltaY;
            newBarY = Math.max(rightArea.getY() + SCROLLBAR_OFFSET, Math.min(rightArea.getBottom() - barHeight - SCROLLBAR_OFFSET, newBarY));

            double scrollRange = rightArea.getHeight() - barHeight - SCROLLBAR_OFFSET * 2;
            scrollPercentage = (newBarY - (rightArea.getY() + SCROLLBAR_OFFSET)) / scrollRange;
            scrollPercentage = Math.max(0.0, Math.min(1.0, scrollPercentage));

            updateScrollbarProperties();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDragging) {
            isDragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Category focusedCategory = categories.get(selectedCategory);

        for (Option option : focusedCategory.getOptions()) {
            if (option.getController().buttonClicked(mouseX, mouseY, scrollOffset)) return true;
        }
        for (Group group : focusedCategory.getGroups()) {
            for (Option option : group.getGroups()) {
                if (option.getController().buttonClicked(mouseX, mouseY, scrollOffset)) return true;
            }
        }

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);

            int textWidth = this.textRenderer.getWidth(category.getName()) + 1;
            int textHeight = this.textRenderer.fontHeight + 1;
            int textX = category.getTitleX() - textWidth / 2 - 1;
            int textY = category.getTitleY() - 1;

            if (mouseX >= textX && mouseX <= textX + textWidth && mouseY >= textY && mouseY <= textY + textHeight) {
                selectedCategory = i;
                scrollPercentage = 0;
                scrollOffset = 0;
                initCategory(category);
                return true;
            }
        }

        if (scrollbarVisible) {
            if (mouseX >= barX && mouseX <= barX + SCROLLBAR_WIDTH && mouseY >= barY && mouseY <= barY + barHeight) {
                isDragging = true;
                return true;
            }
        }

        for (SocialButton socialButton : socialButtons) {
            if (socialButton.buttonClicked(mouseX, mouseY, scrollOffset)) {
                socialButton.onClick(this);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
        ConfigManager.saveConfig();
    }
}