package com.hazem.skyplus.config.gui;

import com.hazem.skyplus.utils.gui.ClickableElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class SocialButton extends ClickableElement {
    private final Identifier texture;
    private final String url;
    private final Screen parentScreen;

    public SocialButton(Identifier texture, String url, int x, int y, int width, int height, Screen parentScreen) {
        super.init(x, y, width, height);
        this.texture = texture;
        this.url = url;
        this.parentScreen = parentScreen;
    }

    @Override
    public void render(DrawContext context) {
        context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, 0, 0, width, height, width, height);
    }

    @Override
    public void onClick() {
        ConfirmLinkScreen.open(parentScreen, url);
    }
}
