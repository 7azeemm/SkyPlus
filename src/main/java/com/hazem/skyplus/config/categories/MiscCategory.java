package com.hazem.skyplus.config.categories;

import com.hazem.skyplus.config.ConfigManager;
import com.hazem.skyplus.config.controllers.BooleanController;
import com.hazem.skyplus.config.gui.Category;
import com.hazem.skyplus.config.gui.Option;
import net.minecraft.text.Text;

public class MiscCategory {

    public static Category create() {
        return Category.createBuilder()
                .name(Text.of("Miscellaneous"))
                .option(Option.createBuilder()
                        .name(Text.of("Disable Status Effects Overlay"))
                        .controller(new BooleanController(newValue -> ConfigManager.getConfig().misc.disableStatusEffectsOverlay = newValue, ConfigManager.getConfig().misc.disableStatusEffectsOverlay))
                        .build())
                .build();
    }
}