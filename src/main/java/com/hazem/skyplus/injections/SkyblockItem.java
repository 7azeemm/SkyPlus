package com.hazem.skyplus.injections;

public interface SkyblockItem {

    default String getItemId() {
        return "";
    }

    default boolean isSkyblockItem() {
        return false;
    }
}