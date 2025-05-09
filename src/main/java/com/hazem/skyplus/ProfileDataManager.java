package com.hazem.skyplus;

import com.hazem.skyplus.config.PersistentData;
import com.hazem.skyplus.utils.HypixelData;
import com.mojang.serialization.Codec;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A subclass of PersistentData that adds profile-specific data loading and saving.
 * It loads and saves data specific to a profile identified by the profile ID.
 *
 * @param <T> The type of data to be serialized/deserialized.
 */
public class ProfileDataManager<T> extends PersistentData<T> {
    private Map<String, T> profileData = new HashMap<>();
    protected Supplier<T> defaultSupplier;
    private T tempProfileData;

    /**
     * Constructs a ProfileDataManager instance with a specified file path, codec, and a default supplier.
     * Loads the profile data upon instantiation.
     *
     * @param filePath The file path where the profile data is stored.
     * @param codec    The codec for encoding and decoding the profile data.
     * @param defaultSupplier A supplier for creating default profile data when it doesn't exist.
     */
    public ProfileDataManager(Path filePath, Codec<T> codec, Supplier<T> defaultSupplier) {
        super(filePath, codec);
        this.defaultSupplier = defaultSupplier;
        load(filePath);
    }

    /**
     * Constructs a ProfileDataManager instance with a specified codec and default supplier,
     * but does not load from a file initially.
     *
     * @param codec           The codec for encoding and decoding the profile data.
     * @param defaultSupplier A supplier for creating default profile data when it doesn't exist.
     */
    public ProfileDataManager(Codec<T> codec, Supplier<T> defaultSupplier) {
        super(null, codec);
        this.defaultSupplier = defaultSupplier;
    }

    /**
     * Loads the profile data from the file.
     *
     * @param filePath The file path where the profile data is stored.
     */
    public void load(Path filePath) {
        this.setFilePath(filePath);
        this.profileData = load();
    }

    /**
     * Saves the current profile data to the file.
     */
    public void save() {
        save(profileData);
    }

    /**
     * Saves the current profile data to the file.
     *
     * @param filePath The file path where the profile data is stored.
     */
    public void save(Path filePath) {
        this.setFilePath(filePath);
        save(profileData);
    }

    /**
     * Retrieves the profile data for the current profile ID. If the profile ID is still not detected,
     * it creates a temporary object using the default supplier. This temporary object
     * is not associated with any profile ID and will not be saved. Once the profile ID is detected,
     * the actual profile data is created or retrieved for the detected profile ID.
     *
     * @return The profile data for the current profile. If the profile ID is not detected, a temporary object is returned.
     */
    public T get() {
        if (HypixelData.profileID.isEmpty()) {
            if (tempProfileData == null) {
                tempProfileData = defaultSupplier.get();
            }
            return tempProfileData;
        }
        return profileData.computeIfAbsent(HypixelData.profileID, id -> defaultSupplier.get());
    }

    /**
     * Retrieves all the profile data stored in the manager.
     *
     * @return A collection containing all the profile data.
     */
    public Collection<T> getProfilesData() {
        return profileData.values();
    }
}