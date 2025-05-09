package com.hazem.skyplus.config;

import com.hazem.skyplus.annotations.ConfigCategory;
import com.hazem.skyplus.annotations.ConfigOption;
import com.hazem.skyplus.annotations.ConfigSubCategory;
import com.hazem.skyplus.config.controllers.BooleanController;
import com.hazem.skyplus.config.controllers.ButtonController;
import com.hazem.skyplus.config.controllers.ButtonOption;
import com.hazem.skyplus.config.controllers.DropdownController;
import com.hazem.skyplus.config.gui.Category;
import com.hazem.skyplus.config.gui.Option;
import com.hazem.skyplus.config.gui.SubCategory;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for processing configuration categories, subcategories, and options
 * from annotated fields within the {@link SkyPlusConfig} class.
 * This class creates structured categories and options for a configuration GUI based on
 * annotations like {@link ConfigCategory}, {@link ConfigOption}, and {@link ConfigSubCategory}.
 */
public class ConfigProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProcessor.class);

    public static List<Category> createCategoriesFromConfig(SkyPlusConfig config) {
        List<Category> categories = new ArrayList<>();
        for (Field field : SkyPlusConfig.class.getDeclaredFields()) {
            if (field.getType().isAnnotationPresent(ConfigCategory.class)) {
                ConfigCategory categoryAnnotation = field.getType().getAnnotation(ConfigCategory.class);
                String categoryName = categoryAnnotation.name();

                field.setAccessible(true);
                try {
                    Object subConfig = field.get(config);
                    categories.add(createCategory(categoryName, subConfig));
                } catch (IllegalAccessException e) {
                    LOGGER.error("Error creating category for field: {}", field.getName(), e);
                }
            }
        }
        return categories;
    }

    private static Category createCategory(String categoryName, Object categoryInstance) {
        List<Option> categoryOptions = new ArrayList<>();
        List<SubCategory> subCategories = new ArrayList<>();

        for (Field field : categoryInstance.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(ConfigOption.class)) {
                processOption(field, categoryInstance, categoryOptions);
            } else if (field.isAnnotationPresent(ConfigSubCategory.class)) {
                processSubCategory(field, categoryInstance, subCategories);
            }
        }

        return new Category(Text.literal(categoryName), subCategories, categoryOptions);
    }

    private static void processOption(Field field, Object categoryInstance, List<Option> categoryOptions) {
        ConfigOption optionAnnotation = field.getAnnotation(ConfigOption.class);
        String name = optionAnnotation.name();
        String description = optionAnnotation.description();
        ConfigType type = optionAnnotation.type() == ConfigType.AUTO ? ConfigType.fromFieldType(field.getType()) : optionAnnotation.type();

        try {
            Object initialValue = field.get(categoryInstance);
            categoryOptions.add(createOption(type, name, description, field, categoryInstance, initialValue, optionAnnotation));
        } catch (IllegalAccessException e) {
            LOGGER.error("Failed to process option: {}", field.getName(), e);
        }
    }

    private static void processSubCategory(Field field, Object categoryInstance, List<SubCategory> subCategories) {
        ConfigSubCategory subCategoryAnnotation = field.getAnnotation(ConfigSubCategory.class);
        String subCategoryName = subCategoryAnnotation.name();

        try {
            Object subCategoryInstance = field.get(categoryInstance);
            if (subCategoryInstance != null) {
                List<Option> subCategoryOptions = new ArrayList<>();
                for (Field subField : subCategoryInstance.getClass().getDeclaredFields()) {
                    subField.setAccessible(true);
                    if (subField.isAnnotationPresent(ConfigOption.class)) {
                        processOption(subField, subCategoryInstance, subCategoryOptions);
                    }
                }
                subCategories.add(new SubCategory(Text.literal(subCategoryName), subCategoryOptions));
            }
        } catch (IllegalAccessException e) {
            LOGGER.error("Failed to process subcategory: {}", field.getName(), e);
        }
    }

    private static Option createOption(ConfigType type, String name, String description, Field field, Object instance, Object initialValue, ConfigOption optionAnnotation) {
        return switch (type) {
            case TOGGLE -> new Option(
                    Text.literal(name),
                    Text.literal(description),
                    new BooleanController(
                            newValue -> setFieldValue(field, instance, newValue),
                            (boolean) initialValue
                    )
            );
            case DROPDOWN -> {
                @SuppressWarnings("unchecked")
                Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) field.getType();
                Enum<?>[] enumConstants = enumClass.getEnumConstants();

                yield new Option(
                        Text.literal(name),
                        Text.literal(description),
                        new DropdownController(
                                newValue -> setFieldValue(field, instance, newValue),
                                (Enum<?>) initialValue,
                                enumConstants
                        )
                );
            }
            case BUTTON -> new Option(
                    Text.literal(name),
                    Text.literal(description),
                    new ButtonController(
                            ((ButtonOption) initialValue).name(),
                            ((ButtonOption) initialValue).action()
                    )
            );
            default -> throw new IllegalArgumentException("Unsupported config type: " + type);
        };
    }

    private static void setFieldValue(Field field, Object instance, Object newValue) {
        try {
            field.set(instance, newValue);
        } catch (IllegalAccessException e) {
            LOGGER.error("Failed to set value for field: {}", field.getName(), e);
        }
    }
}