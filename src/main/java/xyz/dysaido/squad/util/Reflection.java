package xyz.dysaido.squad.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public class Reflection {

    private Reflection() {
        throw new RuntimeException();
    }

    public static Field getField(Class<?> target, Class<?> fieldType) {
        return getField(target, null, fieldType);
    }

    public static Field getField(Class<?> clazz, String name, Class<?> fieldType) {
        do {
            for (Field field : clazz.getDeclaredFields()) {
                if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType())) {
                    return setAccessible(field);
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
        throw new RuntimeException("Can't find field " + name + " in class " + clazz + " or it's superclasses");
    }

    @SuppressWarnings("unchecked")
    public static <T> T fetch(Object object, Field field) {
        try {
            return (T) setAccessible(field).get(object);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T extends AccessibleObject> T setAccessible(T object) {
        object.setAccessible(true);
        return object;
    }
}
