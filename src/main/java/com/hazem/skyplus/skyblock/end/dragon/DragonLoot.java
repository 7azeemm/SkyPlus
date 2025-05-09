package com.hazem.skyplus.skyblock.end.dragon;

import com.hazem.skyplus.utils.hud.tracker.ItemLoot;
import net.minecraft.util.Formatting;

//TODO: each dye should have his color for displaying its name.
public enum DragonLoot implements ItemLoot {
    LEG_ENDER_DRAGON("Ender Dragon", Formatting.GOLD),
    EPIC_ENDER_DRAGON("Ender Dragon", Formatting.DARK_PURPLE),
    DYE_PEARLESCENT("Pearlescent Dye", Formatting.GOLD),
    DRAGON_HORN("Dragon Horn", Formatting.DARK_PURPLE),
    DRAGON_CLAW("Dragon Claw", Formatting.BLUE),
    DRAGON_SCALE("Dragon Scale", Formatting.BLUE),
    ASPECT_OF_THE_DRAGON("Aspect of the Dragons", Formatting.GOLD),
    ESSENCE_DRAGON("Dragon Essence", Formatting.DARK_PURPLE),
    CRYSTAL_FRAGMENT("Crystal Fragment", Formatting.DARK_PURPLE),
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
    SUPERIOR_FRAGMENT("Superior Dragon Fragment", Formatting.DARK_PURPLE),
    STRONG_FRAGMENT("Strong Dragon Fragment", Formatting.DARK_PURPLE),
    UNSTABLE_FRAGMENT("Unstable Dragon Fragment", Formatting.DARK_PURPLE),
    YOUNG_FRAGMENT("Young Dragon Fragment", Formatting.DARK_PURPLE),
    OLD_FRAGMENT("Old Dragon Fragment", Formatting.DARK_PURPLE),
    WISE_FRAGMENT("Wise Dragon Fragment", Formatting.DARK_PURPLE),
    PROTECTOR_FRAGMENT("Protector Dragon Fragment", Formatting.DARK_PURPLE),
    DRAGON_NEST_TRAVEL_SCROLL("Travel Scroll to Dragon's Nest", Formatting.DARK_PURPLE),
    ENCHANTED_ENDER_PEARL("Enchanted Ender Pearl", Formatting.GREEN);

    public final String displayName;
    public final Formatting color;
    private final Integer priority;

    DragonLoot(String displayName, Formatting color) {
        this(displayName, color, Integer.MAX_VALUE);
    }

    DragonLoot(String displayName, Formatting color, Integer priority) {
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
