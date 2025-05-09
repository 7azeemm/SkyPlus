package com.hazem.skyplus.config.configs;

import com.hazem.skyplus.annotations.ConfigCategory;
import com.hazem.skyplus.annotations.ConfigOption;
import com.hazem.skyplus.annotations.ConfigSubCategory;
import com.hazem.skyplus.config.controllers.ButtonOption;
import com.hazem.skyplus.skyblock.end.dragon.DragonTracker;
import com.hazem.skyplus.skyblock.end.sacrificer.SacrificerTracker;
import com.hazem.skyplus.utils.hud.HUDMaster;
import com.hazem.skyplus.utils.hud.tracker.Tracker;
import com.hazem.skyplus.utils.hud.tracker.TrackerResetMode;

@ConfigCategory(name = "End")
public class End {

    @ConfigSubCategory(name = "Sacrificer Tracker")
    public Sacrificer sacrificerTracker = new Sacrificer();

    public static class Sacrificer {

        @ConfigOption(name = "Enable HUD")
        public boolean enableHUD = true;

        @ConfigOption(name = "Display Item Prices", description = "Toggle to show the price of each item in the tracker.")
        public boolean showAllPrices = true;

        @ConfigOption(name = "Enable Background Display", description = "Toggles the display of a background behind the HUD.")
        public boolean enableBackgroundDisplay = true;

        @ConfigOption(name = "Reset Mode", description = "Defines when the tracker resets its data.")
        public TrackerResetMode resetMode = TrackerResetMode.NEVER;

        @ConfigOption(name = "Reset Tracker", description = "Resets all tracker data")
        public transient ButtonOption resetTracker = new ButtonOption("RESET", () -> HUDMaster.getWidgetOfType(SacrificerTracker.class).ifPresent(Tracker::reset));
    }

    @ConfigSubCategory(name = "Dragon Tracker")
    public Dragon dragonTracker = new Dragon();

    public static class Dragon {

        @ConfigOption(name = "Enable HUD")
        public boolean enableHUD = true;

        @ConfigOption(name = "Display Item Prices", description = "Toggle to show the price of each item in the tracker.")
        public boolean showAllPrices = true;

        @ConfigOption(name = "Enable Background Display", description = "Toggles the display of a background behind the HUD.")
        public boolean enableBackgroundDisplay = true;

        @ConfigOption(name = "Reset Mode", description = "Defines when the tracker resets its data.")
        public TrackerResetMode resetMode = TrackerResetMode.NEVER;

        @ConfigOption(name = "Reset Tracker", description = "Resets all tracker data")
        public transient ButtonOption resetTracker = new ButtonOption("RESET", () -> HUDMaster.getWidgetOfType(DragonTracker.class).ifPresent(Tracker::reset));
    }
}
