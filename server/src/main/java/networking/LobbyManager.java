package networking;

import exceptions.NoSuchLobbyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LobbyManager {

    private static final Logger log = LogManager.getLogger(LobbyManager.class);
    private static final List<Lobby> lobbies = new ArrayList<>();

    private static final int MAX_LOBBIES = 1000;

    public static int createLobby() {
        int joinCode = generateCode();
        lobbies.add(new Lobby(joinCode));
        log.info("lobby created, join code = " + joinCode);
        return joinCode;
    }

    public static Lobby joinLobby(int joinCode, Endpoint endpoint) throws Exception {
        Lobby lobby = getLobby(joinCode);
        lobby.join(endpoint);
        return lobby;
    }

    private static int generateCode() {
        int joinCode = (int) Math.floor(Math.random() * MAX_LOBBIES);
        for (Lobby lobby : lobbies) {
            if (lobby.getJoinCode() == joinCode) {
                return generateCode();
            }
        }
        return joinCode;
    }

    public static void leaveLobby(Endpoint endpoint) {
        for (Lobby lobby : lobbies) {
            if (lobby.hasPlayer(endpoint)) {
                lobby.removePlayer(endpoint);
                return;
            }
        }
        log.error("Endpoint " + endpoint + " tried to leave a lobby, but is in no lobby");
    }

    /**
     *
     * @return lobby with the joinCode
     * @throws Exception if no lobby with the provided joinCode exists
     */
    public static Lobby getLobby(int joinCode) throws NoSuchLobbyException {
        for (Lobby lobby : lobbies) {
            if (lobby.getJoinCode() == joinCode) {
                return lobby;
            }
        }
        throw new NoSuchLobbyException("No matching Lobby found for joinCode: " + joinCode);
    }

    /**
     * closes all lobbies, even if games are running
     */
    public static void closeAllLobbies() {
        Endpoint.closeAllEndpoints();
        lobbies.clear();
        log.warn("All open lobbies have been closed!");
    }

    // TODO: 02.01.2022 close empty lobbies that are opened for 10 minutes
}
