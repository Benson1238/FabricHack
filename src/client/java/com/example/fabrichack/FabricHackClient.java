package com.example.fabrichack;

import com.example.fabrichack.config.ConfigManager;
import com.example.fabrichack.gui.ClickGuiScreen;
import com.example.fabrichack.gui.HudEditorScreen;
import com.example.fabrichack.hud.HudRenderer;
import com.example.fabrichack.module.ModuleManager;
import com.example.fabrichack.module.impl.BetterThirdPersonModule;
import com.example.fabrichack.module.impl.FpsDisplayModule;
import com.example.fabrichack.module.impl.KeystrokesModule;
import com.example.fabrichack.module.impl.PingDisplayModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class FabricHackClient implements ClientModInitializer {
    public static final String MOD_ID = "fabrichack";

    private static FabricHackClient instance;

    private final ModuleManager moduleManager = new ModuleManager();
    private final ConfigManager configManager = new ConfigManager(moduleManager);
    private HudRenderer hudRenderer;

    private KeyBinding openGuiKey;
    private KeyBinding openHudEditorKey;
    private KeyBinding freelookKey;

    public static FabricHackClient getInstance() {
        return instance;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onInitializeClient() {
        instance = this;

        registerModules();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fabrichack.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.fabrichack"
        ));

        openHudEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fabrichack.hud_editor",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_CONTROL,
                "category.fabrichack"
        ));

        freelookKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fabrichack.freelook",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "category.fabrichack"
        ));

        hudRenderer = new HudRenderer(moduleManager, configManager);
        HudRenderCallback.EVENT.register(hudRenderer::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                client.setScreen(new ClickGuiScreen(moduleManager));
            }
            while (openHudEditorKey.wasPressed()) {
                client.setScreen(new HudEditorScreen(moduleManager, configManager));
            }

            moduleManager.getModules().forEach(module -> module.onClientTick(client));

            BetterThirdPersonModule freelook = moduleManager.getModule(BetterThirdPersonModule.class);
            if (freelook != null) {
                freelook.updateFreelook(MinecraftClient.getInstance(), freelookKey);
            }
        });

        configManager.load();
    }

    private void registerModules() {
        moduleManager.register(new KeystrokesModule());
        moduleManager.register(new PingDisplayModule());
        moduleManager.register(new FpsDisplayModule());
        moduleManager.register(new BetterThirdPersonModule());
    }
}
