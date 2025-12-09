package com.example.fabrichack.config;

import com.example.fabrichack.hud.HudElement;
import com.example.fabrichack.hud.HudPosition;
import com.example.fabrichack.module.Module;
import com.example.fabrichack.module.ModuleManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final long SAVE_INTERVAL_MS = 2_000L;
    private final ModuleManager moduleManager;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path configPath;
    private long lastSave = 0L;

    public ConfigManager(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
        this.configPath = MinecraftClient.getInstance().runDirectory.toPath()
                .resolve("config").resolve("fabrichack.json");
    }

    public void load() {
        if (!Files.exists(configPath)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(configPath)) {
            Type type = new TypeToken<Map<String, ModuleConfig>>() {}.getType();
            Map<String, ModuleConfig> config = gson.fromJson(reader, type);
            if (config == null) return;

            for (Module module : moduleManager.getModules()) {
                ModuleConfig moduleConfig = config.get(module.getName());
                if (moduleConfig == null) continue;
                module.setEnabled(moduleConfig.enabled);

                if (module instanceof HudElement hud) {
                    if (moduleConfig.position != null) {
                        hud.setPosition(new HudPosition(moduleConfig.position.x, moduleConfig.position.y));
                    }
                    if (moduleConfig.color != null) {
                        hud.setColor(moduleConfig.color);
                    }
                    if (moduleConfig.scale != null) {
                        hud.setScale(moduleConfig.scale);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDeferred() {
        long now = System.currentTimeMillis();
        if (now - lastSave < SAVE_INTERVAL_MS) {
            return;
        }
        save();
    }

    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            Map<String, ModuleConfig> config = new HashMap<>();
            for (Module module : moduleManager.getModules()) {
                ModuleConfig moduleConfig = new ModuleConfig();
                moduleConfig.enabled = module.isEnabled();
                if (module instanceof HudElement hud) {
                    HudPosition position = hud.getPosition();
                    moduleConfig.position = new PositionData(position.getX(), position.getY());
                    moduleConfig.color = hud.getColor();
                    moduleConfig.scale = hud.getScale();
                }
                config.put(module.getName(), moduleConfig);
            }

            try (Writer writer = Files.newBufferedWriter(configPath)) {
                gson.toJson(config, writer);
            }
            lastSave = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ModuleConfig {
        boolean enabled;
        PositionData position;
        Integer color;
        Float scale;
    }

    private static class PositionData {
        int x;
        int y;

        PositionData(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
