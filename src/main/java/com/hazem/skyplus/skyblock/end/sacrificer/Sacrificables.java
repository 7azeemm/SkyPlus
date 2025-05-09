package com.hazem.skyplus.skyblock.end.sacrificer;

import com.hazem.skyplus.utils.hud.tracker.ItemLoot;
import net.minecraft.util.Formatting;

public enum Sacrificables implements ItemLoot {
    ASPECT_OF_THE_DRAGON("Aspect of the Dragons", Formatting.GOLD),
    YOUNG_DRAGON_HELMET("Young Dragon Helmet", Formatting.GOLD),
    YOUNG_DRAGON_CHESTPLATE("Young Dragon Chestplate", Formatting.GOLD),
    YOUNG_DRAGON_LEGGINGS("Young Dragon Leggings", Formatting.GOLD),
    YOUNG_DRAGON_BOOTS("Young Dragon Boots", Formatting.GOLD),
    OLD_DRAGON_HELMET("Old Dragon Helmet", Formatting.GOLD),
    OLD_DRAGON_CHESTPLATE("Old Dragon Chestplate", Formatting.GOLD),
    OLD_DRAGON_LEGGINGS("Old Dragon Leggings", Formatting.GOLD),
    OLD_DRAGON_BOOTS("Old Dragon Boots", Formatting.GOLD),
    STRONG_DRAGON_HELMET("Strong Dragon Helmet", Formatting.GOLD),
    STRONG_DRAGON_CHESTPLATE("Strong Dragon Chestplate", Formatting.GOLD),
    STRONG_DRAGON_LEGGINGS("Strong Dragon Leggings", Formatting.GOLD),
    STRONG_DRAGON_BOOTS("Strong Dragon Boots", Formatting.GOLD),
    WISE_DRAGON_HELMET("Wise Dragon Helmet", Formatting.GOLD),
    WISE_DRAGON_CHESTPLATE("Wise Dragon Chestplate", Formatting.GOLD),
    WISE_DRAGON_LEGGINGS("Wise Dragon Leggings", Formatting.GOLD),
    WISE_DRAGON_BOOTS("Wise Dragon Boots", Formatting.GOLD),
    UNSTABLE_DRAGON_HELMET("Unstable Dragon Helmet", Formatting.GOLD),
    UNSTABLE_DRAGON_CHESTPLATE("Unstable Dragon Chestplate", Formatting.GOLD),
    UNSTABLE_DRAGON_LEGGINGS("Unstable Dragon Leggings", Formatting.GOLD),
    UNSTABLE_DRAGON_BOOTS("Unstable Dragon Boots", Formatting.GOLD),
    PROTECTOR_DRAGON_HELMET("Protector Dragon Helmet", Formatting.GOLD),
    PROTECTOR_DRAGON_CHESTPLATE("Protector Dragon Chestplate", Formatting.GOLD),
    PROTECTOR_DRAGON_LEGGINGS("Protector Dragon Leggings", Formatting.GOLD),
    PROTECTOR_DRAGON_BOOTS("Protector Dragon Boots", Formatting.GOLD),
    SUPERIOR_DRAGON_HELMET("Superior Dragon Helmet", Formatting.GOLD),
    SUPERIOR_DRAGON_CHESTPLATE("Superior Dragon Chestplate", Formatting.GOLD),
    SUPERIOR_DRAGON_LEGGINGS("Superior Dragon Leggings", Formatting.GOLD),
    SUPERIOR_DRAGON_BOOTS("Superior Dragon Boots", Formatting.GOLD),
    HOLY_DRAGON_HELMET("Holy Dragon Helmet", Formatting.GOLD),
    HOLY_DRAGON_CHESTPLATE("Holy Dragon Chestplate", Formatting.GOLD),
    HOLY_DRAGON_LEGGINGS("Holy Dragon Leggings", Formatting.GOLD),
    HOLY_DRAGON_BOOTS("Holy Dragon Boots", Formatting.GOLD),
    END_HELMET("Ender Helmet", Formatting.DARK_PURPLE),
    END_CHESTPLATE("Ender Chestplate", Formatting.DARK_PURPLE),
    END_LEGGINGS("Ender Leggings", Formatting.DARK_PURPLE),
    END_BOOTS("Ender Boots", Formatting.DARK_PURPLE),
    ENDER_NECKLACE("Ender Necklace", Formatting.DARK_PURPLE),
    ENDER_CLOAK("Ender Cloak", Formatting.DARK_PURPLE),
    ENDER_BELT("Ender Belt", Formatting.DARK_PURPLE),
    ENDER_GAUNTLET("Ender Gauntlet", Formatting.DARK_PURPLE),
    END_STONE_BOW("End Stone Bow", Formatting.DARK_PURPLE),
    OBSIDIAN_CHESTPLATE("Obsidian Chestplate", Formatting.DARK_PURPLE);

    public final String displayName;
    public final Formatting color;

    Sacrificables(String displayName, Formatting color) {
        this.displayName = displayName;
        this.color = color;
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
        return Integer.MAX_VALUE;
    }

    @Override
    public String getId() {
        return name();
    }
}
