package server;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import ru.otus.grpc.NumberResponse;
import ru.otus.grpc.NumbersRequest;
import ru.otus.grpc.NumbersServiceGrpc;

public class NumberServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {
    @Override
    public void getNumbers(NumbersRequest request, StreamObserver<NumberResponse> responseObserver) {
        var currentValue = new AtomicInteger(request.getStart());
        var executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            var value = currentValue.incrementAndGet();
            responseObserver.onNext(NumberResponse.newBuilder().setValue(value).build());
            if (value == request.getEnd()) {
                executor.shutdown();
                responseObserver.onCompleted();
            }
        };
        executor.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
    }
}
