package networking;

import logic.Map;
import logic.Player;
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
        Thread.sleep(5000); // wait
        LobbyManager.closeAllLobbies();
        server.stop();
    }

    @Test
    void joinLobby() throws Exception {
        int lobbyJoinCode = LobbyManager.createLobby();
        Lobby lobby = LobbyManager.getLobby(lobbyJoinCode);
        String name = "schneck";

        String destination = "ws://localhost:80/join/" + lobbyJoinCode + "/name/" + name;
        ClientEndpoint clientEndpoint = new ClientEndpoint();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(clientEndpoint, new URI(destination));

        assertEquals(name, lobby.getPlayers().get(0).getName());
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
        String reason = clientEndpoint.getCloseReason().getReasonPhrase();
        assertEquals("No matching Lobby found for joinCode: 5", reason);

    }

    @Test
    void joinLobbyAndSentInput() throws Exception {
        int lobbyJoinCode = LobbyManager.createLobby();
        Lobby lobby = LobbyManager.getLobby(lobbyJoinCode);

        String destination = "ws://localhost:80/join/" + lobbyJoinCode + "/name/schneck";
        ClientEndpoint clientEndpoint = new ClientEndpoint();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(clientEndpoint, new URI(destination));

        char input = 'w';
        clientEndpoint.send(input);
        input = 'a';
        clientEndpoint.send(input);
        input = 's';
        clientEndpoint.send(input);
        input = 'd';
        clientEndpoint.send(input);

        Thread.sleep(1000); // wait 1 second

        try {
            Player player = lobby.getPlayers().get(0);
            assertEquals(input, player.getInput());
        } catch (IndexOutOfBoundsException e) {
            fail("Player not found");
        }
    }

    @Test
    void joinLobbyAndReceiveData() throws Exception {
        int lobbyJoinCode = LobbyManager.createLobby();
        Lobby lobby = LobbyManager.getLobby(lobbyJoinCode);

        String destination = "ws://localhost:80/join/" + lobbyJoinCode + "/name/schneck";
        ClientEndpoint clientEndpoint = new ClientEndpoint();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(clientEndpoint, new URI(destination));

        lobby.setGamemode("basic_snake");
        lobby.start();

        Thread.sleep(10000); // wait

        assertNotNull(clientEndpoint.getLastMessage());
    }
}
