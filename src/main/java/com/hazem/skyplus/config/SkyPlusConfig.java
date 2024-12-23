package com.hazem.skyplus.config;

import com.hazem.skyplus.config.configs.Garden;
import com.hazem.skyplus.config.configs.Misc;
import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class SkyPlusConfig {

    @SerialEntry
    public Garden garden = new Garden();

    @SerialEntry
    public Misc misc = new Misc();
}
