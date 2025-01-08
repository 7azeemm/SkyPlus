package com.hazem.skyplus.utils;

import com.hazem.skyplus.Skyplus;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;

public class ModUtils {

    /**
     * Gets the version string of the mod.
     *
     * @return The version string or "Unknown" if unavailable.
     */
    public static String getModVersion() {
        return FabricLoader.getInstance()
                .getModContainer(Skyplus.NAMESPACE)
                .map(ModContainer::getMetadata)
                .map(metadata -> metadata.getVersion().getFriendlyString())
                .orElse("Unknown");
    }

    /**
     * Retrieves a custom social link from the fabric.mod.json file.
     *
     * @param key The key for the social link (e.g., "modrinth", "discord").
     * @return The social link URL or null if not found.
     */
    public static String getSocialLink(String key) {
        return FabricLoader.getInstance()
                .getModContainer(Skyplus.NAMESPACE)
                .map(ModContainer::getMetadata)
                .map(metadata -> metadata.getCustomValue("socialLinks"))
                .map(CustomValue::getAsObject)
                .map(object -> object.get(key))
                .map(CustomValue::getAsString)
                .orElse("");
    }
}
