package networking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LobbyManagerTest {

    @Test
    void createLobby() {
        List<Integer> expectedJoinCodes = new ArrayList<>(10000);
        List<Integer> actualJoinCodes = new ArrayList<>(10000);

        for (int i = 0; i < 10000; i++) {
            expectedJoinCodes.add(i);
            actualJoinCodes.add(LobbyManager.createLobby());
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
}