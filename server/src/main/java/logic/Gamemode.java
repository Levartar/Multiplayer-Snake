package logic;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class Gamemode {

    //this enum belongs somewhere else
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
        //Set Spawnpoints on each map and use them by random???
        this.snakes = null;
    }

    public String gameLoop() {
        //move Snakes in DIRECTION
        for (int i = 0; i < players.toArray().length-1; i++) {
            snakes.get(i).move(players.get(i).getDirection());
        }
        checkCollision(playMap,snakes);
        //TODO build Return MapString
        JSONObject worldMessage = new JSONObject();
        worldMessage.put("world",playMap.toString());
        JSONArray replaceMessage = new JSONArray();
        //worldMessage.put("replace",)

        return null;
    }

    public void checkCollision(Map map, List<Snake> snakes) {
        //Collision rules are made here!
        // # = Wall = Death
        // @ = Apple = grow
        snakes.forEach(snake -> {
            //Generate head
            Position head = snake.getPositions().get(0);
            //Check head collides with wall
            if (map.get(head)==Material.WALL) {
                snake.die();
            }
            //Check head collide with snakes
            snakes.forEach(s -> {
                s.getPositions().forEach(position -> {
                    if (position == head){
                        snake.die();
                    }
                });
            });
        });
    }

}


