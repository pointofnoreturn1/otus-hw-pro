package ru.otus;

public class Main {
    public static void main(String[] args) {
        Ioc.createTestLogging().calculation(42);
        Ioc.createTestLogging().calculation(42, 43);
    }
}
