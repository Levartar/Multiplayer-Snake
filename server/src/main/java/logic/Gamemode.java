package logic;

import exceptions.GameNotInitializedException;
import exceptions.GameOverException;

import java.util.Map;

public interface Gamemode {
    String BASIC_SNAKE = "basic_snake";

    /**
     *
     * @return String that contains the changes of this gameloop
     * @throws GameOverException if the function is called after the game ended
     */
    String gameLoop() throws GameOverException, GameNotInitializedException;

    String init();

    void setMap(logic.Map map);

    Map<String, Integer> getScores();

    int getTimeLeft();
}
