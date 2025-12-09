package com.example.fabrichack.gui;

import com.example.fabrichack.config.ConfigManager;
import com.example.fabrichack.hud.HudElement;
import com.example.fabrichack.hud.HudPosition;
import com.example.fabrichack.module.Module;
import com.example.fabrichack.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HudEditorScreen extends Screen {
    private final ModuleManager moduleManager;
    private final ConfigManager configManager;

    private HudElement dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    public HudEditorScreen(ModuleManager moduleManager, ConfigManager configManager) {
        super(Text.literal("HUD Editor"));
        this.moduleManager = moduleManager;
        this.configManager = configManager;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, "HUD Editor - Ziehe Module, ESC zum Schließen", width / 2, 10, 0xFFFFFF);

        for (Module module : moduleManager.getModules()) {
            if (module instanceof HudElement hud && module.isEnabled()) {
                hud.render(context, delta);
                HudPosition pos = hud.getPosition();
                int x = pos.getX();
                int y = pos.getY();
                context.drawBorder(x - 2, y - 2, hud.getWidth() + 4, hud.getHeight() + 4, 0x88FFFFFF);
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Module module : moduleManager.getModules()) {
            if (module instanceof HudElement hud && module.isEnabled()) {
                HudPosition pos = hud.getPosition();
                int x = pos.getX();
                int y = pos.getY();
                if (mouseX >= x && mouseX <= x + hud.getWidth() && mouseY >= y && mouseY <= y + hud.getHeight()) {
                    dragging = hud;
                    dragOffsetX = (int) mouseX - x;
                    dragOffsetY = (int) mouseY - y;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging != null) {
            dragging.setPosition(new HudPosition((int) mouseX - dragOffsetX, (int) mouseY - dragOffsetY));
            configManager.saveDeferred();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (dragging != null) {
            dragging = null;
            configManager.save();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
