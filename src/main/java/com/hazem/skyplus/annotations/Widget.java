package com.hazem.skyplus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a class is a widget, allowing it to be automatically
 * registered in the HUD system.
 * <p>
 * Classes annotated with {@code @Widget} must extend the {@link Widget} base class
 * and have a no-argument constructor. These classes will be instantiated and
 * registered automatically during the initialization process.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Widget {
}