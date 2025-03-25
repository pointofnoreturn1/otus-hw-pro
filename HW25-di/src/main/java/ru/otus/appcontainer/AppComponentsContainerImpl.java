package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Class<AppComponent> APP_COMPONENT_ANNOTATION_CLASS = AppComponent.class;
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        Arrays.stream(initialConfigClass).forEach(this::checkConfigClass);
        Arrays.stream(initialConfigClass)
                .filter(m -> m.isAnnotationPresent(APP_COMPONENT_ANNOTATION_CLASS))
                .sorted(Comparator.comparingInt(config -> config.getAnnotation(APP_COMPONENT_ANNOTATION_CLASS).order()))
                .forEach(this::processConfig);
    }

    public AppComponentsContainerImpl(String packageName) {
        new Reflections(packageName).getTypesAnnotatedWith(APP_COMPONENT_ANNOTATION_CLASS).forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        var appComponentMethods = getAppComponentsMethods(configClass).stream()
                .sorted(Comparator.comparingInt(m -> m.getDeclaredAnnotation(APP_COMPONENT_ANNOTATION_CLASS).order()))
                .toList();

        for (var method : appComponentMethods) {
            var componentName = method.getDeclaredAnnotation(APP_COMPONENT_ANNOTATION_CLASS).name();

            if (appComponentsByName.containsKey(componentName)) {
                throw new RuntimeException("A component named " + componentName + " already exists in the context");
            }

            var component = instantiate(method, configClass, getComponentParameters(method.getParameters()));

            appComponents.add(component);
            appComponentsByName.put(componentName, component);
        }
    }

    private Object instantiate(Method method, Class<?> configClass, Object... parameters) {
        try {
            return method.invoke(configClass.getConstructor().newInstance(), parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getComponentParameters(Parameter[] methodParameters) {
        var componentParameters = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            componentParameters[i] = getAppComponent(methodParameters[i].getType());
        }

        return componentParameters;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private List<Method> getAppComponentsMethods(Class<?> cl) {
        return Arrays.stream(cl.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(APP_COMPONENT_ANNOTATION_CLASS))
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        var suitableComponents = appComponents.stream()
                .filter(c -> componentClass.isAssignableFrom(c.getClass()))
                .toList();

        if (suitableComponents.isEmpty()) {
            throw new RuntimeException("No components found for class " + componentClass.getName());
        }

        if (suitableComponents.size() > 1) {
            throw new RuntimeException("Multiple components found for class " + componentClass.getName());
        }

        for (var component : appComponents) {
            if (componentClass.isInstance(component)) {
                return (C) component;
            }
        }

        throw new RuntimeException("The component is not found in the context");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C) Optional.ofNullable(appComponentsByName.get(componentName))
                .orElseThrow(() -> new RuntimeException("The component " + componentName + " is not found in the context"));
    }
}
