package ru.otus;

public class TestLogging implements TestLoggingInterface {
    @Override
    @Log
    public void calculation(int param) {
        // left empty intentionally
    }

    @Override
    @Log
    public void calculation(int p1, int p2) {
        // left empty intentionally
    }
}
