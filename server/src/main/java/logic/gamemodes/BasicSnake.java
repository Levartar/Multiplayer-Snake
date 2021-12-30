package logic.gamemodes;

import logic.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicSnake implements Gamemode {

    private Map map;

    private List<Snake> snakes;

    public BasicSnake(List<Player> players, Map map) {
        this.map = map;
        snakes = new ArrayList<>();

        // create snakes
        List<Position> spawnPoints = map.getSpawnPoints();
        int size = Math.min(players.size(), spawnPoints.size());
        for (int i = 0; i < size; i++) {
            Snake snake = new Snake(spawnPoints.get(i), 5, players.get(i));
            snakes.add(snake);
        }
    }

    @Override
    public String gameLoop() {
        snakes.forEach(Snake::move);
        // TODO: 30.12.2021 Snake can move to other side
        checkCollision();
        //Think about how to not send something if nothing changed
        JSONObject worldMessage = new JSONObject();
        worldMessage.put("world", map.toString());
        worldMessage.put("replace", printReplace());
        worldMessage.put("snakes", printSnakes());
        return worldMessage.toString();
    }

    private void checkCollision() {
        // TODO: 30.12.2021 rework
        //Collision rules are made here!
        // # = Wall = Death
        // @ = Apple = grow
        snakes.forEach(snake -> {
            //Generate head
            Position head = snake.getPositions().get(0);
            //Check head collides with wall
            if (map.getMaterialAt(head) == Material.WALL) {
                snake.die();
            }
            //TODO check apple/(items) collisions
            //Check head collide with snakes
            snakes.forEach(s -> {
                s.getPositions().forEach(position -> {
                    if (position == head) {
                        snake.die();
                    }
                });
            });
        });
    }

    private JSONArray printSnakes() {
        JSONArray snakeArray = new JSONArray();
        snakes.forEach(snake -> {
            JSONObject snakeObject = new JSONObject();
            snakeObject.put("name", snake.getName());
            snakeObject.put("direction", snake.getDirection());
            JSONArray positionsArray = new JSONArray();
            snake.getPositions().forEach(position -> {
                positionsArray.put(new JSONObject(position.toString()));
            });
            snakeObject.put("positions", positionsArray);
            snakeArray.put(snakeObject);
        });
        return snakeArray;
    }

    private JSONArray printReplace() {
        JSONArray replaceArray = new JSONArray();
        //TODO call Apple/Food spawns here
        return replaceArray;
    }

    @Override
    public String toString() {
        // convert map string to list
        List<String> lines = map.toString().lines().collect(Collectors.toList());

        // add snakes
        snakes.forEach(snake -> {
            snake.getPositions().forEach(position -> {
                int x = position.getX();
                int y = position.getY();
                String line = lines.get(y);

                line = line.substring(0, x) + Material.SNAKE + line.substring(x + 1, line.length());
                lines.set(y, line);
            });
        });

        StringBuilder result = new StringBuilder();
        for (String line :
                lines) {
            result.append(line).append('\n');
        }
        return result.toString();
    }
}
