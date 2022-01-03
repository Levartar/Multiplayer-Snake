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
            System.out.println("expectedJoinCodes.size() = " + expectedJoinCodes.size());
            actualJoinCodes.add(LobbyManager.createLobby());
            System.out.println("actualJoinCodes.size() = " + actualJoinCodes.size());
        }

        assertTrue(actualJoinCodes.containsAll(expectedJoinCodes));
    }

    @Test
    void joinLobby() throws Exception {
        int joinCode = LobbyManager.createLobby();
        CommunicationEndpoint endpoint = new CommunicationEndpoint();

        LobbyManager.joinLobby(joinCode, endpoint);
        Lobby lobby = LobbyManager.getLobby(joinCode);

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