package com.github.smallinger.coppergolemlegacy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Configuration for Copper Golem Legacy mod
 */
public class CopperGolemLegacyConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue GOLEM_PRESSES_BUTTONS;

    static {
        BUILDER.push("coppergolemai");

        GOLEM_PRESSES_BUTTONS = BUILDER
            .comment("Enable/Disable Copper Golem randomly pressing copper buttons")
            .define("golemPressesButtons", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    
    public static Screen createConfigScreen(Screen parent) {
        return new CopperGolemConfigScreen(parent);
    }
    
    /**
     * Simple config screen for Copper Golem Legacy
     */
    public static class CopperGolemConfigScreen extends Screen {
        private final Screen parent;
        private Button buttonPressToggle;
        
        public CopperGolemConfigScreen(Screen parent) {
            super(Component.translatable("coppergolemlegacy.config.title"));
            this.parent = parent;
        }
        
        @Override
        protected void init() {
            super.init();
            
            int buttonWidth = 200;
            int buttonHeight = 20;
            int centerX = this.width / 2;
            int startY = this.height / 4;
            
            // Title: Copper Golem Legacy Configuration
            // (rendered in render method)
            
            // Toggle Button: Golem Presses Buttons
            this.buttonPressToggle = Button.builder(
                getToggleButtonText(),
                button -> {
                    boolean newValue = !GOLEM_PRESSES_BUTTONS.get();
                    GOLEM_PRESSES_BUTTONS.set(newValue);
                    button.setMessage(getToggleButtonText());
                    SPEC.save();
                }
            )
            .bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight)
            .tooltip(Tooltip.create(Component.translatable("coppergolemlegacy.config.golemPressesButtons.tooltip")))
            .build();
            
            this.addRenderableWidget(this.buttonPressToggle);
            
            // Done Button
            this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_DONE,
                button -> this.minecraft.setScreen(this.parent)
            )
            .bounds(centerX - buttonWidth / 2, this.height - 30, buttonWidth, buttonHeight)
            .build());
        }
        
        private Component getToggleButtonText() {
            boolean enabled = GOLEM_PRESSES_BUTTONS.get();
            return Component.translatable("coppergolemlegacy.config.golemPressesButtons")
                .append(": ")
                .append(enabled ? 
                    Component.translatable("coppergolemlegacy.config.enabled").withStyle(style -> style.withColor(0x55FF55)) : 
                    Component.translatable("coppergolemlegacy.config.disabled").withStyle(style -> style.withColor(0xFF5555))
                );
        }
        
        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            this.renderBackground(guiGraphics);
            guiGraphics.drawCenteredString(
                this.font, 
                this.title, 
                this.width / 2, 
                20, 
                0xFFFFFF
            );
            super.render(guiGraphics, mouseX, mouseY, partialTick);
        }
        
        @Override
        public void onClose() {
            this.minecraft.setScreen(this.parent);
        }
    }
}
