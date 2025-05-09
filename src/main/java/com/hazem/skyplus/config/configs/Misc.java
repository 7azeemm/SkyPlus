package com.hazem.skyplus.config.configs;

import com.hazem.skyplus.annotations.ConfigCategory;
import com.hazem.skyplus.annotations.ConfigOption;

@ConfigCategory(name = "Miscellaneous")
public class Misc {

    @ConfigOption(name = "Disable Status Effects Overlay")
    public boolean disableStatusEffectsOverlay = true;
}