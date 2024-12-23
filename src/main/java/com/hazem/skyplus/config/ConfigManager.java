package com.hazem.skyplus.config;

import com.google.gson.FieldNamingPolicy;
import com.hazem.skyplus.Skyplus;
import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.config.categories.GardenCategory;
import com.hazem.skyplus.config.categories.MiscCategory;
import com.hazem.skyplus.utils.schedular.Scheduler;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
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

    @Init(priority = Init.Priority.HIGH, ordinal = 1)
    public static void init() {
        HANDLER.load();
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal(Skyplus.NAMESPACE).then(ClientCommandManager.literal("config")
                .executes(Scheduler.openScreen(() -> createGUI(null))
        )))));
    }

        public static Screen createGUI(Screen parent) {
        return YetAnotherConfigLib.create(HANDLER, (defaults, config, builder) -> {
            builder.title(Text.of("SkyPlus Config"))
                    .category(GardenCategory.create(defaults, config))
                    .category(MiscCategory.create(defaults, config));
            return builder;
        }).generateScreen(parent);
    }
}
