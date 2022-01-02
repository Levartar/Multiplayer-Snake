package networking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LobbyManager {

    private static final Logger log = LogManager.getLogger(LobbyManager.class);
    private static final List<Lobby> lobbies = new ArrayList<>();

    public static int createLobby() {
        int joinCode = generateCode();
        lobbies.add(new Lobby(joinCode, new BasicSnake()));
        log.info("lobby created, join code = " + joinCode);
        return joinCode;
    }

    public static void joinLobby(int joinCode, CommunicationEndpoint endpoint) throws Exception {
        Lobby lobby = getLobby(joinCode);
        if (lobby == null) {
            throw new Exception("No matching Lobby found for joinCode: " + joinCode);
        } else {
            lobby.join(endpoint);
        }
    }

    private static int generateCode() {
        int joinCode = (int) Math.floor(Math.random() * 10000);
        for (Lobby lobby : lobbies) {
            if (lobby.getJoinCode() == joinCode) {
                return generateCode();
            }
        }
        return joinCode;
    }

    public static void leaveLobby(CommunicationEndpoint endpoint) {
        for (Lobby lobby : lobbies) {
            if (lobby.hasPlayer(endpoint)) {
                lobby.removePlayer(endpoint);
                return;
            }
        }
        log.error("Endpoint " + endpoint + " tried to leave a lobby, but is in no lobby");
    }

    /**
     * @return lobby with joinCode. null, if no lobby is found
     */
    public static Lobby getLobby(int joinCode) {
        for (Lobby lobby : lobbies) {
            if (lobby.getJoinCode() == joinCode) {
                return lobby;
            }
        }
        return null;
    }

    // TODO: 02.01.2022 close empty lobbies that are opened for 10 minutes
}
