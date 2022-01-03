package networking;

import exceptions.GameOverException;
import logic.Player;

import javax.websocket.server.ServerEndpoint;

public class CommunicationEndpoint {

    private Lobby lobby;
    private Player player;

    public Player getPlayer(){
        return player;
    }

    public void send(String data) throws GameOverException {
        // TODO: 03.01.2022 implement
    }
}
