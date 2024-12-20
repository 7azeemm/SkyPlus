package com.hazem.skyplus.annotations;

import com.hazem.skyplus.Skyplus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class InitProcessor {
    private static final Logger logger = LoggerFactory.getLogger(InitProcessor.class);
    private static final String PACKAGE_NAME = "com.hazem." + Skyplus.NAMESPACE;

    public static void process() {
        List<Class<?>> classes = ClassScanner.findClasses(PACKAGE_NAME);
        if (classes.isEmpty()) return;

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Init.class)) {
                    if (method.getParameterCount() == 0 &&
                            (method.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0) {
                        try {
                            method.setAccessible(true);
                            method.invoke(null);
                            logger.info("Successfully invoked @Init method: {} in {}", method.getName(), clazz.getName());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            logger.error("Failed to invoke @Init method: {} in {}: {}", method.getName(), clazz.getName(), e.getMessage(), e);
                        }
                    } else {
                        logger.error("Method {} in {} is annotated with @Init but is not static or has parameters.",
                                method.getName(), clazz.getName());
                    }
                }
            }
        }
    }
}