package logic.gamemodes;

import exceptions.GameOverException;
import helpers.ResourceManager;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import logic.Snake;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BasicSnakeTest {

    private static final Logger logger = LogManager.getLogger(Snake.class);

    List<Player> players = new ArrayList<>();
    String[] names = {"alpha", "beta", "gamma", "delta"};
    String[] colors = {"blue", "green", "red", "yellow"};

    public BasicSnakeTest() {
        for (int i = 0; i < 4; i++) {
            players.add(new Player(names[i], colors[i], i));
        }

    }

    private char getRandomInput() {
        char[] inputs = {'w', 'a', 's', 'd'};
        return inputs[(int) Math.floor(Math.random() * 4)];
    }

    @Test
    void testToString() {
        String mapString = """
                #####
                # @ #
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
                # @ #
                # H #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
    }

    /**
     * tests movement in all directions
     */
    @Test
    void testMoveSnake() throws GameOverException {
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
    void testBigMapSpawnsAndMove() throws IOException, GameOverException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);

        Gamemode gamemode = new BasicSnake(players, basicMap50x50);
        players.forEach(player -> player.setInput('w'));
        for (int i = 0; i < 10; i++) {
            gamemode.gameLoop();
        }
        String expected = """
                ####################################################
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #              H                   H               #
                #              o                   o               #
                #              o                   o               #
                #              o                   o               #
                #              o                   o               #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #              H                   H               #
                #              o                   o               #
                #              o                   o               #
                #              o                   o               #
                #              o                   o               #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                ####################################################""";

        assertEquals(expected, gamemode.toString());
    }

    @Test
    void testSnakeCollidesWithWall() throws GameOverException {
        String mapString = """
                #####
                #   #
                # s #
                #   #
                #####
                """;
        Map map = new Map(mapString);

        List<Player> _players = new ArrayList<>();
        Player player = new Player();
        _players.add(player);

        Gamemode gamemode = new BasicSnake(_players, map);

        String expected = """
                #####
                #   #
                #   #
                #   #
                #####""";

        player.setInput('d');
        gamemode.gameLoop();
        Assertions.assertNotEquals(expected, gamemode.toString());
        gamemode.gameLoop();
        Assertions.assertEquals(expected, gamemode.toString());
    }

    @Test
    void testSnakeCollidesWithItself() throws IOException, GameOverException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);
        Gamemode gamemode = new BasicSnake(players, basicMap50x50);
        players.forEach(player -> player.setInput('w'));
        gamemode.gameLoop();
        players.forEach(player -> player.setInput('a'));
        gamemode.gameLoop();
        players.forEach(player -> player.setInput('s'));
        gamemode.gameLoop();
        players.forEach(player -> player.setInput('d'));
        gamemode.gameLoop();
        logger.info("\n" + gamemode);
        logger.info("\n" + basicMap50x50);

        assertEquals(basicMap50x50.toString(), gamemode.toString());
    }

    @Test
    void testSnakeCollidesWithOtherSnake() throws GameOverException {
        String mapString = """
                #########
                #       #
                # s s   #
                #       #
                #########
                """;
        Map map = new Map(mapString);

        List<Player> _players = new ArrayList<>();
        _players.add(new Player());
        _players.add(new Player());
        Gamemode gamemode = new BasicSnake(_players, map);

        String expected = """
                #########
                #       #
                #   ooH #
                #       #
                #########""";

        _players.forEach(player -> player.setInput('d'));

        gamemode.gameLoop();
        assertNotEquals(expected, gamemode.toString());
        gamemode.gameLoop();
        assertEquals(expected, gamemode.toString());
    }

    @Test
    void testSnakeCollidesWithApple() throws GameOverException {
        String mapString = """
                ############
                #          #
                # s     @  #
                #          #
                ############
                """;
        Map map = new Map(mapString);

        List<Player> _players = new ArrayList<>();
        Player player = new Player();
        _players.add(player);
        Gamemode gamemode = new BasicSnake(_players, map);

        player.setInput('d');

        String expected = """
                ############
                #          #
                #   ooooH  #
                #          #
                ############""";

        for (int i = 0; i < 6; i++) {
            gamemode.gameLoop();
        }
        assertEquals(expected, gamemode.toString());

        expected = """
                ############
                #          #
                #   oooooH #
                #          #
                ############""";
        gamemode.gameLoop();
        assertEquals(expected, gamemode.toString());

    }

    @Test
    void testBigMapSpawnsMoveRandomAndDie() throws IOException, GameOverException {
        Path basicMap50x50Path = ResourceManager.getMapPath("BasicMap50x50");

        Map basicMap50x50 = new Map(basicMap50x50Path);

        Gamemode gamemode = new BasicSnake(players, basicMap50x50);

        for (int i = 0; i < 5; i++) {
            players.forEach(player -> player.setInput(getRandomInput()));
            gamemode.gameLoop();
        }
        logger.info("\n" + gamemode);
    }

    @Test
    void testSynchronizationMessage() throws GameOverException {
        String mapString = """
                ##########
                #        #
                # s   @  #
                #        #
                ##########
                """;
        Map map = new Map(mapString);

        List<Player> _players = new ArrayList<>();
        Player player1 = new Player();
        player1.setName("jakob");
        _players.add(player1);
        Gamemode gamemode = new BasicSnake(_players, map);

        _players.forEach(player -> player.setInput('d'));

        String[] expected = new String[10];
        expected[0] = "{\"world\":\"##########\\n#        #\\n#     @  #\\n#        #\\n##########\",\"scores\":[{\"name\":\"jakob\",\"points\":5}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":3,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        expected[1] = "{\"scores\":[{\"name\":\"jakob\",\"points\":5}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":4,\"y\":2},{\"x\":3,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        expected[2] = "{\"scores\":[{\"name\":\"jakob\",\"points\":5}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":5,\"y\":2},{\"x\":4,\"y\":2},{\"x\":3,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        expected[3] = "{\"scores\":[{\"name\":\"jakob\",\"points\":6}],\"replace\":[{\"mat\":\" \",\"pos\":{\"x\":6,\"y\":2}}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":6,\"y\":2},{\"x\":5,\"y\":2},{\"x\":4,\"y\":2},{\"x\":3,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        expected[4] = "{\"scores\":[{\"name\":\"jakob\",\"points\":6}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":7,\"y\":2},{\"x\":6,\"y\":2},{\"x\":5,\"y\":2},{\"x\":4,\"y\":2},{\"x\":3,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        expected[5] = "{\"scores\":[{\"name\":\"jakob\",\"points\":6}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":8,\"y\":2},{\"x\":7,\"y\":2},{\"x\":6,\"y\":2},{\"x\":5,\"y\":2},{\"x\":4,\"y\":2},{\"x\":3,\"y\":2}],\"direction\":\"right\"}]}";
        expected[6] = "{\"scores\":[{\"name\":\"jakob\",\"points\":6}],\"snakes\":[]}";

        for (int i = 0; i < 7; i++) {
            assertEquals(expected[i], gamemode.gameLoop());
        }
    }
}
