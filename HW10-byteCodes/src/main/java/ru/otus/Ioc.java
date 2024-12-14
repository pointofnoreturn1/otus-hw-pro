package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        private final TestLoggingInterface target;
        private final Map<String, Method> methods = new HashMap<>();

        public LogInvocationHandler(TestLoggingInterface target) {
            this.target = target;
            for (Method method : target.getClass().getDeclaredMethods()) {
                this.methods.put(method.getName(), method);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methods.get(method.getName()).isAnnotationPresent(Log.class)) {
                String strArgs = Arrays.toString(args).replace("[", "").replace("]", "");
                log.info("Executed method: {}, param: {}", method.getName(), strArgs);
            }
            return method.invoke(target, args);
        }
    }
}
