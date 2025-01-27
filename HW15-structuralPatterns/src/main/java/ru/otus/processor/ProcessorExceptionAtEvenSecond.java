package ru.otus.processor;

import ru.otus.model.Message;

@SuppressWarnings("java:S112")
public class ProcessorExceptionAtEvenSecond implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorExceptionAtEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new RuntimeException("The second is even");
        }

        return message;
    }
}
