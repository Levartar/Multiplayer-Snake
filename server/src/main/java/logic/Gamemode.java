package logic;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Gamemode {

    //TODO think about gamemodes
    // this enum belongs somewhere else
    enum gamemode{
        BasicSnake,
        UnderworldSnake
    }

    int playerNumber;
    List<Player> players;
    List<Snake> snakes;
    Map map;

    public Gamemode(List<Player> players, Map map) {
        this.playerNumber = playerNumber;
        this.players = players;
        this.map = map;
        List<Position> spawns = map.getSpawnPoints();

        for (int i = 0; i < players.size(); i++) {
            snakes.add(new Snake(spawns.get(i),5,players.get(i)));
        }
    }

    public String gameLoop() {
        //move Snakes in DIRECTION
        snakes.forEach(snake -> {
            snake.move();
        });
        checkCollision();
        //Think about how to not send something if nothing changed
        JSONObject worldMessage = new JSONObject();
        worldMessage.put("world", map.toString());
        worldMessage.put("replace", printReplace());
        worldMessage.put("snakes", printSnakes());
        return worldMessage.toString();
    }

    private JSONArray printSnakes() {
        JSONArray snakeArray = new JSONArray();
        snakes.forEach(snake -> {
            JSONObject snakeObject = new JSONObject();
            snakeObject.put("name",snake.getName());
            snakeObject.put("direction",snake.getDirection());
            JSONArray positionsArray = new JSONArray();
            snake.getPositions().forEach(position -> {
                positionsArray.put(new JSONObject(position.toString()));
            });
            snakeObject.put("positions",positionsArray);
            snakeArray.put(snakeObject);
        });
        return snakeArray;
    }

    private JSONArray printReplace() {
        JSONArray replaceArray = new JSONArray();
        //TODO call Apple/Food spawns here
        return replaceArray;
    }

    //TODO Food Spawns

    private void checkCollision() {
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
            //TODO check apple/(items) collisions
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


