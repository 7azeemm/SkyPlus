package com.hazem.skyplus.skyblock.end.sacrificer;

import com.hazem.skyplus.utils.hud.tracker.ItemLoot;
import net.minecraft.util.Formatting;

public enum SacrificerLoot implements ItemLoot {
    ESSENCE_DRAGON("Dragon Essence", Formatting.DARK_PURPLE, 1),
    RITUAL_RESIDUE("Ritual Residue", Formatting.DARK_PURPLE),
    DRAGON_HORN("Dragon Horn", Formatting.DARK_PURPLE),
    DRAGON_CLAW("Dragon Claw", Formatting.BLUE),
    SUMMONING_EYE("Summoning Eye", Formatting.DARK_PURPLE),
    SUPERIOR_FRAGMENT("Superior Dragon Fragment", Formatting.DARK_PURPLE),
    STRONG_FRAGMENT("Strong Dragon Fragment", Formatting.DARK_PURPLE),
    UNSTABLE_FRAGMENT("Unstable Dragon Fragment", Formatting.DARK_PURPLE),
    YOUNG_FRAGMENT("Young Dragon Fragment", Formatting.DARK_PURPLE),
    OLD_FRAGMENT("Old Dragon Fragment", Formatting.DARK_PURPLE),
    WISE_FRAGMENT("Wise Dragon Fragment", Formatting.DARK_PURPLE),
    PROTECTOR_FRAGMENT("Protector Dragon Fragment", Formatting.DARK_PURPLE),
    HOLY_FRAGMENT("Holy Dragon Fragment", Formatting.DARK_PURPLE),
    ENCHANTED_ENDER_PEARL("Enchanted Ender Pearl", Formatting.GREEN),
    ENCHANTED_EYE_OF_ENDER("Enchanted Eye Of Ender", Formatting.GREEN),
    ENCHANTED_ENDSTONE("Enchanted End Stone", Formatting.GREEN),
    ENCHANTED_OBSIDIAN("Enchanted Obsidian", Formatting.GREEN);

    public final String displayName;
    public final Formatting color;
    //TODO: Pinned
    private final Integer priority;

    SacrificerLoot(String displayName, Formatting color, Integer priority) {
        this.displayName = displayName;
        this.color = color;
        this.priority = priority;
    }

    SacrificerLoot(String displayName, Formatting color) {
        this(displayName, color, Integer.MAX_VALUE);
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
