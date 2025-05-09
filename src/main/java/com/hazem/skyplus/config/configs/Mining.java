package com.hazem.skyplus.config.configs;

import com.hazem.skyplus.annotations.ConfigCategory;
import com.hazem.skyplus.annotations.ConfigOption;
import com.hazem.skyplus.annotations.ConfigSubCategory;
import com.hazem.skyplus.config.controllers.ButtonOption;
import com.hazem.skyplus.skyblock.mining.excavator.ExcavatorTracker;
import com.hazem.skyplus.skyblock.mining.powderMining.PowderMiningTracker;
import com.hazem.skyplus.utils.hud.HUDMaster;
import com.hazem.skyplus.utils.hud.tracker.Tracker;
import com.hazem.skyplus.utils.hud.tracker.TrackerResetMode;

@ConfigCategory(name = "Mining")
public class Mining {

    @ConfigSubCategory(name = "Powder Mining Tracker")
    public PowderMining powderMiningTracker = new PowderMining();

    @ConfigSubCategory(name = "Excavator Tracker")
    public Excavator excavatorTracker = new Excavator();

    public static class PowderMining {

        @ConfigOption(name = "Enable HUD")
        public boolean enableHUD = true;

        @ConfigOption(name = "Include Hard Stone", description = "Includes Hard Stone added to the sack.\nRequires sack messages to be enabled in your SkyBlock profile settings!")
        public boolean includeHardStone = true;

        @ConfigOption(name = "Display Item Prices", description = "Toggle to show the price of each item in the tracker.")
        public boolean showAllPrices = true;

        @ConfigOption(name = "Enable Background Display", description = "Toggles the display of a background behind the HUD.")
        public boolean enableBackgroundDisplay = true;

        @ConfigOption(name = "Reset Mode", description = "Defines when the tracker resets its data.")
        public TrackerResetMode resetMode = TrackerResetMode.NEVER;

        @ConfigOption(name = "Reset Tracker", description = "Resets all tracker data")
        public transient ButtonOption resetTracker = new ButtonOption("RESET", () -> HUDMaster.getWidgetOfType(PowderMiningTracker.class).ifPresent(Tracker::reset));
    }

    public static class Excavator {

        @ConfigOption(name = "Enable HUD")
        public boolean enableHUD = true;

        @ConfigOption(name = "Display Item Prices", description = "Toggle to show the price of each item in the tracker.")
        public boolean showAllPrices = true;

        @ConfigOption(name = "Enable Background Display", description = "Toggles the display of a background behind the HUD.")
        public boolean enableBackgroundDisplay = true;

        @ConfigOption(name = "Reset Mode", description = "Defines when the tracker resets its data.")
        public TrackerResetMode resetMode = TrackerResetMode.NEVER;

        @ConfigOption(name = "Reset Tracker", description = "Resets all tracker data")
        public transient ButtonOption resetTracker = new ButtonOption("RESET", () -> HUDMaster.getWidgetOfType(ExcavatorTracker.class).ifPresent(Tracker::reset));
    }
}