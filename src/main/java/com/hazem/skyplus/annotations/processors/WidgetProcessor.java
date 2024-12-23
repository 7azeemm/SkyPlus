package com.hazem.skyplus.annotations.processors;

import com.hazem.skyplus.Skyplus;
import com.hazem.skyplus.annotations.Init;
import com.hazem.skyplus.annotations.Widget;
import com.hazem.skyplus.utils.hud.AbstractWidget;
import com.hazem.skyplus.utils.hud.HUDMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class WidgetProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetProcessor.class);
    private static final String PACKAGE_NAME = "com.hazem." + Skyplus.NAMESPACE;

    @Init(priority = Init.Priority.MEDIUM)
    public static void process() {
        List<Class<?>> widgetClasses = new ArrayList<>();
        List<Class<?>> classes = ClassScanner.findClasses(PACKAGE_NAME);

        if (classes.isEmpty()) return;

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Widget.class)) {
                if (AbstractWidget.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                    widgetClasses.add(clazz);
                } else {
                    LOGGER.error("Class {} is annotated with @Widget but is either not a subclass of Widget or is abstract.", clazz.getName());
                }
            }
        }

        // Instantiate and register each widget
        for (Class<?> widgetClass : widgetClasses) {
            try {
                AbstractWidget widget = (AbstractWidget) widgetClass.getDeclaredConstructor().newInstance();
                HUDMaster.addWidget(widget);
            } catch (ReflectiveOperationException e) {
                LOGGER.error("Failed to instantiate widget: {}. Error: {}", widgetClass.getName(), e.getMessage(), e);
            }
        }
    }
}