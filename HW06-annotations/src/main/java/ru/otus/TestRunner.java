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

@SuppressWarnings("java:S2629")
public class TestRunner {
    private static final Logger log = LoggerFactory.getLogger(TestRunner.class);

    private TestRunner() {}

    public static void run(Class<?> cl) {
        List<Method> testMethods = getTestMethods(cl.getMethods());
        int failed = 0;
        for (Method testMethod : testMethods) {
            Object testInstance = ReflectionHelper.instantiate(cl);
            String methodName = testMethod.getName();
            processBeforeOrAfterMethods(testInstance, getBeforeMethods(cl.getMethods()));
            try {
                ReflectionHelper.callMethod(testInstance, methodName);
            } catch (Exception e) {
                logError(testMethod, e);
                failed++;
            }
            processBeforeOrAfterMethods(testInstance, getAfterMethods(cl.getMethods()));

            log.info("{} is OK", methodName);
        }

        log.info("Tests run: {}. Passed: {}. Failed: {}", testMethods.size(), testMethods.size() - failed, failed);
    }

    private static void processBeforeOrAfterMethods(Object testInstance, List<Method> methods) {
        methods.forEach(it -> {
            try {
                ReflectionHelper.callMethod(testInstance, it.getName());
            } catch (Exception e) {
                logError(it, e);
                System.exit(255);
            }
        });
    }

    private static void logError(Method method, Exception e) {
        if (e.getCause() instanceof InvocationTargetException invocationTargetException)
            log.info(
                    "ERROR! {} failed: {}",
                    method.getName(),
                    invocationTargetException.getTargetException().toString());
        else {
            log.info("ERROR! {} failed: {}", method.getName(), e.toString());
        }
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
