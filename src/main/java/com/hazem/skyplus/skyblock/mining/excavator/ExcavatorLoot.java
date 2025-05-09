package com.hazem.skyplus.skyblock.mining.excavator;

import com.hazem.skyplus.utils.hud.tracker.ItemLoot;
import net.minecraft.util.Formatting;

public enum ExcavatorLoot implements ItemLoot {
    GLACITE_POWDER("Glacite Powder", Formatting.AQUA, 1),
    ESSENCE_DIAMOND("Diamond Essence", Formatting.AQUA, 2),
    ESSENCE_GOLD("Gold Essence", Formatting.GOLD, 3),
    ENCHANTED_PALEONTOLOGIST_1("Paleontologist I", Formatting.BLUE),
    DYE_FOSSIL("Fossil Dye", Formatting.GRAY),
    CLAW_FOSSIL("Claw Fossil", Formatting.GOLD),
    CLUBBED_FOSSIL("Clubbed Fossil", Formatting.GOLD),
    FOOTPRINT_FOSSIL("Footprint Fossil", Formatting.GOLD),
    SPINE_FOSSIL("Spine Fossil", Formatting.GOLD),
    TUSK_FOSSIL("Tusk Fossil", Formatting.GOLD),
    UGLY_FOSSIL("Ugly Fossil", Formatting.GOLD),
    WEBBED_FOSSIL("Webbed Fossil", Formatting.GOLD),
    HELIX("Helix Fossil", Formatting.GOLD),
    FOSSIL_THE_FISH("Fossil the Fish", Formatting.RED),
    ENCHANTED_UMBER("Enchanted Umber", Formatting.BLUE),
    ENCHANTED_TUNGSTEN("Enchanted Tungsten", Formatting.BLUE),
    ENCHANTED_GLACITE("Enchanted Glacite", Formatting.BLUE),
    FOSSIL_DUST("Fossil Dust", Formatting.WHITE),
    FLAWED_AQUAMARINE_GEM("α Flawed Aquamarine Gemstone", Formatting.GREEN),
    FINE_AQUAMARINE_GEM("α Fine Aquamarine Gemstone", Formatting.BLUE),
    FLAWED_CITRINE_GEM("☘ Flawed Citrine Gemstone", Formatting.GREEN),
    FINE_CITRINE_GEM("☘ Fine Citrine Gemstone", Formatting.BLUE),
    FLAWED_ONYX_GEM("☣ Flawed Onyx Gemstone", Formatting.GREEN),
    FINE_ONYX_GEM("☣ Fine Onyx Gemstone", Formatting.BLUE),
    FLAWED_PERIDOT_GEM("☘ Flawed Peridot Gemstone", Formatting.GREEN),
    FINE_PERIDOT_GEM("☘ Fine Peridot Gemstone", Formatting.BLUE);

    public final String displayName;
    public final Formatting color;
    private final Integer priority;

    ExcavatorLoot(String displayName, Formatting color) {
        this(displayName, color, Integer.MAX_VALUE);
    }

    ExcavatorLoot(String displayName, Formatting color, Integer priority) {
        this.displayName = displayName;
        this.color = color;
        this.priority = priority;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Formatting getColor() {
        return color;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getId() {
        return name();
    }
}
