package ru.otus.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

class ProcessorExceptionAtEvenSecondTest {
    @Test
    void theSecondIsOddTest() {
        var expected = new Message.Builder(1L).field1("field1").build();
        var proc = new ProcessorExceptionAtEvenSecond(() -> LocalDateTime.now().withSecond(1));

        var actual = proc.process(expected);

        assertEquals(expected, actual);
        assertEquals(expected.getField1(), actual.getField1());
    }

    @Test
    void theSecondIsEvenTest() {
        var message = new Message.Builder(1L).field1("field1").build();
        var proc = new ProcessorExceptionAtEvenSecond(() -> LocalDateTime.now().withSecond(2));

        var exception = assertThrows(RuntimeException.class, () -> proc.process(message));

        assertEquals("The second is even", exception.getMessage());
    }
}
