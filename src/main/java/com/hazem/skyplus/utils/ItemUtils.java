package com.hazem.skyplus.utils;

import com.hazem.skyplus.data.Auction;
import com.hazem.skyplus.data.Bazaar;
import com.hazem.skyplus.data.PriceResult;
import com.hazem.skyplus.data.PriceSource;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemUtils {

    @SuppressWarnings("deprecation")
    public static @NotNull NbtCompound getCustomData(@NotNull ComponentHolder itemStack) {
        return itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
    }

    /**
     * Checks if the item is a SkyBlock item by verifying if it contains the "id" key in its custom data.
     *
     * @param itemStack The component holder representing the item stack.
     * @return `true` if the item is a SkyBlock item, otherwise `false`.
     */
    public static boolean isSkyblockItem(ComponentHolder itemStack) {
        return getCustomData(itemStack).contains("id");
    }

    /**
     * Retrieves the item ID associated with a SkyBlock item.
     *
     * @param itemStack The component holder representing the item stack.
     * @return The item ID, or an empty string if no ID is found.
     */
    public static String getItemId(ComponentHolder itemStack) {
        NbtCompound customData = getCustomData(itemStack);
        String id = customData.getString("id");
        if (id.isEmpty()) return id;

        return ItemIdResolver.resolveItemId(id, customData);
    }

    /**
     * Retrieves the price of an item from the Bazaar.
     *
     * @param itemId The ID of the item.
     * @param useBuyPrice If `true`, the buy price is returned; if `false`, the sell price is returned.
     * @return An `Optional` containing the price, or empty optional if the item is not found.
     */
    public static Optional<PriceResult> getBazaarPrice(String itemId, boolean useBuyPrice) {
        Bazaar.BazaarResponse bazaarResponse = Bazaar.data;
        if (bazaarResponse != null && bazaarResponse.products().containsKey(itemId)) {
            Bazaar.QuickStatus productStatus = bazaarResponse.products().get(itemId).quick_status();
            double price = useBuyPrice ? productStatus.buyPrice() : productStatus.sellPrice();
            return Optional.of(new PriceResult(price, PriceSource.BAZAAR));
        }

        return Optional.empty();
    }

    /**
     * Retrieves the price of an item from the Auction House.
     *
     * @param itemId The ID of the item.
     * @return An `Optional` containing the price, or empty optional if the item is not found.
     */
    public static Optional<PriceResult> getAuctionPrice(String itemId) {
        Object2DoubleOpenHashMap<String> data = Auction.data;
        if (data != null && data.containsKey(itemId)) {
            return Optional.of(new PriceResult(data.getDouble(itemId), PriceSource.AUCTION));
        }

        return Optional.empty();
    }

    /**
     * Retrieves the price of an item, checking both the Auction House and Bazaar.
     * This method first attempts to fetch the item’s auction price. If the auction price is not available,
     * it falls back to fetching the Bazaar price (buy price).
     *
     * @param itemId The ID of the item.
     * @return An `Optional` containing the item price, or empty optional if neither the Auction House nor Bazaar has the item price.
     */
    public static Optional<PriceResult> getItemPrice(String itemId) {
        return getAuctionPrice(itemId).or(() -> getBazaarPrice(itemId, true));
    }
}