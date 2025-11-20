package com.github.smallinger.coppergolemlegacy.client;

import com.github.smallinger.coppergolemlegacy.CopperGolemLegacyConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Client-only configuration screen for Copper Golem Legacy
 * This class must not be referenced from server-side code
 */
@OnlyIn(Dist.CLIENT)
public class ConfigScreenFactory {
    
    /**
     * Creates a configuration screen for the mod
     * @param parent The parent screen to return to
     * @return A new configuration screen
     */
    public static Screen createConfigScreen(Screen parent) {
        return new CopperGolemConfigScreen(parent);
    }
    
    /**
     * Simple config screen for Copper Golem Legacy
     */
    @OnlyIn(Dist.CLIENT)
    private static class CopperGolemConfigScreen extends Screen {
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
                    boolean newValue = !CopperGolemLegacyConfig.GOLEM_PRESSES_BUTTONS.get();
                    CopperGolemLegacyConfig.GOLEM_PRESSES_BUTTONS.set(newValue);
                    button.setMessage(getToggleButtonText());
                    CopperGolemLegacyConfig.SPEC.save();
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
            boolean enabled = CopperGolemLegacyConfig.GOLEM_PRESSES_BUTTONS.get();
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
