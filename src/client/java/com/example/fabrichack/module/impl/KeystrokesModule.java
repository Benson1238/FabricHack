package com.example.fabrichack.module.impl;

import com.example.fabrichack.hud.HudElement;
import com.example.fabrichack.hud.HudPosition;
import com.example.fabrichack.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.GameOptions;

import java.util.Arrays;
import java.util.List;

public class KeystrokesModule extends Module implements HudElement {
    private HudPosition position = new HudPosition(10, 10);
    private int color = 0xFFFFFFFF;
    private float scale = 1.0f;

    private final List<KeyInfo> keys = Arrays.asList(
            new KeyInfo("W", KeyAccessor::forwardKey, 1, 0, 1, 1),
            new KeyInfo("A", KeyAccessor::leftKey, 0, 1, 1, 1),
            new KeyInfo("S", KeyAccessor::backKey, 1, 1, 1, 1),
            new KeyInfo("D", KeyAccessor::rightKey, 2, 1, 1, 1),
            new KeyInfo("SPACE", KeyAccessor::jumpKey, 0, 2, 3, 1),
            new KeyInfo("SHIFT", KeyAccessor::sneakKey, 0, 3, 3, 1)
    );

    public KeystrokesModule() {
        super("Keystrokes", "Zeigt gedrückte Steuerungstasten an.");
        setEnabled(true);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        GameOptions options = client.options;

        int boxSize = (int) (18 * scale);
        for (KeyInfo info : keys) {
            boolean pressed = info.pressed(options);
            int x = position.getX() + info.x * boxSize;
            int y = position.getY() + info.y * boxSize;
            int width = info.width * boxSize;
            int height = info.height * boxSize;
            int fillColor = pressed ? 0xAA00FFAA : 0x55000000;
            context.fill(x, y, x + width, y + height, fillColor);
            int textWidth = context.getTextRenderer().getWidth(info.label);
            int textX = x + (width - textWidth) / 2;
            int textY = y + (height - context.getTextRenderer().fontHeight) / 2;
            context.drawText(context.getTextRenderer(), info.label, textX, textY, color, true);
        }
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
        this.scale = Math.max(0.5f, Math.min(scale, 3.0f));
    }

    @Override
    public int getWidth() {
        return (int) (3 * 18 * scale);
    }

    @Override
    public int getHeight() {
        return (int) (4 * 18 * scale);
    }

    private static class KeyInfo {
        private final String label;
        private final KeyAccessor accessor;
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private KeyInfo(String label, KeyAccessor accessor, int x, int y, int width, int height) {
            this.label = label;
            this.accessor = accessor;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean pressed(GameOptions options) {
            return accessor.access(options).isPressed();
        }
    }

    @FunctionalInterface
    private interface KeyAccessor {
        net.minecraft.client.option.KeyBinding access(GameOptions options);

        static net.minecraft.client.option.KeyBinding forwardKey(GameOptions options) { return options.forwardKey; }
        static net.minecraft.client.option.KeyBinding backKey(GameOptions options) { return options.backKey; }
        static net.minecraft.client.option.KeyBinding leftKey(GameOptions options) { return options.leftKey; }
        static net.minecraft.client.option.KeyBinding rightKey(GameOptions options) { return options.rightKey; }
        static net.minecraft.client.option.KeyBinding jumpKey(GameOptions options) { return options.jumpKey; }
        static net.minecraft.client.option.KeyBinding sneakKey(GameOptions options) { return options.sneakKey; }
    }
}
