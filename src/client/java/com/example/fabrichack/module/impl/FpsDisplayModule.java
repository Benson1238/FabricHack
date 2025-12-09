package com.example.fabrichack.module.impl;

import com.example.fabrichack.hud.HudElement;
import com.example.fabrichack.hud.HudPosition;
import com.example.fabrichack.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FpsDisplayModule extends Module implements HudElement {
    private HudPosition position = new HudPosition(10, 110);
    private int color = 0xFFFFFFFF;
    private float scale = 1.0f;

    public FpsDisplayModule() {
        super("FPS", "Zeigt die aktuellen FPS an.");
        setEnabled(true);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        int fps = MinecraftClient.getInstance().getCurrentFps();
        String text = "FPS: " + fps;
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
        return (int) (MinecraftClient.getInstance().textRenderer.getWidth("FPS: 0000") * scale);
    }

    @Override
    public int getHeight() {
        return (int) (10 * scale) + 10;
    }
}
