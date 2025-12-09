package com.example.fabrichack.gui;

import com.example.fabrichack.hud.HudElement;
import com.example.fabrichack.module.Module;
import com.example.fabrichack.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private final ModuleManager moduleManager;
    private final List<Integer> palette = Arrays.asList(
            0xFFFFFFFF, 0xFF00FFAA, 0xFFFFAA00, 0xFF55FFFF, 0xFF55FF55, 0xFFFF5555
    );

    public ClickGuiScreen(ModuleManager moduleManager) {
        super(Text.literal("FabricHack GUI"));
        this.moduleManager = moduleManager;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.width / 2 - 90;
        int y = 30;
        int spacing = 24;

        for (Module module : moduleManager.getModules()) {
            ButtonWidget toggle = ButtonWidget.builder(Text.literal(labelFor(module)), button -> {
                module.toggle();
                button.setMessage(Text.literal(labelFor(module)));
            }).dimensions(x, y, 180, 20).build();
            addDrawableChild(toggle);
            y += spacing;

            if (module instanceof HudElement hud) {
                int colorIndex = palette.indexOf(hud.getColor());
                if (colorIndex < 0) colorIndex = 0;
                CyclingButtonWidget<Integer> colorButton = CyclingButtonWidget.builder(value -> Text.literal("Farbe"))
                        .values(palette)
                        .initially(palette.get(colorIndex))
                        .build(x, y, 88, 20, Text.literal("Farbe"), (button, value) -> hud.setColor(value));
                addDrawableChild(colorButton);

                ButtonWidget scaleDown = ButtonWidget.builder(Text.literal("-"), b -> hud.setScale(hud.getScale() - 0.1f))
                        .dimensions(x + 92, y, 40, 20).build();
                ButtonWidget scaleUp = ButtonWidget.builder(Text.literal("+"), b -> hud.setScale(hud.getScale() + 0.1f))
                        .dimensions(x + 134, y, 46, 20).build();
                addDrawableChild(scaleDown);
                addDrawableChild(scaleUp);
                y += spacing;
            }
        }
    }

    private String labelFor(Module module) {
        return module.getName() + " [" + (module.isEnabled() ? "ON" : "OFF") + "]";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, "FabricHack ClickGUI", width / 2, 10, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
