package com.hazem.skyplus.annotations.processors;

import com.hazem.skyplus.Skyplus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClassScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanner.class);
    private static final String PACKAGE_NAME = "com.hazem." + Skyplus.NAMESPACE;
    private static final List<Class<?>> CLASSES = new ArrayList<>();

    public static void scan() {
        String path = PACKAGE_NAME.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

        if (resource == null) {
            LOGGER.warn("Package not found: {}", PACKAGE_NAME);
            return;
        }

        File root = new File(resource.getFile());
        if (!root.exists() || !root.isDirectory()) {
            LOGGER.warn("Invalid package directory: {}", root.getAbsolutePath());
            return;
        }

        try (Stream<Path> paths = Files.walk(root.toPath())) {
            paths.filter(Files::isRegularFile)
                    .filter(pathItem -> pathItem.toString().endsWith(".class"))
                    .forEach(pathItem -> {
                        String relativePath = root.toPath().relativize(pathItem).toString();
                        String className = PACKAGE_NAME + '.' + relativePath.replace(File.separator, ".").replace(".class", "");

                        // Skip mixin classes
                        if (className.contains("Mixin")) return;

                        try {
                            CLASSES.add(Class.forName(className));
                        } catch (Exception e) {
                            LOGGER.error("Unexpected error while loading class: {}", className, e);
                        }
                    });
        } catch (IOException e) {
            LOGGER.error("Error reading directory: {}", root.getAbsolutePath(), e);
        }
    }

    public static List<Class<?>> getClasses() {
        return CLASSES;
    }
}