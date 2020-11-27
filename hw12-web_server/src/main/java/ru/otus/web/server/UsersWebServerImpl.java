package ru.otus.web.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.entities.PhoneDataSet;
import ru.otus.entities.User;
import ru.otus.helpers.FileHelper;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.services.AdminUserDaoImpl;
import ru.otus.services.AdminUserService;
import ru.otus.services.AdminUserServiceImp;
import ru.otus.web.servelets.AuthorizationFilter;
import ru.otus.web.servelets.EditUserServelet;
import ru.otus.web.servelets.LoginServlet;
import ru.otus.web.servelets.UsersListServelet;
import ru.otus.web.services.TemplateProcessor;
import ru.otus.web.services.UserAuthService;
import ru.otus.web.services.UserAuthServiceImpl;

import java.util.Arrays;

public class UsersWebServerImpl implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String WEB_TOOLS_RESOURCES_DIR = "web_tools";

    private final UserAuthService userAuthService;
    private final TemplateProcessor templateProcessor;
    private final AdminUserService userService;
    private final Server server;

    public UsersWebServerImpl(int port, SessionManagerHibernate sessionManager, TemplateProcessor templateProcessor) {
        this.userService = new AdminUserServiceImp(new AdminUserDaoImpl(sessionManager));
        initAdminData();
        this.userAuthService =  new UserAuthServiceImpl(this.userService);
        this.templateProcessor = templateProcessor;
        this.server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if(this.server.getHandlers().length == 0){
            initContext();
        }
        this.server.start();
    }

    @Override
    public void join() throws Exception {
        this.server.join();
    }

    @Override
    public void stop() throws Exception {
        this.server.stop();
    }

    private Server initContext(){
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(resourceHandler);
        handlerList.addHandler(applySecurity(servletContextHandler,"/users","/api/user"));
        handlerList.addHandler(createResourceHandlerList());

        this.server.setHandler(handlerList);
        return this.server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths){
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(this.userAuthService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler(){
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler(){
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersListServelet(templateProcessor,userService)),"/users");
        servletContextHandler.addServlet(new ServletHolder(new EditUserServelet(templateProcessor,userService)),"/edit");
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandlerList(){
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setResourceBase(WEB_TOOLS_RESOURCES_DIR);
        return resourceHandler;
    }


    private void initAdminData(){
        User adminUser = new User("Administrator", "admin", "admin");
        adminUser.setPhones(Arrays.asList(new PhoneDataSet("(495) 937-99-92")));
        userService.create(adminUser);
    }
}
