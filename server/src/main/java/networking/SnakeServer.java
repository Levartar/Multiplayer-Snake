package networking;

import networking.requests.CreateLobby;
import networking.requests.GetGameInfo;
import networking.requests.SelectMap;
import networking.requests.StartLobby;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;

import java.util.Objects;

public class SnakeServer {
    private static final Logger log = LogManager.getLogger(SnakeServer.class);

    private final Server server;

    public SnakeServer(int port) {
        server = new Server(port);
        Connector connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");

        log.debug("configuring websockets...");
        // websocket configuration
        JavaxWebSocketServletContainerInitializer.configure(contextHandler, ((servletContext, serverContainer) -> {
            serverContainer.setDefaultMaxTextMessageBufferSize(69);
            // timeout for websockets (in milliseconds I think)
            serverContainer.setDefaultMaxSessionIdleTimeout(3600000);
            serverContainer.addEndpoint(Endpoint.class);
        }));

        log.debug("configuring static file serving...");
        // static webpage configuration
        ServletHolder defaultHolder = new ServletHolder("default", new DefaultServlet());
        String resourceBase = Objects.requireNonNull(this.getClass()
                        .getClassLoader()
                        .getResource("client"))
                .toExternalForm();
        log.debug("static files resource base = " + resourceBase);
        contextHandler.setResourceBase(resourceBase);
        defaultHolder.setInitParameter("resourceBase", resourceBase);
        contextHandler.addServlet(defaultHolder, "/*");

        log.debug("configuring HTTP Requests...");

        // get request on /create
        ServletHolder createServlet = new ServletHolder(new CreateLobby());
        contextHandler.addServlet(createServlet, "/create");

        // get request on /create
        ServletHolder getGameInfoServlet = new ServletHolder(new GetGameInfo());
        contextHandler.addServlet(getGameInfoServlet, "/game-info");

        //post request on /start
        ServletHolder postGameStartServlet = new ServletHolder(new StartLobby());
        contextHandler.addServlet(postGameStartServlet, "/start");

        //post request on /select-map
        ServletHolder postSelectedMap = new ServletHolder(new SelectMap());
        contextHandler.addServlet(postSelectedMap, "/select-map");

//        //get request on /highscores
//        ServletHolder getHighscores = new ServletHolder(new GetHighscores());
//        contextHandler.addServlet(getHighscores, "/highscores");
    }

    public void run() {
        try {
            server.start();
            log.info("Server started successfully");
            server.join();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Server getServer() {
        return server;
    }
}
