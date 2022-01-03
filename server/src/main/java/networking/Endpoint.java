package networking;

import exceptions.GameOverException;
import logic.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/join/{code}/name/{name}")
public class Endpoint {
    private static final Logger log = LogManager.getLogger(Endpoint.class);

    private Session session;

    private Lobby lobby;
    private Player player;

    @OnOpen
    public void onOpen(@PathParam("code") int joinCode, @PathParam("name") String name, Session session) throws IOException {
        this.session = session;
        try {
            lobby = LobbyManager.getLobby(joinCode);
        } catch (Exception e) {
            // the client receives: onClose(1003, some text)
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, e.getMessage()));
        }

        player = new Player(name);

        try {
            lobby.join(this);
            log.info("Player with the name " + player.getName() +
                    " joined Lobby with the code " + lobby.getJoinCode());
        } catch (Exception e) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, e.getMessage()));
        }
    }

    @OnMessage
    public void onMessage(char input, Session session) {
        player.setInput(input);
    }

    @OnClose
    public void onClose(Session session) {
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

    public void send(String data) throws GameOverException {
        try {
            session.getBasicRemote().sendText(data);
            log.trace("synchronization message successfully sent");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
