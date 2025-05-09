package com.hazem.skyplus.config;

import com.hazem.skyplus.Skyplus;
import com.hazem.skyplus.config.gui.*;
import com.hazem.skyplus.utils.ModUtils;
import com.hazem.skyplus.utils.RenderHelper;
import com.hazem.skyplus.utils.gui.Region;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {
    private static final Identifier MODRINTH_ICON = Identifier.of(Skyplus.NAMESPACE, "textures/socials/modrinth.png");
    private static final Identifier DISCORD_ICON = Identifier.of(Skyplus.NAMESPACE, "textures/socials/discord.png");
    private static final Identifier GITHUB_ICON = Identifier.of(Skyplus.NAMESPACE, "textures/socials/github.png");
    private static final float WIDTH_PERCENTAGE = 0.65f;
    private static final float HEIGHT_PERCENTAGE = 0.8f;
    private static final int TITLE_COLOR = 0xFFFFFF;
    private static final int VERSION_COLOR = 0xAAAAAA;
    private static final int BACKGROUND_COLOR = 0x90000000;
    private static final int BORDER_COLOR = 0xFF000000;
    private static final int ICON_SIZE = 16;
    private static final List<View> VIEWS = new ArrayList<>();
    private final List<SocialButton> socialButtons = new ArrayList<>();
    public static View currentView;
    private final Screen parent;
    private final List<Category> categories;
    private int x, y;
    private int guiWidth, guiHeight;
    private float titleX, titleY;
    private int versionX, versionY;
    private boolean isDragging = false;
    private Region topArea;
    private Region leftArea;
    private Region rightArea;

    protected ConfigScreen(Screen parent, List<Category> categories) {
        super(Text.of("SkyPlus Config"));
        this.parent = parent;
        this.categories = categories;

        if (VIEWS.isEmpty()) {
            VIEWS.add(new View(categories.getFirst()));
            currentView = VIEWS.getFirst();
        } else {
            for (Category category : categories) {
                VIEWS.stream()
                        .filter(view -> view.getCategory().getName().getString().equals(category.getName().getString()))
                        .findFirst().ifPresent(matchingView -> matchingView.setCategory(category));
            }
        }

        currentView.getCategory().setVisibility(true);
    }

    @Override
    protected void init() {
        // Calculate new GUI dimensions and areas
        guiWidth = (int) (this.width * WIDTH_PERCENTAGE);
        guiHeight = (int) (this.height * HEIGHT_PERCENTAGE);

        x = (this.width - guiWidth) / 2;
        y = (this.height - guiHeight) / 2;

        topArea = new Region(x, y, guiWidth, guiHeight / 8);
        leftArea = new Region(x, y + guiHeight / 8, guiWidth / 4, guiHeight - guiHeight / 8);
        rightArea = new Region(x + guiWidth / 4, y + guiHeight / 8, guiWidth - guiWidth / 4, guiHeight - guiHeight / 8);

        titleX = topArea.getX() + (float) topArea.getWidth() / 2;
        titleY = topArea.getY() + (float) topArea.getHeight() / 2 - this.textRenderer.fontHeight + 2;

        versionX = topArea.getX() + topArea.getWidth() / 2 + this.textRenderer.getWidth(this.title) + 4;
        versionY = topArea.getY() + topArea.getHeight() / 2 + this.textRenderer.fontHeight / 2;

        socialButtons.clear();
        socialButtons.add(new SocialButton(MODRINTH_ICON, ModUtils.getSocialLink("modrinth"), topArea.getRight() - (ICON_SIZE + 2), topArea.getY() + 4, ICON_SIZE, ICON_SIZE, this));
        socialButtons.add(new SocialButton(DISCORD_ICON, ModUtils.getSocialLink("discord"), topArea.getRight() - (ICON_SIZE + 2) * 2, topArea.getY() + 4, ICON_SIZE, ICON_SIZE, this));
        socialButtons.add(new SocialButton(GITHUB_ICON, ModUtils.getSocialLink("github"), topArea.getRight() - (ICON_SIZE + 2) * 3, topArea.getY() + 4, ICON_SIZE, ICON_SIZE, this));

        // Update categories and options
        int categoriesHeight = 0;
        for (Category category : categories) {
            categoriesHeight += 12;
            category.init(leftArea.getX() + leftArea.getWidth() / 2 + 1, leftArea.getY() + categoriesHeight);
        }

        currentView.init(rightArea);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Render Title
        context.getMatrices().push();
        context.getMatrices().translate(titleX, titleY, 0);
        context.getMatrices().scale(2f, 2f, 1.0f);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, 0, 0, TITLE_COLOR);
        context.getMatrices().pop();

        // Render Version
        context.drawTextWithShadow(this.textRenderer, ModUtils.getModVersion(), versionX, versionY, VERSION_COLOR);

        // Render Categories name and content of the selected one
        for (Category category : categories) {
            category.render(context);
        }
        renderCategory(context, currentView.getCategory(), mouseX, mouseY);

        // Render Scrollbar
        Scrollbar scrollbar = currentView.getScrollbar();
        if (scrollbar.isVisible()) scrollbar.render(context);
    }

    private void renderCategory(DrawContext context, Category category, int mouseX, int mouseY) {
        context.enableScissor(rightArea.getX(), rightArea.getY(), this.width, rightArea.getBottom());

        context.getMatrices().push();
        context.getMatrices().translate(0, -currentView.getScrollbar().getScrollOffset(), 0);

        // Render options within the category
        for (Option option : category.getOptions()) {
            renderOption(context, option, mouseX, mouseY);
        }

        // Render groups within the category
        for (SubCategory subCategory : category.getSubCategories()) {
            renderGroup(context, subCategory, mouseX, mouseY);
        }

        context.getMatrices().pop();
        context.disableScissor();
    }

    private void renderGroup(DrawContext context, SubCategory subCategory, int mouseX, int mouseY) {
        subCategory.render(context);

        for (Option option : subCategory.getOptions()) {
            renderOption(context, option, mouseX, mouseY);
        }
    }

    private void renderOption(DrawContext context, Option option, int mouseX, int mouseY) {
        option.render(context);
        option.getController().render(context, mouseX, mouseY, currentView.getScrollbar().getScrollOffset());
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);

        RenderHelper.drawBackground(context, topArea.getX(), topArea.getY(), topArea.getRight(), topArea.getBottom(), BACKGROUND_COLOR);
        RenderHelper.drawBackground(context, leftArea.getX(), leftArea.getY(), leftArea.getRight(), leftArea.getY() + leftArea.getHeight(), BACKGROUND_COLOR);
        RenderHelper.drawBackground(context, rightArea.getX(), rightArea.getY(), rightArea.getRight(), rightArea.getBottom(), BACKGROUND_COLOR);

        context.drawHorizontalLine(x, x + guiWidth - 1, topArea.getBottom(), BORDER_COLOR);
        context.drawVerticalLine(leftArea.getRight(), leftArea.getY(), leftArea.getBottom(), BORDER_COLOR);

        context.drawBorder(x, y, guiWidth, guiHeight, BORDER_COLOR);

        // Render icons
        for (SocialButton socialButton : socialButtons) {
            socialButton.render(context);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (currentView.getScrollbar().isVisible() && rightArea.contains(mouseX, mouseY)) {
            currentView.onMouseScroll(verticalAmount);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging) {
            currentView.onMouseDrag(deltaY);
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
        float offset = currentView.getScrollbar().getScrollOffset();

        for (Option option : currentView.getCategory().getAllOptions()) {
            if (option.getController().isHovered(mouseX, mouseY, offset)) {
                currentView.closeDropdowns(option.getController());
                option.getController().onClick();
                return true;
            }
        }

        for (Category category : categories) {
            if (category.isHovered(mouseX, mouseY)) {
                selectCategory(category);
                return true;
            }
        }

        if (currentView.getScrollbar().isVisible()) {
            if (currentView.getScrollbar().isHovered(mouseX, mouseY)) {
                currentView.closeDropdowns(null);
                isDragging = true;
                return true;
            }
        }

        for (SocialButton socialButton : socialButtons) {
            if (socialButton.isHovered(mouseX, mouseY)) {
                socialButton.onClick();
                return true;
            }
        }

        currentView.closeDropdowns(null);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void selectCategory(Category category) {
        currentView.getCategory().setVisibility(false);

        VIEWS.stream()
                .filter(c -> c.getCategory().equals(category))
                .findFirst()
                .ifPresentOrElse(
                        newCategory -> currentView = newCategory,
                        () -> {
                            VIEWS.add(new View(category));
                            currentView = VIEWS.getLast();
                        });

        currentView.getCategory().setVisibility(true);
        currentView.init(rightArea);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
        ConfigManager.saveConfig();
    }
}