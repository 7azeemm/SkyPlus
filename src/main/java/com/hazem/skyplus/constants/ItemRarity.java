package com.hazem.skyplus.constants;

public enum ItemRarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    MYTHIC,
    DIVAN,
    SPECIAL,
    VERY_SPECIAL;

    public static ItemRarity getRarityByOrdinal(int ordinal) {
        for (ItemRarity rarity : values()) {
            if (rarity.ordinal() == ordinal)
                return rarity;
        }
        return null;
    }
}