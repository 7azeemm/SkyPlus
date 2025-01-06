package com.hazem.skyplus.config.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class SocialButton {
    private final Identifier texture;
    private final String link;
    private final int x, y;
    private final int width, height;

    public SocialButton(Pair<Identifier, String> social, int x, int y, int width, int height) {
        this.texture = social.getLeft();
        this.link = social.getRight();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(DrawContext context) {
        context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, 0, 0, width, height, width, height);
    }

    public boolean buttonClicked(double mouseX, double mouseY, float scrollOffset) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y - scrollOffset && mouseY <= y + height - scrollOffset;
    }

    public void onClick(Screen screen) {
        ConfirmLinkScreen.open(screen, link);
    }
}
