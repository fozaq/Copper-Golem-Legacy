package com.github.smallinger.coppergolemlegacy;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Configuration for Copper Golem Legacy mod
 */
public class CopperGolemLegacyConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue GOLEM_PRESSES_BUTTONS;

    static {
        BUILDER.push("coppergolemai");

        GOLEM_PRESSES_BUTTONS = BUILDER
            .comment("Enable/Disable Copper Golem randomly pressing copper buttons")
            .define("golemPressesButtons", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
