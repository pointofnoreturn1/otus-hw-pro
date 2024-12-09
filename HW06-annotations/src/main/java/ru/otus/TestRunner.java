package ru.otus;

import static ru.otus.util.ReflectionHelper.callMethod;

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

@SuppressWarnings("java:S135")
public class TestRunner {
    private static final Logger log = LoggerFactory.getLogger(TestRunner.class);

    private TestRunner() {}

    public static void run(Class<?> cl) {
        Method[] methods = cl.getMethods();
        List<Method> testMethods = getTestMethods(methods);
        int failedTests = 0;
        for (Method testMethod : testMethods) {
            Object testInstance = ReflectionHelper.instantiate(cl);
            String testMethodName = testMethod.getName();

            boolean beforeFailed = processBeforeOrAfterMethods(testInstance, testMethodName, getBeforeMethods(methods));
            if (beforeFailed) continue;

            try {
                callMethod(testInstance, testMethodName);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                log.info("ERROR! {} failed: {}", testMethodName, e.getCause().toString());
                failedTests++;
            }

            boolean afterFailed = processBeforeOrAfterMethods(testInstance, testMethodName, getAfterMethods(methods));
            if (afterFailed) continue;

            log.info("{} is OK", testMethodName);
        }

        log.info(
                "Tests run: {}. Passed: {}. Failed: {}",
                testMethods.size(),
                testMethods.size() - failedTests,
                failedTests);
    }

    private static boolean processBeforeOrAfterMethods(
            Object testInstance, String testMethodName, List<Method> methods) {
        for (Method method : methods) {
            try {
                callMethod(testInstance, method.getName());
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                log.info(
                        "ERROR! {} failed, error occurred in {}: {}",
                        testMethodName,
                        method.getName(),
                        e.getCause().toString());
                return true;
            }
        }

        return false;
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
