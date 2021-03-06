package logic.gamemodes;

import exceptions.GameNotInitializedException;
import exceptions.GameOverException;
import exceptions.GameRunningException;
import logic.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BasicSnake implements Gamemode {
    private static final Logger log = LogManager.getLogger(BasicSnake.class);

    private final List<Snake> snakes = new ArrayList<>();
    private final List<Snake> scheduledForRemoval = new ArrayList<>();
    private final List<Player> players;
    private final java.util.Map<Player, Integer> scores = new HashMap<>();
    private final JSONArray JSONReplace = new JSONArray();
    private final JSONObject JSONSynchronizationMessage = new JSONObject();
    private final JSONArray JSONArrayScores = new JSONArray();
    private final JSONObject JSONObjectGameOver = new JSONObject();
    private final JSONObject JSONObjectWorld = new JSONObject();
    private Map originalMap;
    private Map currentMap;

    private boolean gameOver = false;
    private boolean initialized = false;

    private int loopCount;

    private long lastFrameSystemTime;
    private int timeLeft;
    private int countDown;
    private final int initialCountDown;

    /**
     * Constructor
     * @param players List of all players who are playing in this round
     * @param map the actual chosen map for this round
     * @param countDown Countdown until start of round
     */
    public BasicSnake(List<Player> players, Map map, int countDown) {
        this.players = players;
        try {
            setMap(map);
        } catch (GameRunningException e) {
            log.error(e.getMessage());
        }
        this.initialCountDown = countDown * 1000;
        log.debug("BasicSnake created\n" + this);
    }

    /**
     * Constructor for testing
     * @param players List of testplayers
     * @param map Test map
     */
    public BasicSnake(List<Player> players, Map map) {
        this(players, map, 0);
    }

    /**
     * Returns in form of a String the initial synchronisation message
     * @return String that contains the initial information of the round
     */
    @Override
    public String init() {

        // reset map
        this.currentMap = new Map(this.originalMap);

        // reset player input
        for (Player p: this.players) {
            p.setInput(' ');
        }

        // initial message has world
        JSONSynchronizationMessage.put("world", jsonGetWorld());

        // remove game over message
        JSONObjectGameOver.clear();

        // init GameTime
        countDown = initialCountDown;
        timeLeft = 60000 * players.size() + countDown; // 1 Minute per player + CountDown

        // create snakes
        snakes.clear();
        List<Position> spawnPoints = currentMap.getSpawnPoints();
        int size = Math.min(players.size(), spawnPoints.size());
        for (int i = 0; i < size; i++) {
            Snake snake = new Snake(spawnPoints.get(i), 5, players.get(i));
            snakes.add(snake);
        }

        //init Scores
        scores.clear();
        players.forEach(player -> scores.put(player, 0));
        updateScore();

        lastFrameSystemTime = System.currentTimeMillis();
        updateTime();

        initialized = true;
        gameOver = false;

        log.debug("BasicSnake initialized: snakes: " + snakes + ", game time (millis): " + timeLeft);

        return getSynchronizationMessage();
    }

    /**
     * Represents one step for all snakes and all that could happen during this one step.
     * @return The synchronisation message after all changes that happens during one step
     * @throws GameOverException ...
     * @throws GameNotInitializedException ...
     */
    @Override
    public String gameLoop() throws GameOverException, GameNotInitializedException {
        if (gameOver) throw new GameOverException();
        if (!initialized) throw new GameNotInitializedException();
        log.trace("updating time...");
        updateTime();
        log.trace("countdown in milliseconds: " + countDown);
        if (countDown <= 0) {
            update();
        } else {
            log.trace("countdown: " + (int) Math.ceil(countDown / 1000f));
            log.trace("countdown in milliseconds: " + countDown);
        }
        log.trace("getting synchronization message...");
        String message = getSynchronizationMessage();
        log.trace("game loop done:\n" +
                "world state:\n" + this + "\n" +
                "players: " + players.toString() + "\n" +
                "scores: " + scores + "\n" +
                "time left (millis): " + timeLeft + "\n" +
                "synchronization message: " + message);
        return message;
    }

    /**
     * moves all snakes, checks for collision, updates the scores and spawns apples
     */
    private void update() {
        loopCount++;
        if (loopCount % (15 / snakes.size()) == 0) {
            spawnFood();
        }

        log.trace("moving snakes...");
        snakes.forEach(Snake::move);
        log.trace("checking collisions...");
        checkCollisions();
        log.trace("updating score...");
        updateScore();
    }

    /**
     * spawning the apples
     */
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
        } while (currentMaterial != Material.FREESPACE);

        currentMap.changeMaterial(spawnPosition, Material.APPLE);
        jsonChangeMaterial(spawnPosition, Material.APPLE);
        log.trace("food spawned at " + spawnPosition);
    }

    /**
     * Checking if a snake hit a wall, itself or another snake.
     */
    private void checkCollisions() {
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

            // collision with its own body
            snake.getPositions().forEach(position -> {
                if (position != head && position.equals(head)) {
                    log.debug(snake + " collided with its own body at " + head);
                    scheduledForRemoval.add(snake);
                }
            });

            // collisions with another snake
            snakes.forEach(otherSnake -> {
                if (otherSnake == snake) return;
                if (otherSnake == null) return;
                if (otherSnake.getPositions().contains(head)) {
                    log.debug(snake + " collided with another snake at " + head);
                    scheduledForRemoval.add(snake);
                }
            });

            // apple collisions
            if (currentMap.getMaterialAt(head) == Material.APPLE) {
                snake.grow(1);
                currentMap.changeMaterial(head, Material.FREESPACE);
                jsonChangeMaterial(head, Material.FREESPACE);
            }else if (currentMap.getMaterialAt(head) == Material.APPLETREE){
                snake.grow(1);
            }
        });

        scheduledForRemoval.forEach(this::snakeToApples);
        scheduledForRemoval.forEach(snake -> log.info("snake " + snake.getName() + " died"));
        scheduledForRemoval.forEach(snakes::remove);
        gameEnds();
        scheduledForRemoval.clear();
    }

    /**
     * If a snake dies it body will converts into apples
     * @param snake The snake that died
     */
    private void snakeToApples(Snake snake) {
        List<Position> newApples = snake.getPositions();
        newApples.forEach(position -> {
            if (currentMap.getMaterialAt(position) == Material.FREESPACE) {
                currentMap.changeMaterial(position, Material.APPLE);
                jsonChangeMaterial(position, Material.APPLE);
            }
        });

    }

    /**
     * Checking if the game ends because time is over or all snakes are dead
     */
    private void gameEnds() {
        if (timeLeft < 0) { //endif gameMaxTime is surpassed
            log.debug("time is up");
            endGame();
        } else if (snakes.isEmpty()) { //Game ends when all snakes are dead
            log.debug("Game ends, because there are no snakes");
            endGame();
        }
    }

    /**
     * Ending the round
     */
    private void endGame() {
        gameOver = true;
        initialized = false;
        JSONObjectGameOver.put("winner", getWinner());
        log.info("Game Ends winner: " + getWinner());
    }

    /**
     *
     * @return The player that has won this round
     */
    private String getWinner() {
        String winner = "";
        int maxPoints = Collections.max(scores.values());
        boolean alreadyAWinner = false;
        for (java.util.Map.Entry<Player, Integer> entry : scores.entrySet()) {  // Iterate through hashmap
            if (entry.getValue() == maxPoints) {
                if (alreadyAWinner) {
                    return "draw";
                }
                winner = entry.getKey().getName();
                alreadyAWinner = true;
            }
        }
        return winner; //should never be reached
    }

    /**
     * Updates the score for all snakes
     */
    private void updateScore() {
        snakes.forEach(snake -> setScore(snake, snake.getLength()));
    }

    /**
     * Updates the score of a snake if e.g. it ate a apple
     * @param snake The {@link Snake} instance that gets the points
     * @param points amount of points to be added
     */
    private void setScore(Snake snake, int points) {
        scores.put(snake.getPlayer(), points);
        log.trace("set points for " + snake + " :" + points + " points");
    }

    /**
     * decreases the countdown and the time left.
     */
    private void updateTime() {
        long now = System.currentTimeMillis();
        int delta = (int) (now - lastFrameSystemTime);
        lastFrameSystemTime = now;
        log.trace("delta (millis) = " + delta);

        timeLeft -= delta;
        countDown -= delta;
        log.trace("time left (millis): " + timeLeft);
    }

    /**
     * The Synchronization Message
     * @return JSON object containing all needed information for the frontend
     */
    private String getSynchronizationMessage() {
        if (countDown > 0) {
            JSONSynchronizationMessage.put("countdown", Math.ceil(countDown / 1000f));
        }
        if (!JSONReplace.isEmpty()) {
            JSONSynchronizationMessage.put("replace", JSONReplace);
        }
        JSONSynchronizationMessage.put("snakes", jsonGetSnakes());

        JSONSynchronizationMessage.put("scores", jsonGetScores());

        if (!JSONObjectGameOver.isEmpty()) {
            JSONSynchronizationMessage.put("gameover", JSONObjectGameOver);
        }

        JSONSynchronizationMessage.put("timer", getTimeLeft());

        String message = JSONSynchronizationMessage.toString();
        JSONSynchronizationMessage.clear();
        JSONReplace.clear();

        return message;
    }

    /**
     *
     * @return JSON object with all information about a map (String, height, width)
     */
    private JSONObject jsonGetWorld() {
        JSONObjectWorld.put("worldstring", currentMap.toString());
        JSONObjectWorld.put("height", currentMap.getHeight());
        JSONObjectWorld.put("width", currentMap.getWidth());
        return JSONObjectWorld;
    }

    /**
     *
     * @return JSON object containing all information about all snakes.
     */
    private JSONArray jsonGetSnakes() {
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

    /**
     *
     * @return Returns the scores of all players in a JSON object.
     */
    private JSONArray jsonGetScores() {
        JSONArrayScores.clear();
        scores.forEach((player, points) -> {
            JSONObject score = new JSONObject();
            score.put("name", player.getName());
            score.put("points", points);
            JSONArrayScores.put(score);
        });
        return JSONArrayScores;
    }

    /**
     * Putting all to be changed positions into a JSON object
     * @param position The {@link Position} where to change the {@link Material}
     * @param material The new {@link Material}
     */
    private void jsonChangeMaterial(Position position, Material material) {
        // bad performance, because many objects are created
        JSONObject materialChange = new JSONObject();
        JSONObject pos = new JSONObject();
        pos.put("x", position.getX());
        pos.put("y", position.getY());
        materialChange.put("pos", pos);
        materialChange.put("mat", material.toString());
        JSONReplace.put(materialChange);
    }

    /**
     * Allowing to change the map in the lobby screen
     * @param map The {@link Map} to be used
     * @throws GameRunningException if you try to set a map while a game is running
     */
    @Override
    public void setMap(Map map) throws GameRunningException {
        if (initialized) throw new GameRunningException();

        this.originalMap = map;
        this.currentMap = new Map(map);
    }

    /**
     *
     * @return Time left for this round in seconds.
     */
    @Override
    public int getTimeLeft() {
        return (int) Math.ceil(timeLeft / 1000f);
    }

    /**
     *
     * @return Hashmap that contains the actual score of all players
     */
    @Override
    public java.util.Map<String, Integer> getScores() {
        java.util.Map<String, Integer> scoreMap = new HashMap<>();
        scores.forEach((player, score) -> scoreMap.put(player.getName(), score));
        return scoreMap;
    }

    /**
     * Used for testing and the Swing application
     * @return String with map and snakes
     */
    @Override
    public String toString() {
        Material[][] tmp = currentMap.getMap();

        // add snakes to tmp
        snakes.forEach(snake -> {
            Position head = snake.getHead();
            snake.getPositions().forEach(position -> {
                int x = position.getX();
                int y = position.getY();

                // if the current position is the head -> write H
                if (position == head) {
                    tmp[x][y] = Material.SNAKEHEAD;
                } else if (!position.equals(head)) { // if the head is at the current position -> don't override
                    tmp[x][y] = Material.SNAKE;
                }
            });
        });

        StringBuilder result = new StringBuilder();
        for (int y = 0; y < tmp[0].length; y++) {
            for (Material[] materials : tmp) {
                result.append(materials[y].toString());
            }
            result.append("\n");
        }

        // delete '\n' at the end
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }
}
