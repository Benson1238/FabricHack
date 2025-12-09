package com.example.fabrichack.module.impl;

import com.example.fabrichack.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class BetterThirdPersonModule extends Module {
    private Float lockedYaw;
    private Float lockedPitch;

    public BetterThirdPersonModule() {
        super("Better Third Person", "Ermöglicht eine freiere Third-Person-Kamera.");
    }

    @Override
    public void onDisable() {
        lockedYaw = null;
        lockedPitch = null;
    }

    public void updateFreelook(MinecraftClient client, KeyBinding freelookKey) {
        if (!isEnabled() || client.player == null) {
            return;
        }

        if (!freelookKey.isPressed()) {
            lockedYaw = null;
            lockedPitch = null;
            return;
        }

        if (lockedYaw == null || lockedPitch == null) {
            lockedYaw = client.player.getYaw();
            lockedPitch = client.player.getPitch();
        }

        // Keep the player facing direction stable while the camera can be rotated by the user.
        client.player.setYaw(lockedYaw);
        client.player.setHeadYaw(lockedYaw);
        client.player.setPitch(Math.max(-90.0f, Math.min(90.0f, lockedPitch)));
    }
}
