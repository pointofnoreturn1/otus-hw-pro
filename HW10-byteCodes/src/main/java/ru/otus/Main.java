package ru.otus;

public class Main {
    public static void main(String[] args) {
        TestLoggingInterface instance = Ioc.createTestLogging();
        instance.calculation(42);
        instance.calculation(42, 43);
        instance.calculation(44.0, 45);
    }
}
