package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronizedCounter {
    private static final Logger logger = LoggerFactory.getLogger(SynchronizedCounter.class);

    private final int initValue;
    private final int lastValue;
    private String currentThreadTurn = "Thread-0";
    private boolean reverse;

    public SynchronizedCounter(int initValue, int lastValue) {
        this.initValue = initValue;
        this.lastValue = lastValue;
    }

    public static void main(String[] args) {
        var counter = new SynchronizedCounter(1, 10);
        new Thread(() -> counter.count(counter.initValue), "Thread-0").start();
        new Thread(() -> counter.count(counter.initValue), "Thread-1").start();
    }

    private synchronized void count(int idx) {
        while (!Thread.currentThread().isInterrupted()) {
            var threadName = Thread.currentThread().getName();
            try {
                while (!threadName.equals(currentThreadTurn)) {
                    this.wait();
                }
                checkAndChangeOrder(idx);
                logger.info(String.valueOf(reverse ? idx-- : idx++));
                currentThreadTurn = threadName.equals("Thread-0") ? "Thread-1" : "Thread-0";
                this.notifyAll();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void checkAndChangeOrder(int idx) {
        if (idx == lastValue) reverse = true;
        if (idx == initValue) reverse = false;
    }
}
