package logic.gamemodes;

import exceptions.GameNotInitializedException;
import exceptions.GameOverException;
import logic.*;
import logic.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.System;
import java.util.*;
import java.util.stream.Collectors;

public class BasicSnake implements Gamemode {

    private final Map map;
    private final List<Snake> snakes;

    private int loopCount;
    private boolean gameover = false;
    private boolean initialized = false;
    private long gameStartTime;
    private int gameMaxTime;
    private int timer;
    private final List<Snake> scheduledForRemoval = new ArrayList<>();
    private final JSONArray JSON_replace = new JSONArray();
    private final JSONObject JSON_synchronizationMessage = new JSONObject();
    private final JSONArray JSONArrayScores = new JSONArray();
    private final JSONObject JSONObjectGameover = new JSONObject();
    private final JSONObject JSONObjectWorld = new JSONObject();
    private final HashMap<Player, Integer> scores = new HashMap<>();
    private final List<Player> players;

    public BasicSnake(List<Player> players, Map map) {
        this.players = players;
        this.map = map;
        snakes = new ArrayList<>();
    }

    private JSONObject getWorld() {
        JSONObjectWorld.put("worldstring",map.toString());
        JSONObjectWorld.put("height",map.getHeight());
        JSONObjectWorld.put("width",map.getWidth());
        return JSONObjectWorld;
    }

    @Override
    public String gameLoop() throws GameOverException, GameNotInitializedException {
        if (gameover) throw new GameOverException();
        if (!initialized) throw new GameNotInitializedException();

        snakes.forEach(Snake::move);
        checkCollision();
        synchronizeScore();

        loopCount++;
        if (loopCount % 20 == 0) {
            spawnFood();
        }

        updateTimer();
        return getSynchronizationMessage();
    }

    @Override
    public String init() {

        // initial message
        JSON_synchronizationMessage.put("world", getWorld());

        //init GameTime
        gameMaxTime = 60000*players.size(); //1 Minute per player

        // create snakes
        List<Position> spawnPoints = map.getSpawnPoints();
        int size = Math.min(players.size(), spawnPoints.size());
        for (int i = 0; i < size; i++) {
            Snake snake = new Snake(spawnPoints.get(i), 5, players.get(i));
            snakes.add(snake);
        }

        //init Scores
        players.forEach(player -> scores.put(player,0));
        synchronizeScore();

        gameStartTime = System.currentTimeMillis();
        updateTimer();
        initialized = true;

        return getSynchronizationMessage();
    }

    @Override
    public java.util.Map<String, Integer> getScores() {
        // TODO: 03.01.2022 convert scores to Map<String, Integer> and return it
        return null;
    }


    private void spawnFood() {
        int width = map.getWidth();
        int height = map.getHeight();

        Position spawnPosition = new Position(0, 0);
        Material currentMaterial;

        do {
            spawnPosition.set(
                    (int) Math.floor(Math.random() * width),
                    (int) Math.floor(Math.random() * height)
            );
            currentMaterial = map.getMaterialAt(spawnPosition);
        } while (currentMaterial == Material.APPLE || currentMaterial == Material.WALL);
        map.changeMaterial(spawnPosition, Material.APPLE);
        jsonChangeMaterial(spawnPosition, Material.APPLE);
    }

    private String getSynchronizationMessage() {
        if (!JSON_replace.isEmpty()) {
            JSON_synchronizationMessage.put("replace", JSON_replace);
        }
        JSON_synchronizationMessage.put("snakes", printSnakes());

        JSON_synchronizationMessage.put("scores", printScores());

        if (!JSONObjectGameover.isEmpty()){
            JSON_synchronizationMessage.put("gameover",JSONObjectGameover);
        }
        JSON_synchronizationMessage.put("timer",getTimer());

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
        doesGameEnd();
        scheduledForRemoval.clear();

    }

    private void doesGameEnd() {
        if (timer<0){ //endif gameMaxTime is surpassed
            endGame();
        } else if (snakes.isEmpty()){ //Game ends when all snakes are dead
            endGame();
        }
    }

    private void endGame() {
        gameover = true;
        initialized = false;
        JSONObjectGameover.put("winner", getWinner());
    }

    private String getWinner() {
        String winner = "";
        int maxPoints = Collections.max(scores.values());
        boolean alreadyAWinner = false;
        for (java.util.Map.Entry<Player, Integer> entry : scores.entrySet()) {  // Iterate through hashmap
            if (entry.getValue()==maxPoints) {
                if (alreadyAWinner){
                    return "draw";
                }
                winner = entry.getKey().getName();
                alreadyAWinner = true;
            }
        }
        return winner; //should never be reached
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

    private JSONArray printScores(){
        JSONArrayScores.clear();
        scores.forEach((player, points) ->  {
            JSONObject score = new JSONObject();
            score.put("name",player.getName());
            score.put("points",points);
            JSONArrayScores.put(score);
        });
        return JSONArrayScores;
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

    private void raiseScoreForAll(int points){
        snakes.forEach(snake -> {
            scores.put(snake.getPlayer(),points);
        });
    }

    private void raiseScore(Snake snake,int points){
        scores.put(snake.getPlayer(),points);
    }

    private void synchronizeScore() {
        snakes.forEach(snake -> {
            raiseScore(snake, snake.length());
        });
    }

    private void updateTimer(){
        long passedTime = System.currentTimeMillis()-gameStartTime;
        timer = Math.round((gameMaxTime-passedTime)/1000f);
    }

    @Override
    public int getTimer() {
        return timer;
    }
}
