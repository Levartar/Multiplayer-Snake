package logic.gamemodes;

import logic.Gamemode;
import logic.Map;
import logic.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BasicSnakeTest {

    @Test
    void testToString() {
        String mapString = """
                #####
                #   #
                # s #
                #   #
                #####
                """;
        Map map = new Map(mapString);

        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new Player());
        }

        Gamemode gamemode = new BasicSnake(players, map);

        System.out.println("gamemode = \n" + gamemode);
    }
}
