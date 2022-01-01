package logic.gamemodes;

import helpers.ResourceManager;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        return inputs[(int) Math.floor(Math.random() * 4)];
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

        List<Player> _players = new ArrayList<>();
        _players.add(new Player());

        Gamemode gamemode = new BasicSnake(_players, map);

        String expected = """
                #####
                #   #
                # H #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
    }

    /**
     * tests movement in all directions
     */
    @Test
    void testMoveSnake() {
        String mapString = """
                #####
                #   #
                # s #
                #   #
                #####
                """;
        Map map = new Map(mapString);

        List<Player> _players = new ArrayList<>();
        _players.add(new Player());

        Gamemode gamemode = new BasicSnake(_players, map);

        String expected = """
                #####
                #   #
                # H #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('w');
        gamemode.gameLoop();
        expected = """
                #####
                # H #
                # o #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('a');
        gamemode.gameLoop();
        expected = """
                #####
                #Ho #
                # o #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('s');
        gamemode.gameLoop();
        expected = """
                #####
                #oo #
                #Ho #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('s');
        gamemode.gameLoop();
        expected = """
                #####
                #oo #
                #oo #
                #H  #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('d');
        gamemode.gameLoop();
        expected = """
                #####
                #oo #
                #o  #
                #oH #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
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
        // TODO: 01.01.2022 add assert statement
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
        // TODO: 01.01.2022 add assert statement
        logger.info("\n"+gamemode);

    }

    @Test
    void testDie() throws IOException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);
        Gamemode gamemode = new BasicSnake(players, basicMap50x50);
        players.forEach(player -> {
            player.setInput('w');
        });
        gamemode.gameLoop();
        players.forEach(player -> {
            player.setInput('a');
        });
        gamemode.gameLoop();
        players.forEach(player -> {
            player.setInput('s');
        });
        gamemode.gameLoop();
        players.forEach(player -> {
            player.setInput('d');
        });
        gamemode.gameLoop();
        gamemode.gameLoop();
        gamemode.gameLoop();
        logger.info("\n"+gamemode);
        logger.info("\n"+basicMap50x50);

        assertEquals(basicMap50x50.toString(),gamemode.toString());
    }
}
