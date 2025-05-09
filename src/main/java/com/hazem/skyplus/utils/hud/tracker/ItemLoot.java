package com.hazem.skyplus.utils.hud.tracker;

import net.minecraft.util.Formatting;

public interface ItemLoot {
    String getId();
    String getDisplayName();
    Formatting getColor();
    int getPriority();
}
