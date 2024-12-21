package com.hazem.skyplus.annotations;

import com.hazem.skyplus.Skyplus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InitProcessor {
    private static final Logger logger = LoggerFactory.getLogger(InitProcessor.class);
    private static final String PACKAGE_NAME = "com.hazem." + Skyplus.NAMESPACE;

    public static void process() {
        List<AnnotatedMethod> initMethods = new ArrayList<>();
        List<Class<?>> classes = ClassScanner.findClasses(PACKAGE_NAME);

        if (classes.isEmpty()) return;

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Init.class)) {
                    if (method.getParameterCount() == 0 &&
                            (method.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0) {
                        Init annotation = method.getAnnotation(Init.class);
                        initMethods.add(new AnnotatedMethod(method, annotation.priority(), annotation.ordinal()));
                    } else {
                        logger.error("Method {} in {} is annotated with @Init but is not static or has parameters.",
                                method.getName(), clazz.getName());
                    }
                }
            }
        }

        // Sort methods based on priority and ordinal
        initMethods.sort(Comparator
                .comparing((AnnotatedMethod m) -> m.priority)
                .thenComparingInt(m -> m.ordinal));

        // Invoke the methods in the sorted order
        for (AnnotatedMethod annotatedMethod : initMethods) {
            try {
                annotatedMethod.method.setAccessible(true);
                annotatedMethod.method.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("Failed to invoke @Init method: {} in {}: {}",
                        annotatedMethod.method.getName(),
                        annotatedMethod.method.getDeclaringClass().getName(),
                        e.getMessage(), e);
            }
        }
    }

    private record AnnotatedMethod(Method method, Init.Priority priority, int ordinal) {}
}
