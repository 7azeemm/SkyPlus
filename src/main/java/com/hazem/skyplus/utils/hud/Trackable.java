package com.hazem.skyplus.utils.hud;

import java.util.function.Supplier;

/**
 * Represents a trackable item with a supplier that provides a value to monitor for changes.
 */
public class Trackable<T> {
    private final Supplier<T> supplier;
    private T previousValue;

    public Trackable(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Checks if the value has changed since the last check.
     *
     * @return true if the value has changed, false otherwise
     */
    public boolean isChanged() {
        T currentValue = supplier.get();  // Get the current value from the supplier
        if (!currentValue.equals(previousValue)) {  // Compare with the previous value
            previousValue = currentValue;  // Update the previous value
            return true;
        }
        return false;
    }
}