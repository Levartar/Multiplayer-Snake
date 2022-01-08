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

import static org.junit.jupiter.api.Assertions.*;

public class WebsocketTest {

    private static Server server;

    @BeforeAll
    static void beforeAll() throws InterruptedException {
        Thread serverThread = new Thread(() -> {
            SnakeServer snakeServer = new SnakeServer(80);
            server = snakeServer.getServer();
            snakeServer.run();
        });
        serverThread.start();
        Thread.sleep(5000); // wait 5 seconds
    }

    @AfterAll
    static void afterAll() throws Exception {
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

    @Test
    void joinNonExistingLobby() throws InterruptedException, URISyntaxException, DeploymentException, IOException {
        int lobbyJoinCode = 5;

        // try to join a lobby that does not exist
        String destination = "ws://localhost:80/join/" + lobbyJoinCode + "/name/schneck";
        ClientEndpoint clientEndpoint = new ClientEndpoint();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(clientEndpoint, new URI(destination));

        Thread.sleep(1000); // wait 1 second

        assertFalse(clientEndpoint.isOpen());
    }
}
