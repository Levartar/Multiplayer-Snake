package logic.gamemodes;

import helpers.ResourceManager;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class BasicSnakeTest {

    List<Player> players = new ArrayList<>();
    char[] inputs = {'w','a','s','d'};

    public BasicSnakeTest() {
        for (int i = 0; i < 4; i++) {
            players.add(new Player());
        }

    }

    private int getRandomInt(int max) {
        return (int) Math.floor(Math.random() * max);
    }

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

        Gamemode gamemode = new BasicSnake(players, map);

        System.out.println("gamemode = \n" + gamemode);
    }

    @Test
    void testBigMapSpawnsAndMove() throws IOException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);

        Gamemode gamemode = new BasicSnake(players, basicMap50x50);
        players.forEach(player -> {
            player.setInput(inputs[getRandomInt(3)]);
        });
        for (int i = 0; i < 5; i++) {
            gamemode.gameLoop();
        }
        System.out.println(gamemode);
    }

    @Test
    void testBigMapSpawnsMoveAndDie() throws IOException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);

        Gamemode gamemode = new BasicSnake(players, basicMap50x50);

        for (int i = 0; i < 5; i++) {
            players.forEach(player -> {
                player.setInput(inputs[getRandomInt(3)]);
            });
            gamemode.gameLoop();
        }
    }
}
