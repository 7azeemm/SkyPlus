package com.hazem.skyplus.config.categories;

import com.hazem.skyplus.config.SkyPlusConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.text.Text;

public class MiscCategory {

    public static ConfigCategory create(SkyPlusConfig defaults, SkyPlusConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.of("Misc"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.of("Disable Status Effects Overlay"))
                        .binding(defaults.misc.disableStatusEffectsOverlay,
                                () -> config.misc.disableStatusEffectsOverlay,
                                newValue -> config.misc.disableStatusEffectsOverlay = newValue)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                .build();
    }
}