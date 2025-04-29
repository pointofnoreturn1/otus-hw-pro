package server;

import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final int PORT = 8090;

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = ServerBuilder.forPort(PORT)
                .addService(new NumberServiceImpl())
                .build()
                .start();

        logger.info("Server started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Server shutdown requested");
            server.shutdown();
            logger.info("Server stopped");
        }));

        logger.info("Server is running on port {}", PORT);
        server.awaitTermination();
    }
}
