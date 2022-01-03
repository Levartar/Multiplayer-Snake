package networking;

import logic.Gamemode;
import logic.Map;
import logic.Player;
import logic.gamemodes.BasicSnake;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
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

    public static void joinLobby(int joinCode, CommunicationEndpoint endpoint) throws Exception {
        Lobby lobby = getLobby(joinCode);
        lobby.join(endpoint);
    }

    public static void setGamemode(int joinCode, String gamemode) throws Exception {
        Lobby lobby = getLobby(joinCode);
        lobby.setGamemode(gamemode);
    }

    public static void setMap(int joinCode, Map map) throws Exception {
        Lobby lobby = getLobby(joinCode);
        lobby.setMap(map);
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
     *
     * @return lobby with the joinCode
     * @throws Exception if no lobby with the provided joinCode exists
     */
    public static Lobby getLobby(int joinCode) throws Exception {
        for (Lobby lobby : lobbies) {
            if (lobby.getJoinCode() == joinCode) {
                return lobby;
            }
        }
        throw new Exception("No matching Lobby found for joinCode: " + joinCode);
    }

    /**
     * closes all lobbies, even if games are running
     */
    public static void closeAllLobbies() {
        lobbies.clear();
    }

    // TODO: 02.01.2022 close empty lobbies that are opened for 10 minutes
}
