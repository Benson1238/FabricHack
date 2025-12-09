package com.example.fabrichack.hud;

import com.example.fabrichack.config.ConfigManager;
import com.example.fabrichack.module.ModuleManager;
import com.example.fabrichack.module.impl.FpsDisplayModule;
import com.example.fabrichack.module.impl.KeystrokesModule;
import com.example.fabrichack.module.impl.PingDisplayModule;
import net.minecraft.client.gui.DrawContext;

public class HudRenderer {
    private final ModuleManager moduleManager;
    private final ConfigManager configManager;

    public HudRenderer(ModuleManager moduleManager, ConfigManager configManager) {
        this.moduleManager = moduleManager;
        this.configManager = configManager;
    }

    public void render(DrawContext context, float tickDelta) {
        if (moduleManager.getModule(KeystrokesModule.class) != null) {
            KeystrokesModule module = moduleManager.getModule(KeystrokesModule.class);
            if (module != null && module.isEnabled()) {
                module.render(context, tickDelta);
            }
        }

        PingDisplayModule pingDisplay = moduleManager.getModule(PingDisplayModule.class);
        if (pingDisplay != null && pingDisplay.isEnabled()) {
            pingDisplay.render(context, tickDelta);
        }

        FpsDisplayModule fpsDisplay = moduleManager.getModule(FpsDisplayModule.class);
        if (fpsDisplay != null && fpsDisplay.isEnabled()) {
            fpsDisplay.render(context, tickDelta);
        }

        configManager.saveDeferred();
    }
}
