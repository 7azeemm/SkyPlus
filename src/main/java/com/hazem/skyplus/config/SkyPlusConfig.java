package com.hazem.skyplus.config;

import com.hazem.skyplus.config.configs.Garden;
import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class SkyPlusConfig {

    @SerialEntry
    public Garden garden = new Garden();
}
