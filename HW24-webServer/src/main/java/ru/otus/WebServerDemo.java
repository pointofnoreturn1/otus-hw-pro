package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.server.UsersWebServerImpl;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthServiceImpl;

public class WebServerDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        var userDao = new InMemoryUserDao();
        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Phone.class, Address.class);

        var usersWebServer = getServer(sessionFactory, userDao, gson);
        usersWebServer.join();
    }

    private static UsersWebServerImpl getServer(SessionFactory sessionFactory, InMemoryUserDao userDao, Gson gson)
            throws Exception {
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var authService = new UserAuthServiceImpl(userDao);

        var usersWebServer =
                new UsersWebServerImpl(WEB_SERVER_PORT, authService, gson, templateProcessor, dbServiceClient);
        usersWebServer.start();

        return usersWebServer;
    }
}
