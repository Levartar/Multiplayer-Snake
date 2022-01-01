package logic.gamemodes;

import logic.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicSnake implements Gamemode {

    private final Map map;
    private final List<Snake> snakes;

    private final List<Snake> scheduledForRemoval = new ArrayList<>();

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
        // Collision rules are made here!
        // # = Wall = Death
        // @ = Apple = grow

        scheduledForRemoval.clear();

        snakes.forEach(snake -> {
            if (snake == null) return;

            Position head = snake.getHead();

            // wall collisions
            if (map.getMaterialAt(head) == Material.WALL) {
                scheduledForRemoval.add(snake);
            }

            // TODO apple collisions

            // snake collisions
            snakes.forEach(snake2 -> {
                if (snake2 == null) return;

                snake2.getPositions().forEach(position -> {
                    // remove snake, if its head collides with another snake or its own body
                    if (head.equals(position) && position != head) {
                        scheduledForRemoval.add(snake);
                    }
                });
            });
        });

        scheduledForRemoval.forEach(snakes::remove);
        scheduledForRemoval.clear();
    }

    private JSONArray printSnakes() {
        JSONArray snakeArray = new JSONArray();
        snakes.forEach(snake -> {
            JSONObject snakeObject = new JSONObject();
            snakeObject.put("name", snake.getName());
            snakeObject.put("direction", snake.getDirection());
            JSONArray positionsArray = new JSONArray();
            snake.getPositions().forEach(position -> {
                positionsArray.put("{" + position.toString() + "}");
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
            Position head = snake.getHead();
            snake.getPositions().forEach(position -> {
                int x = position.getX();
                int y = position.getY();
                String line = lines.get(y);

                char insert;
                // if the current position is the head -> write H
                if (position == head) {
                    insert = 'H';
                // if the current position is at the position of the head -> don't override the head symbol
                } else if (position.equals(head)) {
                    return;
                } else {
                    insert = Material.SNAKE.getSymbol();
                }

                line = line.substring(0, x) + insert + line.substring(x + 1);
                lines.set(y, line);
            });
        });

        StringBuilder result = new StringBuilder();
        for (String line :
                lines) {
            result.append(line).append('\n');
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}
