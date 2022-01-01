package logic.gamemodes;

import helpers.ResourceManager;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import logic.Snake;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class BasicSnakeTest {

    private static final Logger logger = LogManager.getLogger(Snake.class);

    List<Player> players = new ArrayList<>();
    String[] names = {"alpha","beta","gamma","delta"};
    String[] colors = {"blue","green","red","yellow"};

    public BasicSnakeTest() {
        for (int i = 0; i < 4; i++) {
            players.add(new Player(names[i],colors[i],i ));
        }
        org.apache.log4j.BasicConfigurator.configure();

    }

    private char getRandomInput() {
        char[] inputs = {'w','a','s','d'};
        return inputs[(int) Math.floor(Math.random() * 3)];
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

        logger.info("gamemode = \n" + gamemode);
    }

    @Test
    void testBigMapSpawnsAndMove() throws IOException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);

        Gamemode gamemode = new BasicSnake(players, basicMap50x50);
        players.forEach(player -> {
            player.setInput(getRandomInput());
        });
        for (int i = 0; i < 10; i++) {
            gamemode.gameLoop();
        }
    }

    @Test
    void testBigMapSpawnsMoveAndDie() throws IOException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);

        Gamemode gamemode = new BasicSnake(players, basicMap50x50);

        for (int i = 0; i < 5; i++) {
            players.forEach(player -> {
                player.setInput(getRandomInput());
            });
            gamemode.gameLoop();
        }
        logger.info("\n"+gamemode);

    }
}
