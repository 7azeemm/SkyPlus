package com.hazem.skyplus.config;

import com.hazem.skyplus.config.controllers.ButtonOption;

import java.util.*;

public enum ConfigType {
    AUTO,
    TOGGLE(boolean.class, Boolean.class),
    SLIDER(int.class, Integer.class, float.class, Float.class),
    INT(int.class, Integer.class),
    FLOAT(float.class, Float.class),
    BUTTON(ButtonOption.class),
    DROPDOWN();

    private static final Map<Class<?>, ConfigType> TYPE_LOOKUP = new HashMap<>();

    static {
        // Populate the lookup map for quick access
        for (ConfigType type : values()) {
            for (Class<?> associatedType : type.associatedTypes) {
                TYPE_LOOKUP.put(associatedType, type);
            }
        }
    }

    private final Set<Class<?>> associatedTypes;

    ConfigType(Class<?>... types) {
        this.associatedTypes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(types)));
    }

    public static ConfigType fromFieldType(Class<?> fieldType) {
        ConfigType type = TYPE_LOOKUP.get(fieldType);
        if (type != null) {
            return type;
        }
        if (fieldType.isEnum()) return DROPDOWN;
        throw new IllegalArgumentException("Cannot infer ConfigType for field type: " + fieldType.getName());
    }
}