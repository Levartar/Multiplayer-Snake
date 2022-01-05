package networking;

import exceptions.GameOverException;
import helpers.ResourceManager;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import logic.gamemodes.BasicSnake;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Database.SQLConnection;

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

    public Lobby(int joinCode) {
        this.joinCode = joinCode;
    }

    public int getJoinCode() {
        return joinCode;
    }

    /**
     *
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
        log.info("Player : "+ player.getName() +" Successfully joined the Lobby with joincode "  + joinCode);
    }

    public void removePlayer(Endpoint endpoint) {
        Player player = endpoint.getPlayer();
        players.remove(player);
        endpoints.remove(endpoint);
        log.info("Player : "+ player.getName() +" Successfully removed from the Lobby with joincode "  + joinCode);
    }

    public boolean hasPlayer(Endpoint endpoint) {
        return endpoints.contains(endpoint);
    }

    public void setGamemode(String gamemode) {
        switch (gamemode) {
            case "basic_snake" -> {
                this.gamemode = new BasicSnake(players, map);
                log.info("Gamemode " + this.gamemode.getClass().getName()
                        + " set for lobby with code " + joinCode);
            }
            default -> log.error("Wrong String input for Gamemode");
        }
    }

    public void start() throws Exception {
        if (gamemode == null) {
            throw new Exception("Cannot start game. No Gamemode was selected.");
        }
        if (map == null) {
            try {
                this.map = new Map(ResourceManager.getMapPath("BasicMap50x50"));
                log.info("Default Basic map created");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        if (!isReadyToStart()) {
            throw new Exception("Not enough players or not every player is ready");
        }

        running = true;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            try {
                String data = gamemode.gameLoop();//if doesn't send a string throw exception
                for (Endpoint endpoint : endpoints) {
                    endpoint.send(data);
                }
            } catch (GameOverException e) {
                log.info("Game ended from lobby " + joinCode);
                java.util.Map<String, Integer> highscores = gamemode.getScores();
                try{
                    SQLConnection.connectToServer("testdb");
                    log.info("Successfully connected to Database: ");
                }catch(Exception ex){
                    log.error("Couldn't connect to Database : " + ex.getMessage());
                }
                highscores.forEach((key,value) -> {
                    // TODO: 05.01.2022 insert the highscores of the players
                    System.out.println(key + " " + value);
                });
                SQLConnection.getAllDBNames();
                try{
                    SQLConnection.closeConnections();
                    log.info("Successfully disconnected from Database: ");
                }catch(Exception exp){
                    log.error(exp.getMessage());
                }
                running = false;
                executor.shutdown();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public boolean isReadyToStart() {
        // TODO: 02.01.2022 check if all players are ready, Player needs a isReady() method
        return false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setMap(Map map) {
        this.map = map;
        log.debug("Changed Map to: " + map + " for lobby " + joinCode);
    }
}
