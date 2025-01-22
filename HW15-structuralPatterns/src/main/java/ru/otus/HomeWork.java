package ru.otus;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.*;

@SuppressWarnings("java:S1171")
public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        List<Processor> processors = List.of(
                new LoggerProcessor(new ProcessorSwapFields()),
                new LoggerProcessor(new ProcessorExceptionAtEvenSecond(LocalDateTime::now)));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var historyListener = new HistoryListener();
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(historyListener);
        complexProcessor.addListener(listenerPrinter);

        var field13 = new ObjectForMessage();
        field13.setData(List.of("testData"));
        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(historyListener);
        complexProcessor.removeListener(listenerPrinter);
    }
}
