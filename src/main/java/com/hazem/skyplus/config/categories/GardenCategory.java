package com.hazem.skyplus.config.categories;

import com.hazem.skyplus.config.SkyPlusConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.text.Text;

public class GardenCategory {

    public static ConfigCategory create(SkyPlusConfig defaults, SkyPlusConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.of("Garden"))
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Jacob's Contest"))
                        .collapsed(true)
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Enable Jacob's Contest HUD"))
                                .description(OptionDescription.of(Text.of("Displays a HUD showing the details of the active and upcoming Jacob's Contest.")))
                                .binding(defaults.garden.jacobContest.enableHUD,
                                        () -> config.garden.jacobContest.enableHUD,
                                        newValue -> config.garden.jacobContest.enableHUD = newValue)
                                .controller(BooleanControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Enable HUD Outside the Garden"))
                                .description(OptionDescription.of(Text.of("Enables the HUD to be visible throughout SkyBlock, not just in the Garden.")))
                                .binding(defaults.garden.jacobContest.enableOutsideGarden,
                                        () -> config.garden.jacobContest.enableOutsideGarden,
                                        newValue -> config.garden.jacobContest.enableOutsideGarden = newValue)
                                .controller(BooleanControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Enable Background Display"))
                                .description(OptionDescription.of(Text.of("Toggles the display of a background behind the Jacob's Contest HUD.")))
                                .binding(defaults.garden.jacobContest.enableBackgroundDisplay,
                                        () -> config.garden.jacobContest.enableBackgroundDisplay,
                                        newValue -> config.garden.jacobContest.enableBackgroundDisplay = newValue)
                                .controller(BooleanControllerBuilder::create)
                                .build())
                        .build())
                .build();
    }
}
