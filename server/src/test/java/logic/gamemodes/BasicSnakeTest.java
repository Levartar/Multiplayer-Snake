package logic.gamemodes;

import exceptions.GameNotInitializedException;
import exceptions.GameOverException;
import helpers.ResourceManager;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicSnakeTest {

    private static final Logger log = LogManager.getLogger(BasicSnakeTest.class);

    List<Player> players = new ArrayList<>();
    String[] names = {"alpha", "beta", "gamma", "delta"};
    String[] colors = {"blue", "green", "red", "yellow"};

    public BasicSnakeTest() {
        for (int i = 0; i < 4; i++) {
            players.add(new Player(names[i]));
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
        gamemode.init();

        String expected = """
                #####
                # @ #
                # H #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
        log.info("Test "+"testToString" +" passed");
    }

    /**
     * tests movement in all directions
     */
    @Test
    void testMoveSnake() throws GameOverException, GameNotInitializedException {
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
        gamemode.init();

        String expected = """
                #####
                #   #
                # H #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
        log.debug("\n"+gamemode.toString());

        _players.get(0).setInput('w');
        gamemode.gameLoop();
        expected = """
                #####
                # H #
                # o #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
        log.debug("\n"+gamemode.toString());

        _players.get(0).setInput('a');
        gamemode.gameLoop();
        expected = """
                #####
                #Ho #
                # o #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
        log.debug("\n"+gamemode.toString());

        _players.get(0).setInput('s');
        gamemode.gameLoop();
        expected = """
                #####
                #oo #
                #Ho #
                #   #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
        log.debug("\n"+gamemode.toString());

        _players.get(0).setInput('s');
        gamemode.gameLoop();
        expected = """
                #####
                #oo #
                #oo #
                #H  #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
        log.debug("\n"+gamemode.toString());

        _players.get(0).setInput('d');
        gamemode.gameLoop();
        expected = """
                #####
                #oo #
                #o  #
                #oH #
                #####""";
        Assertions.assertEquals(expected, gamemode.toString());
        log.debug("\n"+gamemode.toString());
        log.info("Test "+"testMoveSnake" +" passed");
    }

    @Test
    void testSpawnAndMove() throws GameOverException, GameNotInitializedException {
        Map map = new Map("""
                #####
                #   #
                #  s#
                #   #
                #   #
                #   #
                #   #
                #####""");
        Player player = new Player();
        List<Player> players = new ArrayList<>(1);
        players.add(player);
        player.setInput('w');
        Gamemode gamemode = new BasicSnake(players, map);

        gamemode.init();
        String expected = """
                #####
                #   #
                #  H#
                #   #
                #   #
                #   #
                #   #
                #####""";
        assertEquals(expected, gamemode.toString());
    }

    @Test
    void testSnakeCollidesWithWall() throws GameOverException, GameNotInitializedException {
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
        gamemode.init();

        String expected = """
                #####
                #   #
                # @@#
                #   #
                #####""";

        player.setInput('d');
        gamemode.gameLoop();
        Assertions.assertNotEquals(expected, gamemode.toString());
        gamemode.gameLoop();
        Assertions.assertEquals(expected, gamemode.toString());
        log.info("Test "+"testSnakeCollidesWithWall" +" passed");
    }

    @Test
    void testSnakeCollidesWithItself() throws IOException, GameOverException, GameNotInitializedException {
        Map basicMap50x50 = new Map(ResourceManager.getMapString("BasicMap50x50"));
        Gamemode gamemode = new BasicSnake(players, basicMap50x50);
        gamemode.init();
        players.forEach(player -> player.setInput('w'));
        gamemode.gameLoop();
        players.forEach(player -> player.setInput('a'));
        gamemode.gameLoop();
        players.forEach(player -> player.setInput('s'));
        gamemode.gameLoop();
        players.forEach(player -> player.setInput('d'));
        gamemode.gameLoop();

        String expected = """
                ####################################################
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
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                #                                                  #
                ####################################################""";

        assertEquals(expected, gamemode.toString().replaceAll("@", " "));
        log.info("Test "+"testSnakeCollidesWithItself" +" passed");
    }

    @Test
    void testSnakeCollidesWithOtherSnake() throws GameOverException, GameNotInitializedException {
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
        gamemode.init();

        String expected = """
                #########
                #       #
                # @@ooH #
                #       #
                #########""";

        _players.forEach(player -> player.setInput('d'));

        gamemode.gameLoop();
        assertNotEquals(expected, gamemode.toString());
        gamemode.gameLoop();
        assertEquals(expected, gamemode.toString());

        log.info("Test "+"testSnakeCollidesWithOtherSnake" +" passed");
    }

    @Test
    void testSnakeCollidesWithApple() throws GameOverException, GameNotInitializedException {
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
        gamemode.init();

        player.setInput('d');

        String expected = """
                ############
                #          #
                #   ooooH  #
                #          #
                ############""";

        for (int i = 0; i < 6; i++) {
            gamemode.gameLoop();
            log.debug("\n"+gamemode.toString());
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
        log.debug("\n"+gamemode.toString());
        log.info("Test "+"testSnakeCollidesWithApple" +" passed");
    }

    @Test
    void testBigMapSpawnsMoveRandomAndDie() throws IOException, GameOverException, GameNotInitializedException {
        Map basicMap50x50 = new Map(ResourceManager.getMapString("BasicMap50x50"));

        Gamemode gamemode = new BasicSnake(players, basicMap50x50);
        gamemode.init();

        try{
            for (int i = 0; i < 5; i++) {
                players.forEach(player -> player.setInput(getRandomInput()));
                gamemode.gameLoop();
            }
        }catch (Exception e){

        }
        log.info("Test "+"testBigMapSpawnsMoveRandomAndDie" +" passed");
    }

    @Test
    void testSynchronizationMessage() throws GameOverException, GameNotInitializedException {
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
        Gamemode gamemode = new BasicSnake(_players, map,0);


        String[] expected = new String[8];
        String[] actual = new String[8];
        actual[0] = gamemode.init();

        _players.forEach(player -> player.setInput('d'));

        expected[0] = "{\"timer\":" + gamemode.getTimeLeft() + ",\"world\":{\"worldstring\":\"##########\\n#        #\\n#     @  #\\n#        #\\n##########\",\"width\":10,\"height\":5},\"scores\":[{\"name\":\"jakob\",\"points\":5}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":2,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"up\"}]}";
        actual[1] = gamemode.gameLoop();
        expected[1] = "{\"timer\":" + gamemode.getTimeLeft() + ",\"scores\":[{\"name\":\"jakob\",\"points\":5}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":3,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        actual[2] = gamemode.gameLoop();
        expected[2] = "{\"timer\":" + gamemode.getTimeLeft() + ",\"scores\":[{\"name\":\"jakob\",\"points\":5}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":4,\"y\":2},{\"x\":3,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        actual[3] = gamemode.gameLoop();
        expected[3] = "{\"timer\":" + gamemode.getTimeLeft() + ",\"scores\":[{\"name\":\"jakob\",\"points\":5}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":5,\"y\":2},{\"x\":4,\"y\":2},{\"x\":3,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        actual[4] = gamemode.gameLoop();
        expected[4] = "{\"timer\":" + gamemode.getTimeLeft() + ",\"scores\":[{\"name\":\"jakob\",\"points\":6}],\"replace\":[{\"mat\":\" \",\"pos\":{\"x\":6,\"y\":2}}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":6,\"y\":2},{\"x\":5,\"y\":2},{\"x\":4,\"y\":2},{\"x\":3,\"y\":2},{\"x\":2,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        actual[5] = gamemode.gameLoop();
        expected[5] = "{\"timer\":" + gamemode.getTimeLeft() + ",\"scores\":[{\"name\":\"jakob\",\"points\":6}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":7,\"y\":2},{\"x\":6,\"y\":2},{\"x\":5,\"y\":2},{\"x\":4,\"y\":2},{\"x\":3,\"y\":2},{\"x\":2,\"y\":2}],\"direction\":\"right\"}]}";
        actual[6] = gamemode.gameLoop();
        expected[6] = "{\"timer\":" + gamemode.getTimeLeft() + ",\"scores\":[{\"name\":\"jakob\",\"points\":6}],\"snakes\":[{\"name\":\"jakob\",\"positions\":[{\"x\":8,\"y\":2},{\"x\":7,\"y\":2},{\"x\":6,\"y\":2},{\"x\":5,\"y\":2},{\"x\":4,\"y\":2},{\"x\":3,\"y\":2}],\"direction\":\"right\"}]}";
        actual[7] = gamemode.gameLoop();
        expected[7] = "{\"timer\":" + gamemode.getTimeLeft() + ",\"scores\":[{\"name\":\"jakob\",\"points\":6}],\"replace\":[{\"mat\":\"@\",\"pos\":{\"x\":8,\"y\":2}},{\"mat\":\"@\",\"pos\":{\"x\":7,\"y\":2}},{\"mat\":\"@\",\"pos\":{\"x\":6,\"y\":2}},{\"mat\":\"@\",\"pos\":{\"x\":5,\"y\":2}},{\"mat\":\"@\",\"pos\":{\"x\":4,\"y\":2}}],\"snakes\":[],\"gameover\":{\"winner\":\"jakob\"}}";

        for (int i = 0; i < 8; i++) {
            assertEquals(expected[i], actual[i]);
        }
        log.info("Test "+"testSynchronizationMessage" +" passed");
    }

    @Test
    void testGameEndingConditions() throws GameOverException, GameNotInitializedException {
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
        gamemode.init();

        _players.forEach(player -> player.setInput('d'));

        for (int i = 0; i < 7; i++) {
            gamemode.gameLoop();
            log.debug("\n"+gamemode);
        }
        String endString = """
                ##########
                #        #
                #   @@@@@#
                #        #
                ##########""";
        assertEquals(endString,gamemode.toString());
        log.info("Test "+"testGameEndingConditions" +" passed");
    }

    @Test
    void testTimer() throws GameOverException, GameNotInitializedException, InterruptedException {
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

        gamemode.init();

        _players.forEach(player -> player.setInput('d'));


        for (int i = 0; i < 6; i++) {
            gamemode.gameLoop();
            Thread.sleep(1000);
            assertEquals(60-i,gamemode.getTimeLeft());
            log.debug("Timer: "+gamemode.getTimeLeft());
        }

        log.info("Test "+"testTimer" +" passed");
    }

    @Test
    void testGameOverException() throws GameOverException, GameNotInitializedException {
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
        gamemode.init();
        _players.forEach(player -> player.setInput('d'));

        for (int i = 0; i < 7; i++) {
            gamemode.gameLoop();
        }
        try {
            gamemode.gameLoop();
            fail("The 8th gameLoop() call should have thrown a GameOverException");
        } catch (GameNotInitializedException e) {
            fail(e.getMessage());
        } catch (GameOverException e) {
            log.info("Test "+"testGameOverException" +" passed");
        }
    }

    @Test
    void testCountDown() throws GameOverException, GameNotInitializedException, InterruptedException {
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
        Gamemode gamemode = new BasicSnake(_players, map,5);
        _players.forEach(player -> player.setInput('d'));
        gamemode.init();

        assertTrue(gamemode.gameLoop().contains("\"countdown\":5"));
        Thread.sleep(1000);
        assertTrue(gamemode.gameLoop().contains("\"countdown\":4"));
        Thread.sleep(1000);
        assertTrue(gamemode.gameLoop().contains("\"countdown\":3"));
        Thread.sleep(1000);
        assertTrue(gamemode.gameLoop().contains("\"countdown\":2"));
        Thread.sleep(1000);
        assertTrue(gamemode.gameLoop().contains("\"countdown\":1"));
        Thread.sleep(1000);
        assertFalse(gamemode.gameLoop().contains("\"countdown\""));
    }
}
