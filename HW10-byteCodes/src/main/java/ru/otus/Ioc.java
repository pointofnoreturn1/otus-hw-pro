package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ioc {
    private static final Logger log = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    public static TestLoggingInterface createTestLogging() {
        InvocationHandler handler = new LogInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    private static class LogInvocationHandler implements InvocationHandler {
        private final Object target;
        private final Map<String, List<Method>> methods = new HashMap<>();

        public LogInvocationHandler(Object target) {
            this.target = target;
            for (Method method : target.getClass().getDeclaredMethods()) {
                this.methods.computeIfAbsent(method.getName(), k -> new ArrayList<>());
                this.methods.get(method.getName()).add(method);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Optional<Method> targetMethod = methods.get(method.getName()).stream()
                    .filter(LogInvocationHandler::hasAnnotation)
                    .filter(it -> haveSameSignature(it, method))
                    .findFirst();

            if (targetMethod.isPresent()) {
                String strArgs = Arrays.toString(args).replace("[", "").replace("]", "");
                log.info("Executed method: {}, param: {}", method.getName(), strArgs);
            }

            return method.invoke(target, args);
        }

        private static boolean hasAnnotation(Method method) {
            return method.isAnnotationPresent(Log.class);
        }

        private static boolean haveSameSignature(Method m1, Method m2) {
            if (m1.getParameterCount() != m2.getParameterCount()) {
                return false;
            }

            for (int i = 0; i < m1.getParameterCount(); i++) {
                if (!m1.getParameterTypes()[i].equals(m2.getParameterTypes()[i])) {
                    return false;
                }
            }

            return true;
        }
    }
}
