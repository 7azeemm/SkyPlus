package com.hazem.skyplus.config.categories;

import com.hazem.skyplus.config.ConfigManager;
import com.hazem.skyplus.config.controllers.BooleanController;
import com.hazem.skyplus.config.gui.Category;
import com.hazem.skyplus.config.gui.Group;
import com.hazem.skyplus.config.gui.Option;
import net.minecraft.text.Text;

public class GardenCategory {

    public static Category create() {
        return Category.createBuilder()
                .name(Text.of("Garden"))
                .group(Group.createBuilder()
                        .name(Text.of("Jacob's Contest"))
                        .option(Option.createBuilder()
                                .name(Text.of("Enable Jacob's Contest HUD"))
                                .controller(new BooleanController(newValue -> ConfigManager.getConfig().misc.disableStatusEffectsOverlay = newValue, ConfigManager.getConfig().misc.disableStatusEffectsOverlay))
                                .description(Text.of("Displays a HUD showing the details of the active and upcoming Jacob's Contest."))
                                .build())
                        .option(Option.createBuilder()
                                .name(Text.of("Enable HUD Outside the Garden"))
                                .description(Text.of("Enables the HUD to be visible throughout SkyBlock, not just in the Garden."))
                                .controller(new BooleanController(newValue -> ConfigManager.getConfig().misc.disableStatusEffectsOverlay = newValue, ConfigManager.getConfig().misc.disableStatusEffectsOverlay))
                                .build())
                        .option(Option.createBuilder()
                                .name(Text.of("Enable Background Display"))
                                .description(Text.of("Toggles the display of a background behind the Jacob's Contest HUD."))
                                .controller(new BooleanController(newValue -> ConfigManager.getConfig().misc.disableStatusEffectsOverlay = newValue, ConfigManager.getConfig().misc.disableStatusEffectsOverlay))
                                .build())
                        .build())
                .build();
    }
}