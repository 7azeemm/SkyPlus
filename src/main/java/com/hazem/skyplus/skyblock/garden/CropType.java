package com.hazem.skyplus.skyblock.garden;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public enum CropType {
    WHEAT("Wheat", new ItemStack(Items.WHEAT)),
    CARROT("Carrot", new ItemStack(Items.CARROT)),
    POTATO("Potato", new ItemStack(Items.POTATO)),
    PUMPKIN("Pumpkin", new ItemStack(Items.PUMPKIN)),
    SUGAR_CANE("Sugar Cane", new ItemStack(Items.SUGAR_CANE)),
    MELON("Melon", new ItemStack(Items.MELON_SLICE)),
    CACTUS("Cactus", new ItemStack(Items.CACTUS)),
    COCOA_BEANS("Cocoa Beans", new ItemStack(Items.COCOA_BEANS)),
    MUSHROOM("Mushroom", new ItemStack(Items.RED_MUSHROOM_BLOCK)),
    NETHER_WART("Nether Wart", new ItemStack(Items.NETHER_WART));

    public final String name;
    public final ItemStack itemStack;
    private static final Map<String, CropType> CROP_NAME_TO_TYPE = new HashMap<>();

    CropType(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    static {
        for (CropType type : values()) {
            CROP_NAME_TO_TYPE.put(type.name, type);
        }
    }

    public static CropType fromCropName(String cropName) {
        return CROP_NAME_TO_TYPE.get(cropName);
    }
}