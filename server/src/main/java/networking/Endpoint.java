package networking;

import exceptions.GameOverException;
import logic.Player;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/join/{code}/name/{name}")
public class Endpoint {

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
        } catch (Exception e) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, e.getMessage()));
        }
    }

    @OnMessage
    public void onMessage(char input, Session session) {

        // TODO: 03.01.2022 input message handler
    }

    @OnClose
    public void onClose(Session session) {
        lobby = null;
        // TODO: 03.01.2022 remove endpoint
    }

    @OnError
    public void onError(Session session) {
        // TODO: 03.01.2022
    }


    public Player getPlayer() {
        return player;
    }

    public void send(String data) throws GameOverException {
        // TODO: 03.01.2022 implement
    }
}
