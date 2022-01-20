package networking;

import exceptions.GameNotInitializedException;
import exceptions.GameOverException;
import helpers.ResourceManager;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import logic.gamemodes.BasicSnake;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Lobby {
    private static final Logger log = LogManager.getLogger(Lobby.class);

    private final int joinCode;
    private final List<Player> players = new ArrayList<>();
    private final Set<Endpoint> endpoints = new CopyOnWriteArraySet<>();
    private boolean running = false;
    private Gamemode gamemode;
    private Map map;
    private String mapName;

    public Lobby(int joinCode) {
        this.joinCode = joinCode;
        createDefaultMap();
        gamemode = new BasicSnake(players, map);
    }

    public int getJoinCode() {
        return joinCode;
    }

    /**
     * @throws Exception if the lobby is full or the game is running
     */
    public void join(Endpoint endpoint) throws Exception {
        if (players.size() >= 4) {
            throw new Exception("The lobby is already full. lobby size = " + players.size());
        }
        if (running) {
            throw new Exception("The game is already running.");
        }
        Player player = endpoint.getPlayer();
        players.add(player);
        endpoints.add(endpoint);
        log.info("Player : " + player.getName() + " Successfully joined the Lobby with joincode " + joinCode);
    }

    public void removePlayer(Endpoint endpoint) {
        Player player = endpoint.getPlayer();
        players.remove(player);
        endpoints.remove(endpoint);
        log.info("Player : " + player.getName() + " Successfully removed from the Lobby with joincode " + joinCode);
    }

    public boolean hasPlayer(Endpoint endpoint) {
        return endpoints.contains(endpoint);
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        switch (gamemode) {
            case Gamemode.BASIC_SNAKE -> {
                this.gamemode = new BasicSnake(players, map);
                log.info("Gamemode " + this.gamemode.getClass().getName()
                        + " set for lobby with code " + joinCode);
            }
            default -> log.error("No such gamemode: " + gamemode);
        }
    }

    public void start() throws Exception {
        if (map == null) {
            createDefaultMap();
        }
        if (gamemode == null) {
            throw new Exception("Cannot start game. No Gamemode was selected.");
        }
        if (!isReadyToStart()) {
            throw new Exception("Not enough players or not every player is ready");
        }

        setPlayerColors();

        running = false;

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            try {
                String data;
                if (!running) {
                    log.info("initializing game...");
                    data = gamemode.init();
                    log.trace("init data: " + data);
                    running = true;
                } else {
                    data = gamemode.gameLoop();//if doesn't send a string throw exception
                    log.trace("gameLoop data: " + data);
                }
                for (Endpoint endpoint : endpoints) {
                    endpoint.send(data);
                }
            } catch (GameNotInitializedException e) {
                log.error(e.getMessage());
            } catch (GameOverException e) {
                log.info("Game ended from lobby " + joinCode);
                java.util.Map<String, Integer> highscores = gamemode.getScores();
                // TODO: 03.01.2022 send highscores data to database
                running = false;
                executor.shutdown();
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    private void setPlayerColors() {
        for (int i = 0; i < players.size(); i++) {

            String color;
            switch (i) {
                case 0 -> color = "red";
                case 1 -> color = "green";
                case 2 -> color = "blue";
                case 3 -> color = "magenta";
                default -> color = "grey";
            }

            players.get(i).setColor(color);
        }
    }

    public boolean hasStarted() {
        return running;
    }

    private void createDefaultMap() {
        try {
            this.mapName = "default";
            this.map = new Map(ResourceManager.getMapString(mapName));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public boolean isReadyToStart() {
        // TODO: 02.01.2022 check if all players are ready, Player needs a isReady() method
        return true;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setMap(String fileName) throws IOException {
        this.map = new Map(ResourceManager.getMapString(fileName));
        this.mapName = fileName;
        log.debug("Changed Map to: " + map + " for lobby " + joinCode);
    }
    public String getMapName(){
        return mapName;
    }

    public Map getMap(){
        return map;
    }

    @Override
    public String toString() {
        return "Lobby{" +
                "joinCode=" + joinCode +
                ", running=" + running +
                ", gamemode=" + gamemode.getClass().getName() +
                '}';
    }
}
