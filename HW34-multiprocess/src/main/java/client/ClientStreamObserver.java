package client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.NumberResponse;

public class ClientStreamObserver implements StreamObserver<NumberResponse> {
    private static final Logger logger = LoggerFactory.getLogger(ClientStreamObserver.class);

    private int currentValue;

    @Override
    public void onNext(NumberResponse numberResponse) {
        setCurrentValue(numberResponse.getValue());
        logger.info("newValue:{}", currentValue);
    }

    @Override
    public void onError(Throwable throwable) {
        logger.error("error", throwable);
    }

    @Override
    public void onCompleted() {
        logger.info("request completed");
    }

    public synchronized int getCurrentValue() {
        var pervValue = this.currentValue;
        this.currentValue = 0;

        return pervValue;
    }

    private synchronized void setCurrentValue(int value) {
        this.currentValue = value;
    }
}
