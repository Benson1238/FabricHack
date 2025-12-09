package com.example.fabrichack.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public abstract class Module {
    private final String name;
    private final String description;
    private boolean enabled;

    protected Module(String name, String description) {
        this.name = name;
        this.description = description;
        this.enabled = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        boolean changed = this.enabled != enabled;
        this.enabled = enabled;
        if (changed) {
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onClientTick(MinecraftClient client) {
    }

    public Text getDisplayText() {
        return Text.literal(name);
    }
}
