package com.hazem.skyplus.config.configs;

import com.hazem.skyplus.annotations.ConfigCategory;
import com.hazem.skyplus.annotations.ConfigOption;
import com.hazem.skyplus.annotations.ConfigSubCategory;

@ConfigCategory(name = "Garden")
public class Garden {

    @ConfigSubCategory(name = "Jacob's Contest")
    public JacobContest jacobContest = new JacobContest();

    public static class JacobContest {

        @ConfigOption(name = "Enable Jacob's Contest HUD", description = "Displays a HUD showing the details of the active and upcoming Jacob's Contest.")
        public boolean enableHUD = true;

        @ConfigOption(name = "Enable HUD Outside the Garden", description = "Enables the HUD to be visible throughout SkyBlock, not just in the Garden.")
        public boolean enableOutsideGarden = true;

        @ConfigOption(name = "Enable Background Display", description = "Toggles the display of a background behind the Jacob's Contest HUD.")
        public boolean enableBackgroundDisplay = true;
    }
}
