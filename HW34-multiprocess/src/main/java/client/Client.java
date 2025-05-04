package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.NumbersRequest;
import ru.otus.grpc.NumbersServiceGrpc;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final String HOST = "localhost";
    private static final int PORT = 8090;

    private static final int FIRST_VALUE = 0;
    private static final int LAST_VALUE = 30;
    private static final int THRESHOLD = 50;

    private final NumbersServiceGrpc.NumbersServiceStub stub;
    private final ClientStreamObserver responseStreamObserver;
    private final ManagedChannel channel;

    public static void main(String[] args) {
        var request = NumbersRequest.newBuilder()
                .setStart(FIRST_VALUE)
                .setEnd(LAST_VALUE)
                .build();
        new Client().start(request);
    }

    public Client() {
        channel = ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();
        stub = NumbersServiceGrpc.newStub(channel);
        responseStreamObserver = new ClientStreamObserver();
    }

    private void start(NumbersRequest rangeRequest) {
        logger.info("numbers Client is starting...");

        var counter = new AtomicInteger(0);
        var currentValue = new AtomicInteger(0);
        var executor = Executors.newScheduledThreadPool(1);

        stub.getNumbers(rangeRequest, responseStreamObserver);

        Runnable task = () -> {
            if (counter.incrementAndGet() == THRESHOLD) {
                channel.shutdown();
                executor.shutdown();
            }

            var value = currentValue.getAndIncrement() + responseStreamObserver.getCurrentValueAndReset() + 1;
            currentValue.set(value);
            logger.info("currentValue:{}", value);
        };
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }
}
