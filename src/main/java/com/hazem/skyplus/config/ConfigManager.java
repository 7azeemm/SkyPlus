package com.hazem.skyplus.config;

import com.google.gson.FieldNamingPolicy;
import com.hazem.skyplus.Skyplus;
import com.hazem.skyplus.config.categories.GardenCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

import java.nio.file.Path;

public class ConfigManager {
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("skyplus.json");
    private static final ConfigClassHandler<SkyPlusConfig> HANDLER = ConfigClassHandler.createBuilder(SkyPlusConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_FILE)
                    .setJson5(false)
                    .appendGsonBuilder(builder -> builder
                            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                            .registerTypeHierarchyAdapter(Identifier.class, new Identifier.Serializer()))
                    .build())
            .build();

    public static SkyPlusConfig getHandler() {
        return HANDLER.instance();
    }

    public static void init() {
        HANDLER.load();
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal(Skyplus.NAMESPACE).then(ClientCommandManager.literal("config")
                .executes(context -> {
                    MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(createGUI(null)));
                    return 1;
                }
        )))));
    }

        public static Screen createGUI(Screen parent) {
        return YetAnotherConfigLib.create(HANDLER, (defaults, config, builder) -> {
            builder.title(Text.of("SkyPlus Config"))
                    .category(GardenCategory.create(defaults, config));
            return builder;
        }).generateScreen(parent);
    }
}
