package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

@SuppressWarnings("java:S127")
public class MyTests {

    @Before
    public void beforeEach() {
        for (int i = 0; i < 10; i++) {
            i = i + 1;
        }
    }

    @After
    public void afterEach() {
        for (int i = 0; i < 10; i++) {
            i = i + 1;
        }
    }

    @Test
    public void positiveTest1() {
        for (int i = 0; i < 10; i++) {
            i = i + 1;
        }
    }

    @Test
    public void negativeTest() {
        throw new ArithmeticException("/ by zero");
    }

    @Test
    public void positiveTest2() {
        for (int i = 0; i < 10; i++) {
            i = i + 1;
        }
    }

    @Test
    public void positiveTest3() {
        for (int i = 0; i < 10; i++) {
            i = i + 1;
        }
    }
}
