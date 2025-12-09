package com.example.fabrichack.hud;

import net.minecraft.client.gui.DrawContext;

public interface HudElement {
    String getId();

    void render(DrawContext context, float tickDelta);

    HudPosition getPosition();

    void setPosition(HudPosition position);

    int getColor();

    void setColor(int color);

    float getScale();

    void setScale(float scale);

    int getWidth();

    int getHeight();
}
