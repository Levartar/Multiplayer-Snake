package logic;

import netscape.javascript.JSObject;

import java.util.List;

public class Gamemode {

    enum gamemode{
        BasicSnake,
        OctagonSnake,
        UnderworldSnake
    }

    int playerNumber;
    List<Player> players;
    List<Snake> snakes;
    Map playMap;

    public Gamemode(int playerNumber, List<Player> players, Map playMap) {
        this.playerNumber = playerNumber;
        this.players = players;
        this.playMap = playMap;

        //TODO create one Snake for each player
        this.snakes = null;
    }

    public String gameLoop() {
        //move Snakes in DIRECTION
        for (int i = 0; i < players.toArray().length-1; i++) {
            snakes.get(i).move(players.get(i).getDirection());
        }
        checkCollision(playMap,snakes);
        //TODO build Return MapString
        return null;
    }

    public void checkCollision(Map checkMap, List<Snake> snakes) {
        //TODO
    }
}


