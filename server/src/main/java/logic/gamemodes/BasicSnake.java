package logic.gamemodes;

import exceptions.GameNotInitializedException;
import exceptions.GameOverException;
import logic.*;
import logic.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.System;
import java.util.*;

public class BasicSnake implements Gamemode {

    private static final Logger log = LogManager.getLogger(BasicSnake.class);

    private final Map originalMap;
    private Map currentMap;
    private final List<Snake> snakes;

    private int loopCount;
    private boolean gameover = false;
    private boolean initialized = false;
    private long gameStartTime;
    private int gameMaxTime;
    private int timer;
    private int countDown;
    private long maxCountdown;
    private final List<Snake> scheduledForRemoval = new ArrayList<>();
    private final JSONArray JSON_replace = new JSONArray();
    private final JSONObject JSON_synchronizationMessage = new JSONObject();
    private final JSONArray JSONArrayScores = new JSONArray();
    private final JSONObject JSONObjectGameover = new JSONObject();
    private final JSONObject JSONObjectWorld = new JSONObject();
    private final HashMap<Player, Integer> scores = new HashMap<>();
    private final List<Player> players;

    public BasicSnake(List<Player> players, Map map, int countDown) {
        this.players = players;
        this.originalMap = map;
        this.currentMap = new Map(map);
        this.maxCountdown = countDown*1000;
        snakes = new ArrayList<>();
    }

    public BasicSnake(List<Player> players, Map map) {
        this.players = players;
        this.originalMap = map;
        snakes = new ArrayList<>();
        log.debug("BasicSnake created\n"+this);
    }

    private JSONObject getWorld() {
        JSONObjectWorld.put("worldstring", currentMap.toString());
        JSONObjectWorld.put("height", currentMap.getHeight());
        JSONObjectWorld.put("width", currentMap.getWidth());
        return JSONObjectWorld;
    }

    @Override
    public String gameLoop() throws GameOverException, GameNotInitializedException {
        if (gameover) throw new GameOverException();
        if (!initialized) throw new GameNotInitializedException();
        if (countDown>0){
            sendCountDown();
        } else {

            snakes.forEach(Snake::move);
            checkCollision();
            synchronizeScore();

            loopCount++;
            if (loopCount % (15 / snakes.size()) == 0) {
                spawnFood();
            }
        }
        updateTimer();
        log.debug("\nGamemode:\n"+this+"\nplayers:\n"+players.toString()+"\nscores:\n"+scores.toString()+"\nTimer:\n"+timer);
        return getSynchronizationMessage();
    }

    @Override
    public String init() {

        log.debug("Init BasicSnake:");
        this.countDown = 0;

        // reset map
        this.currentMap = new Map(this.originalMap);

        // initial message
        JSON_synchronizationMessage.put("world", getWorld());

        // remove game over message
        JSONObjectGameover.clear();

        //init GameTime
        gameMaxTime = (int) (60000*players.size()+maxCountdown); //1 Minute per player + CountDown in ms
        log.debug("set GameTime to:"+gameMaxTime/1000f);

        // create snakes
        List<Position> spawnPoints = currentMap.getSpawnPoints();
        int size = Math.min(players.size(), spawnPoints.size());
        for (int i = 0; i < size; i++) {
            Snake snake = new Snake(spawnPoints.get(i), 5, players.get(i));
            snakes.add(snake);
        }
        log.debug("snakes created: " + snakes);

        //init Scores
        players.forEach(player -> scores.put(player,0));
        synchronizeScore();

        gameStartTime = System.currentTimeMillis();
        log.debug("started at: "+new Date(gameStartTime));
        updateTimer();

        initialized = true;
        gameover = false;

        return getSynchronizationMessage();
    }

    private void sendCountDown() {
        JSON_synchronizationMessage.put("countdown",countDown);
    }

    @Override
    public java.util.Map<String, Integer> getScores() {
        // TODO: 03.01.2022 convert scores to Map<String, Integer> and return it
        return null;
    }


    private void spawnFood() {
        int width = currentMap.getWidth();
        int height = currentMap.getHeight();

        Position spawnPosition = new Position(0, 0);
        Material currentMaterial;

        do {
            spawnPosition.set(
                    (int) Math.floor(Math.random() * width),
                    (int) Math.floor(Math.random() * height)
            );
            currentMaterial = currentMap.getMaterialAt(spawnPosition);
        } while (currentMaterial == Material.APPLE || currentMaterial == Material.WALL);
        currentMap.changeMaterial(spawnPosition, Material.APPLE);
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
            if (currentMap.getMaterialAt(head) == Material.WALL) {
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
            if (currentMap.getMaterialAt(head) == Material.APPLE) {
                snake.grow(1);
                currentMap.changeMaterial(head, Material.FREESPACE);
                jsonChangeMaterial(head, Material.FREESPACE);
            }
        });

        scheduledForRemoval.forEach(snake -> snakeToApples(snake));
        scheduledForRemoval.forEach(snake -> log.info("snake "+snake.getName()+" died"));
        scheduledForRemoval.forEach(snakes::remove);
        doesGameEnd();
        scheduledForRemoval.clear();

    }

    private void snakeToApples(Snake snake) {
        List<Position> newApples = snake.getPositions();
        newApples.forEach(position -> {
            if (currentMap.getMaterialAt(position) == Material.FREESPACE) {
                currentMap.changeMaterial(position, Material.APPLE);
                jsonChangeMaterial(position, Material.APPLE);
            }
        });

    }

    private void doesGameEnd() {
        if (timer<0){ //endif gameMaxTime is surpassed
            log.debug("time is up");
            endGame();
        } else if (snakes.isEmpty()){ //Game ends when all snakes are dead
            log.debug("Game ends, because there are no snakes");
            endGame();
        }
    }

    private void endGame() {
        gameover = true;
        initialized = false;
        JSONObjectGameover.put("winner", getWinner());
        log.info("Game Ends winner: "+getWinner());
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
            snakeObject.put("color", snake.getPlayer().getColor());
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
        Material[][] tmp;
        if (currentMap == null) {
            tmp = originalMap.getMap();
        } else {
            tmp = currentMap.getMap();
        }

        // add snakes
        snakes.forEach(snake -> {
            Position head = snake.getHead();
            snake.getPositions().forEach(position -> {
                int x = position.getX();
                int y = position.getY();

                // if the current position is the head -> write H
                if (position == head) {
                    tmp[x][y] = Material.SNAKEHEAD;
                    // if the current position is at the position of the head -> don't override the head symbol
                } else if (position.equals(head)) {
                    return;
                } else {
                    tmp[x][y] = Material.SNAKE;
                }
            });
        });
        StringBuilder result = new StringBuilder();
        for (int y = 0; y < tmp[0].length; y++) {
            for (int x = 0; x < tmp.length; x++) {
                result.append(tmp[x][y].toString());
            }
            result.append("\n");
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
        log.trace("set Points to: "+snake.getName()+":"+points+" points");
    }

    private void synchronizeScore() {
        snakes.forEach(snake -> {
            raiseScore(snake, snake.length());
        });
    }

    private void updateTimer(){
        long passedTime = System.currentTimeMillis()-gameStartTime;
        timer = Math.round((gameMaxTime-passedTime)/1000f);
        countDown = Math.round((maxCountdown-passedTime)/1000f);
        log.trace("Timer: "+timer);
    }

    @Override
    public int getTimer() {
        return timer;
    }
}
