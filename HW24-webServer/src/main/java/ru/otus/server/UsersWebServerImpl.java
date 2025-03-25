package ru.otus.server;

import com.google.gson.Gson;
import java.util.Arrays;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.ClientsApiServlet;
import ru.otus.servlet.ClientsServlet;
import ru.otus.servlet.LoginServlet;

public class UsersWebServerImpl implements UsersWebServer {
    private final UserAuthService authService;

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String LOGIN_PAGE = "/login";
    private static final String CLIENTS_PAGE = "/clients";
    private static final String CLIENTS_API = "/api/clients/*";

    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;
    private final DBServiceClient dbServiceClient;

    public UsersWebServerImpl(
            int port,
            UserAuthService authService,
            Gson gson,
            TemplateProcessor templateProcessor,
            DbServiceClientImpl dbServiceClient) {
        this.server = new Server(port);
        this.authService = authService;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(
                new ServletHolder(new LoginServlet(templateProcessor, authService)), LOGIN_PAGE);
        var authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));

        return servletContextHandler;
    }

    private void initContext() {
        var resourceHandler = createResourceHandler();
        var servletContextHandler = createServletContextHandler();

        var sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler, CLIENTS_PAGE));

        server.setHandler(sequence);
    }

    private ResourceHandler createResourceHandler() {
        var resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(START_PAGE_NAME);
        resourceHandler.setBaseResourceAsString(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));

        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        var servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(
                new ServletHolder(new ClientsServlet(templateProcessor, dbServiceClient)), "/clients/*");
        servletContextHandler.addServlet(new ServletHolder(new ClientsApiServlet(dbServiceClient, gson)), CLIENTS_API);

        return servletContextHandler;
    }
}
