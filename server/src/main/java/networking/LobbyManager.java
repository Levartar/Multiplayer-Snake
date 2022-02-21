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

    /**
     * opens a new Lobby Object with a free joinCode
     * @return int the used joinCode
     */
    public static int createLobby() {
        int joinCode = generateCode();
        lobbies.add(new Lobby(joinCode));
        log.info("lobby created, join code = " + joinCode);
        return joinCode;
    }

    /**
     * generates a new and unused joinCode
     * @return int joinCode
     */
    private static int generateCode() {
        int joinCode = (int) Math.floor(Math.random() * MAX_LOBBIES);
        for (Lobby lobby : lobbies) {
            if (lobby.getJoinCode() == joinCode) {
                return generateCode();
            }
        }
        return joinCode;
    }

    /**
     * returns the Lobby object with a given joinCode
     * @return Lobby Object
     * @throws NoSuchLobbyException if no lobby with the provided joinCode exists
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
        log.warn("Closing all lobbies. This is not threadsafe and should not be used in production");
        Endpoint.closeAllEndpoints();
        lobbies.clear();
        log.warn("All open lobbies have been closed!");
    }

    // TODO: 02.01.2022 close empty lobbies that are opened for 10 minutes
}
