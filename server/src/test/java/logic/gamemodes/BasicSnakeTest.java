package logic.gamemodes;

import logic.Gamemode;
import logic.Map;
import logic.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicSnakeTest {

    @Test
    void testToString() {
        String mapString = """
                #####
                #   #
                # + #
                #   #
                #####
                """;
        Map map = new Map();
        map.Map(mapString);

        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new Player());
        }

        Gamemode gamemode = new BasicSnake(players, map);

        System.out.println("gamemode = \n" + gamemode);
    }
}
