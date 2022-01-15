package networking;

import exceptions.GameOverException;
import logic.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint("/join/{code}/name/{name}")
public class Endpoint {
    private static final Logger log = LogManager.getLogger(Endpoint.class);

    private static List<Endpoint> endpoints = new ArrayList<>();

    private Session session;

    private Lobby lobby;
    private Player player;

    @OnOpen
    public void onOpen(@PathParam("code") int joinCode, @PathParam("name") String name, Session session) throws IOException {
        this.session = session;
        session.setMaxIdleTimeout(3600000);
        endpoints.add(this);

        try {
            lobby = LobbyManager.getLobby(joinCode);
        } catch (Exception e) {
            log.error(e.getMessage());
            // the client receives: onClose(1003, some text)
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, e.getMessage()));
        }

        player = new Player(name);

        try {
            lobby.join(this);
        } catch (Exception e) {
            log.error(e.getMessage());
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, e.getMessage()));
        }
    }

    @OnMessage
    public void onMessage(char input, Session session) {
        log.debug("input from player " + player.getName() + ": " + input);
        player.setInput(input);
    }

    @OnClose
    public void onClose(Session session) {
        endpoints.remove(this);
        log.info("Websocket connection with player " + player.getName() + " closed");
        lobby.removePlayer(this);
        lobby = null;
    }

    @OnError
    public void onError(Session session, Throwable cause) {
        log.error("Error in the Websocket connection with player " + player.getName() + ": " + cause.getMessage());
    }


    public Player getPlayer() {
        return player;
    }

    public void send(String data) {
        try {
            session.getBasicRemote().sendText(data);
            log.trace("synchronization message sent to player " + player.getName());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void closeAllEndpoints() {
        endpoints.forEach(endpoint -> {
            try {
                if (endpoint.session == null) {
                    log.warn("Trying to close endpoint, but endpoint.session is null");
                } else {
                    endpoint.session.close();
                }
            } catch (IOException e) {
                log.error("Error while closing endpoint: " + e.getMessage());
            }
        });
        log.warn("All Endpoints have been closed!");
    }
}
