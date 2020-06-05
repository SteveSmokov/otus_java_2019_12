package ru.otus;

import ru.otus.db.hibernate.HibernateUtils;
import ru.otus.db.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.web.server.UsersWebServer;
import ru.otus.web.server.UsersWebServerImpl;
import ru.otus.web.services.TemplateProcessor;
import ru.otus.web.services.TemplateProcessorImpl;

public class Main {
    private final static int WEB_SERVER_PORT = 8080;
    private final static String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(HibernateUtils.buildSessionFactory());
        TemplateProcessor processor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UsersWebServer usersWebServer = new UsersWebServerImpl(WEB_SERVER_PORT,
                sessionManager, processor);

        usersWebServer.start();
        usersWebServer.join();


    }
}
