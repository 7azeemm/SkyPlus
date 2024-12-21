package com.hazem.skyplus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method to be executed during initialization by the {@link InitProcessor}.
 * Methods annotated with {@code @Init} must be static and have no parameters.
 * Priority and ordinal values determine the execution order of methods.
 *
 * @see #ordinal()
 * @see #priority()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Init {

    /**
     * Defines the priority level of the method.
     * Higher priority methods are executed before lower priority ones.
     * Defaults to {@link Priority#LOW}.
     *
     * @return the priority level of the method
     */
    Priority priority() default Priority.LOW;

    /**
     * Defines the ordinal value within the priority level.
     * Methods with the same priority are executed in ascending order of their ordinal value.
     * Defaults to {@link Integer#MAX_VALUE}.
     *
     * @return the ordinal value of the method
     */
    int ordinal() default Integer.MAX_VALUE;

    enum Priority {
        HIGH,
        MEDIUM,
        LOW
    }
}