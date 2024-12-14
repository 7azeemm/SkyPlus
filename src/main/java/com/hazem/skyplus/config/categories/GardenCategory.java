package com.hazem.skyplus.config.categories;

import com.hazem.skyplus.config.SkyPlusConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.text.Text;

public class GardenCategory {

    public static ConfigCategory create(SkyPlusConfig defaults, SkyPlusConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.of("Garden"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.of("Garden HUD"))
                        .description(OptionDescription.of(Text.of("Shows Garden HUD")))
                        .binding(defaults.garden.gardenHUD,
                                () -> config.garden.gardenHUD,
                                newValue -> config.garden.gardenHUD = newValue)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                .build();
    }
}
