package com.example.fabrichack.module.impl;

import com.example.fabrichack.hud.HudElement;
import com.example.fabrichack.hud.HudPosition;
import com.example.fabrichack.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public class PingDisplayModule extends Module implements HudElement {
    private HudPosition position = new HudPosition(10, 90);
    private int color = 0xFFFFFFFF;
    private float scale = 1.0f;

    public PingDisplayModule() {
        super("Ping", "Zeigt die aktuelle Latenz an.");
        setEnabled(true);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getNetworkHandler() == null) {
            return;
        }
        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        int ping = entry != null ? entry.getLatency() : -1;
        String text = "Ping: " + (ping < 0 ? "--" : ping + "ms");
        context.drawText(context.getTextRenderer(), text, position.getX(), position.getY(), color, true);
    }

    @Override
    public HudPosition getPosition() {
        return position;
    }

    @Override
    public void setPosition(HudPosition position) {
        this.position = position;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public int getWidth() {
        return (int) (contextWidth("Ping: 000ms") * scale);
    }

    @Override
    public int getHeight() {
        return (int) (10 * scale) + 10;
    }

    private int contextWidth(String text) {
        return MinecraftClient.getInstance().textRenderer.getWidth(text);
    }
}
