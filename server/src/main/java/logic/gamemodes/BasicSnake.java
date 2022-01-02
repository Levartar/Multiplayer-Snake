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
    private final JSONArray JSON_replace = new JSONArray();
    private final JSONObject JSON_synchronizationMessage = new JSONObject();

    public BasicSnake(List<Player> players, Map map) {
        this.map = map;
        snakes = new ArrayList<>();

        // initial message
        JSON_synchronizationMessage.put("world", map.toString());

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
        // TODO: 01.01.2022 spawn food
        //Think about how to not send something if nothing changed
        return getSynchronizationMessage();
    }

    private String getSynchronizationMessage() {
        if (!JSON_replace.isEmpty()) {
            JSON_synchronizationMessage.put("replace", JSON_replace);
        }
        if (!printSnakes().isEmpty()) {
            JSON_synchronizationMessage.put("snakes", printSnakes());
        }

        String message = JSON_synchronizationMessage.toString();
        JSON_synchronizationMessage.clear();
        JSON_replace.clear();
        return message;
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

            // apple collisions
            if (map.getMaterialAt(head) == Material.APPLE) {
                snake.grow(1);
                map.changeMaterial(head, Material.FREESPACE);
                jsonChangeMaterial(head, Material.FREESPACE);
            }
        });

        scheduledForRemoval.forEach(snakes::remove);
        scheduledForRemoval.clear();
    }

    private void jsonChangeMaterial(Position position, Material material) {
        // bad performance, because many objects are created
        JSONObject materialChange = new JSONObject();
        JSONObject pos = new JSONObject();
        pos.put("x", position.getX());
        pos.put("y", position.getY());
        materialChange.put("pos", pos);
        materialChange.put("mat", material.toString());
        JSON_replace.put(materialChange);
    }

    private JSONArray printSnakes() {
        // bad performance, because many objects are created
        JSONArray snakeArray = new JSONArray();
        snakes.forEach(snake -> {
            JSONObject snakeObject = new JSONObject();
            snakeObject.put("name", snake.getName());
            snakeObject.put("direction", snake.getDirection().toString());
            JSONArray positionsArray = new JSONArray();
            snake.getPositions().forEach(position -> {
                JSONObject positionJSON = new JSONObject();
                positionJSON.put("x", position.getX());
                positionJSON.put("y", position.getY());
                positionsArray.put(positionJSON);
            });
            snakeObject.put("positions", positionsArray);
            snakeArray.put(snakeObject);
        });
        return snakeArray;
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
