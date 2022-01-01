package logic.gamemodes;

import helpers.ResourceManager;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import org.junit.jupiter.api.Assertions;
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

    /**
     *
     * @param max maximum result (exclusive)
     * @return random int from 0 to max (exclusive) if max is 3 the result is 0, 1 or 2
     */
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

        List<Player> _players = new ArrayList<>();
        _players.add(new Player());

        Gamemode gamemode = new BasicSnake(_players, map);

        String expected = """
                #####
                #   #
                # H #
                #   #
                #####
                """;
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
                #####
                """;
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('w');
        gamemode.gameLoop();
        expected = """
                #####
                # H #
                # o #
                #   #
                #####
                """;
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('a');
        gamemode.gameLoop();
        expected = """
                #####
                #Ho #
                # o #
                #   #
                #####
                """;
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('s');
        gamemode.gameLoop();
        expected = """
                #####
                #oo #
                #Ho #
                #   #
                #####
                """;
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('s');
        gamemode.gameLoop();
        expected = """
                #####
                #oo #
                #oo #
                #H  #
                #####
                """;
        Assertions.assertEquals(expected, gamemode.toString());

        _players.get(0).setInput('d');
        gamemode.gameLoop();
        expected = """
                #####
                #oo #
                #o  #
                #oH #
                #####
                """;
        Assertions.assertEquals(expected, gamemode.toString());
    }

    @Test
    void testBigMapSpawnsAndMove() throws IOException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);

        Gamemode gamemode = new BasicSnake(players, basicMap50x50);
        players.forEach(player -> player.setInput(inputs[getRandomInt(4)]));
        for (int i = 0; i < 5; i++) {
            System.out.println(gamemode);
            gamemode.gameLoop();
        }
        System.out.println(gamemode);
        // TODO: 01.01.2022 add assert statement
    }

    @Test
    void testBigMapSpawnsMoveAndDie() throws IOException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);

        Gamemode gamemode = new BasicSnake(players, basicMap50x50);

        for (int i = 0; i < 5; i++) {
            players.forEach(player -> player.setInput(inputs[getRandomInt(4)]));
            gamemode.gameLoop();
            // current problem: the snakes are killing themselves, because of the random input
            //                  after being dead, snake.positions is empty, but still used -> IndexOutOfBounds
            System.out.println(gamemode);
        }
        // TODO: 01.01.2022 add assert statement
    }
}
