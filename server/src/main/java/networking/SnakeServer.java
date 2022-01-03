package networking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;

public class SnakeServer {
    private static final Logger log = LogManager.getLogger(SnakeServer.class);

    private final Server server;

    public SnakeServer(int port) {
        server = new Server(port);
        Connector connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");

        // websocket configuration
        JavaxWebSocketServletContainerInitializer.configure(contextHandler, ((servletContext, serverContainer) -> {
            serverContainer.setDefaultMaxTextMessageBufferSize(69);
            // timeout for websockets (in milliseconds I think)
            serverContainer.setDefaultMaxSessionIdleTimeout(100000);
            serverContainer.addEndpoint(Endpoint.class);
        }));

        // static webpage configuration
        ServletHolder defaultHolder = new ServletHolder("default", new DefaultServlet());
        defaultHolder.setInitParameter("resourceBase", "./src/main/resources/client/");
        contextHandler.addServlet(defaultHolder, "/*");
    }

    public void run() {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
