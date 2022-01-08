package networking;

import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketTest {

    private static Server server;

    @BeforeAll
    static void beforeAll() {
        Thread serverThread = new Thread(() -> {
            SnakeServer snakeServer = new SnakeServer(80);
            server = snakeServer.getServer();
            snakeServer.run();
        });
        serverThread.start();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Endpoint.closeAllEndpoints();
        LobbyManager.closeAllLobbies();
        server.stop();
    }

    @Test
    void joinLobby() throws URISyntaxException, DeploymentException, IOException {
        int lobbyJoinCode = LobbyManager.createLobby();

        String destination = "ws://localhost:80/join/" + lobbyJoinCode + "/name/schneck";
        ClientEndpoint clientEndpoint = new ClientEndpoint();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(clientEndpoint, new URI(destination));
    }
}
