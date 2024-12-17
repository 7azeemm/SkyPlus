package com.hazem.skyplus.utils.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;

public final class EmptyScreen extends Screen {
    private final ClientConnection connection;

    public EmptyScreen() {
        super(Text.translatable("connect.joining"));
        this.connection = null;
    }

    public EmptyScreen(ClientConnection connection) {
        super(Text.translatable("connect.reconfiguring"));
        this.connection = connection;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void tick() {
        if (this.connection == null) return;
        if (this.connection.isOpen()) {
            this.connection.tick();
        } else {
            this.connection.handleDisconnection();
        }
    }
}
