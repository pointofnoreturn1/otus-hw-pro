package ru.otus.appcontainer.api;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass);

    <C> Object getAppComponent(String componentName);
}
