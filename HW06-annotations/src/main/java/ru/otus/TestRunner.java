package ru.otus;

import static ru.otus.util.ReflectionHelper.callMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
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
        Method[] classMethods = cl.getMethods();
        Map<Method, LinkedHashSet<Exception>> testMethods = new HashMap<>();

        for (Method testMethod : getTestMethods(classMethods)) {
            testMethods.put(testMethod, new LinkedHashSet<>());
            Object testInstance = ReflectionHelper.instantiate(cl);

            getBeforeMethods(classMethods)
                    .forEach(it -> processMethod(testInstance, it.getName(), testMethod, testMethods));

            var exceptions = testMethods.get(testMethod);
            if (!exceptions.isEmpty()) {
                continue;
            }

            processMethod(testInstance, testMethod.getName(), testMethod, testMethods);

            getAfterMethods(classMethods)
                    .forEach(it -> processMethod(testInstance, it.getName(), testMethod, testMethods));
        }

        printTestsResults(testMethods);
        int failed = countFailed(testMethods);
        log.info("Tests run: {}. Passed: {}. Failed: {}", testMethods.size(), testMethods.size() - failed, failed);
    }

    private static void processMethod(
            Object testInstance,
            String methodName,
            Method testMethod,
            Map<Method, LinkedHashSet<Exception>> testMethods) {
        try {
            callMethod(testInstance, methodName);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            var exceptions = testMethods.get(testMethod);
            exceptions.add(e);
        }
    }

    private static void printTestsResults(Map<Method, LinkedHashSet<Exception>> testMethods) {
        for (Map.Entry<Method, LinkedHashSet<Exception>> entry : testMethods.entrySet()) {
            String methodName = entry.getKey().getName();
            if (entry.getValue().isEmpty()) {
                log.info("{} is OK", methodName);
            } else {
                String exception = entry.getValue().getLast().getCause().toString();
                log.info("ERROR! {} failed: {}", methodName, exception);
            }
        }
    }

    private static int countFailed(Map<Method, LinkedHashSet<Exception>> testMethods) {
        return (int) testMethods.values().stream()
                .filter(Predicate.not(HashSet::isEmpty))
                .count();
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
