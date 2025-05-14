package ru.mehoil.beans;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * Instantiates the class marked as {@link AutoInjectable} using reflection.
 * Implementation is configured in the 'injector.properties' file.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class Injector {

    private final Properties properties;

    public Injector() {
        properties = new Properties();
        try (final var input = getClass().getClassLoader().getResourceAsStream("injector.properties")) {
            if (input == null) {
                throw new RuntimeException("Properties file 'injector.properties' not found in classpath");
            }
            properties.load(input);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    /**
     * Injects dependencies into the given object and its entire inner objects recursively.
     *
     * @param object The object to inject dependencies into.
     * @param <T>    The type of the object.
     */
    public <T> void inject(final T object) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot inject into a null object");
        }

        final Class<?> clazz = object.getClass();
        final Field[] fields = getAllFields(clazz);

        for (final Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                final Class<?> fieldType = field.getType();
                if (!fieldType.isInterface()) {
                    throw new RuntimeException("Field " + field.getName() + " annotated with @AutoInjectable must be an interface");
                }

                final String interfaceName = fieldType.getName();
                final String implName = properties.getProperty(interfaceName);
                if (implName == null || implName.trim().isEmpty()) {
                    throw new RuntimeException("No implementation specified for interface " + interfaceName + " in properties file");
                }

                try {
                    final Class<?> implClass = Class.forName(implName.trim());
                    final Object implInstance = implClass.getDeclaredConstructor().newInstance();
                    field.setAccessible(true);
                    field.set(object, implInstance);

                    inject(implInstance);
                } catch (final ClassNotFoundException e) {
                    throw new RuntimeException("Implementation class " + implName + " not found", e);
                } catch (final InstantiationException | IllegalAccessException |
                               NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException("Failed to instantiate " + implName + " for field " + field.getName(), e);
                }
            }
        }
    }

    /**
     * Retrieves all fields from the class and its superclasses.
     *
     * @param clazz The class to get fields from.
     * @return An array of all fields, including inherited ones.
     */
    private Field[] getAllFields(Class<?> clazz) {
        final var fields = new ArrayList<Field>();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }

}
