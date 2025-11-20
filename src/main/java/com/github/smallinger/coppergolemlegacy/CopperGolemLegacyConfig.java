package com.github.smallinger.coppergolemlegacy;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Configuration for Copper Golem Legacy mod
 * This class is safe to load on both client and server
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
}
