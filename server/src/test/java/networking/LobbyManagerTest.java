package networking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LobbyManagerTest {

    @Test
    void createLobby() {
        List<Integer> expectedJoinCodes = new ArrayList<>(1000);
        List<Integer> actualJoinCodes = new ArrayList<>(1000);

        LobbyManager.closeAllLobbies();

        for (int i = 0; i < 1000; i++) {
            expectedJoinCodes.add(i);
            actualJoinCodes.add(LobbyManager.createLobby());
        }

        assertTrue(actualJoinCodes.containsAll(expectedJoinCodes));
        LobbyManager.closeAllLobbies();
    }

    @Test
    void joinLobby() throws Exception {
        int joinCode = LobbyManager.createLobby();
        Endpoint endpoint = new Endpoint();
        endpoint.onOpen(joinCode, "Anjo", null);

        Lobby lobby = LobbyManager.joinLobby(joinCode, endpoint);

        assertNotNull(lobby);
        assertTrue(lobby.hasPlayer(endpoint));
    }

    @Test
    void closeAllLobbies() {
        int joinCode = LobbyManager.createLobby();
        LobbyManager.closeAllLobbies();
        assertThrows(Exception.class, () -> LobbyManager.getLobby(joinCode));
    }
}