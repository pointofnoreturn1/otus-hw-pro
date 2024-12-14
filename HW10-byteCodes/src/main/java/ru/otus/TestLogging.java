package ru.otus;

public class TestLogging implements TestLoggingInterface {
    @Override
    @Log
    public void calculation(int param) {
        // left empty intentionally
    }

    @Override
    @Log
    public void calculation(int param1, int param2) {
        // left empty intentionally
    }

    @Override
    @Log
    public void calculation(double param1, int param2) {
        // left empty intentionally
    }
}
