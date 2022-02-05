package networking;

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

    /**
     * is executed after the client establishes the Websocket connection
     * sees if given lobby is available, then joins with the given name
     * @param joinCode of the chosen lobby
     * @param name of the player
     * @param session of the Websocket connection i.e. Client and Server
     * @throws IOException if lobby exists or is full/started
     */
    @OnOpen
    public void onOpen(@PathParam("code") int joinCode, @PathParam("name") String name, Session session) throws IOException {
        this.session = session;
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

    /**
     * is executed upon frontend uses send() method. relays the input to the Player Object
     * @param input from the keyboard of the websocket clientside
     * @param session of the Websocket connection i.e. Client and Server
     */
    @OnMessage
    public void onMessage(char input, Session session) {
        log.debug("input from player " + player.getName() + ": " + input);
        player.setInput(input);
    }

    /**
     * upon closing of the websocket connection, removes the endpoint from the open connections, removes the Player from the lobby and dismisses the lobby connection
     * @param session of the Websocket connection i.e. Client and Server
     */
    @OnClose
    public void onClose(Session session) {
        endpoints.remove(this);
        log.info("Websocket connection with player " + player.getName() + " closed");
        lobby.removePlayer(this);
        lobby = null;
    }

    /**
     * upon receiving an error in the websocket connection, displays it into the console
     * @param session of the Websocket connection i.e. Client and Server
     * @param cause of the error
     */
    @OnError
    public void onError(Session session, Throwable cause) {
        log.error("Error in the Websocket connection with player " + player.getName() + ": " + cause.getMessage());
    }


    /**
     * returns the player object corresponding to the endpoint
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * sends data to the frontend
     * @param data is the world information of the game
     */
    public void send(String data) {
        try {
            session.getBasicRemote().sendText(data);
            log.trace("synchronization message sent to player " + player.getName());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * closes all endpoint connections (all endpoints in List endpoints)
     */
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
