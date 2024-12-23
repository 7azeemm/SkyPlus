package com.hazem.skyplus.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class Garden {

    @SerialEntry
    public JacobContest jacobContest = new JacobContest();

    public static class JacobContest {

        @SerialEntry
        public boolean enableHUD = true;

        @SerialEntry
        public boolean enableOutsideGarden = true;

        @SerialEntry
        public boolean enableBackgroundDisplay = true;

    }
}
