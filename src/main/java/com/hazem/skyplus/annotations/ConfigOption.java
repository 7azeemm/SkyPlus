package com.hazem.skyplus.annotations;

import com.hazem.skyplus.config.ConfigType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigOption {
    ConfigType type() default ConfigType.AUTO;
    String name();
    String description() default "";
}