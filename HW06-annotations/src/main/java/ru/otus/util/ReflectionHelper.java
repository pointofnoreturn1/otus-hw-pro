package ru.otus.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@SuppressWarnings({"java:S3011", "java:S112"})
public class ReflectionHelper {
    private ReflectionHelper() {}

    public static Object callMethod(Object object, String name, Object... args)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var method = object.getClass().getDeclaredMethod(name, toClasses(args));
        method.setAccessible(true);
        return method.invoke(object, args);
    }

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
