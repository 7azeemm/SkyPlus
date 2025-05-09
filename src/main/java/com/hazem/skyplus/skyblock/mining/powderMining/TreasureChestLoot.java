package com.hazem.skyplus.skyblock.mining.powderMining;

import com.hazem.skyplus.utils.hud.tracker.ItemLoot;
import net.minecraft.util.Formatting;

public enum TreasureChestLoot implements ItemLoot {
    GEMSTONE_POWDER("Gemstone Powder", Formatting.LIGHT_PURPLE, 1),
    ESSENCE_DIAMOND("Diamond Essence", Formatting.AQUA, 2),
    ESSENCE_GOLD("Gold Essence", Formatting.GOLD, 3),
    ENCHANTED_HARD_STONE("Enchanted Hard Stone", Formatting.GREEN, 4),
    HARD_STONE("Hard Stone", Formatting.WHITE),
    GOBLIN_EGG_BLUE("Blue Goblin Egg", Formatting.AQUA),
    GOBLIN_EGG_RED("Red Goblin Egg", Formatting.RED),
    GOBLIN_EGG_YELLOW("Yellow Goblin Egg", Formatting.YELLOW),
    GOBLIN_EGG_GREEN("Green Goblin Egg", Formatting.GREEN),
    GOBLIN_EGG("Goblin Egg", Formatting.BLUE),
    ELECTRON_TRANSMITTER("Electron Transmitter", Formatting.BLUE),
    FTX_3070("FTX 3070", Formatting.BLUE),
    ROBOTRON_REFLECTOR("Robotron Reflector", Formatting.BLUE),
    CONTROL_SWITCH("Control Switch", Formatting.BLUE),
    SYNTHETIC_HEART("Synthetic Heart", Formatting.BLUE),
    SUPERLITE_MOTOR("Superlite Motor", Formatting.BLUE),
    SLUDGE_JUICE("Sludge Juice", Formatting.GREEN),
    TREASURITE("Treasurite", Formatting.DARK_PURPLE),
    YOGGIE("Yoggie", Formatting.GREEN),
    PREHISTORIC_EGG("Prehistoric Egg", Formatting.WHITE),
    PICKONIMBUS("Pickonimbus", Formatting.DARK_PURPLE),
    WISHING_COMPASS("Wishing Compass", Formatting.GREEN),
    ASCENSION_ROPE("Ascension Rope", Formatting.BLUE),
    JUNGLE_HEART("Jungle Heart", Formatting.GOLD),
    OIL_BARREL("Oil Barrel", Formatting.GREEN),
    ROUGH_RUBY_GEM("❤ Rough Ruby Gemstone", Formatting.WHITE),
    FLAWED_RUBY_GEM("❤ Flawed Ruby Gemstone", Formatting.GREEN),
    FINE_RUBY_GEM("❤ Fine Ruby Gemstone", Formatting.BLUE),
    FLAWLESS_RUBY_GEM("❤ Flawless Ruby Gemstone", Formatting.DARK_PURPLE),
    ROUGH_AMETHYST_GEM("❈ Rough Amethyst Gemstone", Formatting.WHITE),
    FLAWED_AMETHYST_GEM("❈ Flawed Amethyst Gemstone", Formatting.GREEN),
    FINE_AMETHYST_GEM("❈ Fine Amethyst Gemstone", Formatting.BLUE),
    FLAWLESS_AMETHYST_GEM("❈ Flawless Amethyst Gemstone", Formatting.DARK_PURPLE),
    ROUGH_JADE_GEM("☘ Rough Jade Gemstone", Formatting.WHITE),
    FLAWED_JADE_GEM("☘ Flawed Jade Gemstone", Formatting.GREEN),
    FINE_JADE_GEM("☘ Fine Jade Gemstone", Formatting.BLUE),
    FLAWLESS_JADE_GEM("☘ Flawless Jade Gemstone", Formatting.DARK_PURPLE),
    ROUGH_AMBER_GEM("⸕ Rough Amber Gemstone", Formatting.WHITE),
    FLAWED_AMBER_GEM("⸕ Flawed Amber Gemstone", Formatting.GREEN),
    FINE_AMBER_GEM("⸕ Fine Amber Gemstone", Formatting.BLUE),
    FLAWLESS_AMBER_GEM("⸕ Flawless Amber Gemstone", Formatting.DARK_PURPLE),
    ROUGH_SAPPHIRE_GEM("✎ Rough Sapphire Gemstone", Formatting.WHITE),
    FLAWED_SAPPHIRE_GEM("✎ Flawed Sapphire Gemstone", Formatting.GREEN),
    FINE_SAPPHIRE_GEM("✎ Fine Sapphire Gemstone", Formatting.BLUE),
    FLAWLESS_SAPPHIRE_GEM("✎ Flawless Sapphire Gemstone", Formatting.DARK_PURPLE),
    ROUGH_TOPAZ_GEM("✧ Rough Topaz Gemstone", Formatting.WHITE),
    FLAWED_TOPAZ_GEM("✧ Flawed Topaz Gemstone", Formatting.GREEN),
    FINE_TOPAZ_GEM("✧ Fine Topaz Gemstone", Formatting.BLUE),
    FLAWLESS_TOPAZ_GEM("✧ Flawless Topaz Gemstone", Formatting.DARK_PURPLE);

    public final String displayName;
    public final Formatting color;
    private final Integer priority;

    TreasureChestLoot(String displayName, Formatting color) {
        this(displayName, color, Integer.MAX_VALUE);
    }

    TreasureChestLoot(String displayName, Formatting color, Integer priority) {
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
