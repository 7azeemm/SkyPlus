package com.hazem.skyplus.utils;

import com.hazem.skyplus.data.bazaar.Bazaar;
import com.hazem.skyplus.data.bazaar.BazaarData;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

public class ItemUtils {

    @SuppressWarnings("deprecation")
    public static @NotNull NbtCompound getCustomData(@NotNull ComponentHolder itemStack) {
        return itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
    }

    public static boolean isSkyblockItem(ComponentHolder itemStack) {
        return getCustomData(itemStack).contains("id");
    }

    public static String getItemId(ComponentHolder itemStack) {
        NbtCompound customData = getCustomData(itemStack);
        String id = customData.getString("id");
        if (id.isEmpty()) return id;

        switch (id) {
            case "ENCHANTED_BOOK" -> {
                if (customData.contains("enchantments")) {
                    NbtCompound enchants = customData.getCompound("enchantments");
                    String enchant = enchants.getKeys().stream().findFirst().orElse("");
                    return "ENCHANTMENT_" + enchant.toUpperCase(Locale.ENGLISH) + "_" + enchants.getInt(enchant);
                }
            }
        }
        return id;
    }

    public static Optional<Double> getBazaarPrice(String itemId) {
        BazaarData.BazaarResponse bazaarResponse = Bazaar.getCachedData();
        if (bazaarResponse != null && bazaarResponse.products().containsKey(itemId)) {
            BazaarData.Product product = bazaarResponse.products().get(itemId);
            return Optional.of(product.quick_status().sellPrice());
        }

        return Optional.empty();
    }
}