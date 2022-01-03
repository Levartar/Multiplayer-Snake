package logic;

import exceptions.GameOverException;

import java.util.Map;

public interface Gamemode {
    /**
     *
     * @return String that contains the changes of this gameloop
     * @throws GameOverException if the function is called after the game ended
     */
    String gameLoop() throws GameOverException;
}
