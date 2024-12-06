package ru.otus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.util.ReflectionHelper;

public class TestRunner {
    private static final Logger log = LoggerFactory.getLogger(TestRunner.class);

    private TestRunner() {}

    public static void run(Class<?> cl) {
        List<Method> beforeMethods = getBeforeMethods(cl.getMethods());
        List<Method> afterMethods = getAfterMethods(cl.getMethods());
        List<Method> testMethods = getTestMethods(cl.getMethods());

        int failed = 0;
        for (Method testMethod : testMethods) {
            Object testInstance = ReflectionHelper.instantiate(cl);
            beforeMethods.forEach(it -> ReflectionHelper.callMethod(testInstance, it.getName()));
            try {
                ReflectionHelper.callMethod(testInstance, testMethod.getName());
                log.info("{} is OK", testMethod.getName());
            } catch (Exception e) {
                if (e.getCause() instanceof InvocationTargetException invocationTargetException)
                    log.error(
                            "{} failed: {}",
                            testMethod.getName(),
                            invocationTargetException.getTargetException().toString());
                else {
                    log.error("{} failed: {}", testMethod.getName(), e.toString());
                }
                failed++;
            }
            afterMethods.forEach(it -> ReflectionHelper.callMethod(testInstance, it.getName()));
        }

        log.info("Tests run: {}. Passed: {}. Failed: {}", testMethods.size(), testMethods.size() - failed, failed);
    }

    private static List<Method> getBeforeMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(it -> it.isAnnotationPresent(Before.class))
                .toList();
    }

    private static List<Method> getAfterMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(it -> it.isAnnotationPresent(After.class))
                .toList();
    }

    private static List<Method> getTestMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(it -> it.isAnnotationPresent(Test.class))
                .toList();
    }
}
