package com.hazem.skyplus.mixins;

import com.hazem.skyplus.skyblock.end.dragon.DragonTracker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Unique
    private final Map<Integer, ItemStack> previousInventory = new HashMap<>();
    @Unique
    private final Map<Integer, Integer> originalSlots = new HashMap<>(); // Store original slot mappings
    @Unique
    private final Set<Integer> trackedLootHashes = new HashSet<>(); // Avoid duplicate logs (use hash instead of full ItemStack)

    @Inject(method = "setStack", at = @At("HEAD"))
    private void beforeSetStack(int slot, ItemStack stack, CallbackInfo ci) {
        if (!DragonTracker.INSTANCE.shouldRender() || stack == null) return;

        PlayerInventory inventory = (PlayerInventory) (Object) this;
        if (slot >= 0 && slot < inventory.size()) {
            previousInventory.put(slot, inventory.getStack(slot).copy());
            originalSlots.put(System.identityHashCode(stack), slot); // Track original slot
        }
    }

    @Inject(method = "setStack", at = @At("TAIL"))
    private void afterSetStack(int slot, ItemStack stack, CallbackInfo ci) {
        if (!DragonTracker.INSTANCE.shouldRender() || stack == null) return;

        PlayerInventory inventory = (PlayerInventory) (Object) this;

        // Get the original slot before it was changed
        int originalSlot = originalSlots.getOrDefault(System.identityHashCode(stack), slot);
        if (originalSlot < 0 || originalSlot >= inventory.size()) return;

        ItemStack previousStack = previousInventory.get(originalSlot);
        if (isNewItem(previousStack, stack)) {
            int addedAmount = stack.getCount() - (previousStack != null && previousStack.getItemId().equals(stack.getItemId()) ? previousStack.getCount() : 0);

            int stackHash = System.identityHashCode(stack);
            if (addedAmount > 0 && trackedLootHashes.add(stackHash)) {
                DragonTracker.INSTANCE.onItemPickUp(stack, addedAmount);
            }
        }
    }

    @Unique
    private boolean isNewItem(ItemStack previousStack, ItemStack newStack) {
        if (newStack == null || newStack.isEmpty()) return false;
        if (previousStack == null || previousStack.isEmpty()) return true; // A new item was placed in an empty slot

        // Check if the item type changed OR count increased
        return !ItemStack.areItemsEqual(previousStack, newStack) || newStack.getCount() > previousStack.getCount();
    }
}
