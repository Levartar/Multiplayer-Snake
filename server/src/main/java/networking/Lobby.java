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
import database.SQLConnection;

import java.io.IOException;
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

    /**
     * Constructor to create a new Lobby Object
     * @param joinCode the code for the new lobby
     */
    public Lobby(int joinCode) {
        this.joinCode = joinCode;
        createDefaultMap();
        gamemode = new BasicSnake(players, map, 3);
    }

    /**
     * return the joinCode of the current Lobby
     * @return int lobby joincode
     */
    public int getJoinCode() {
        return joinCode;
    }

    /**
     * if the lobby is joinable adds the Player and the endpoint to the corresponding lists
     * @param endpoint corresponding websocket client
     * @throws Exception lobby full or running
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

    /**
     * removes the player and endpoint object from the lists in Lobby
     * @param endpoint corresponding websocket client
     */
    public void removePlayer(Endpoint endpoint) {
        Player player = endpoint.getPlayer();
        players.remove(player);
        endpoints.remove(endpoint);
        log.info("Player : " + player.getName() + " Successfully removed from the Lobby with joincode " + joinCode);
    }

    /**
     * returns true if the given endpoint is already in the list of joined endpoints
     * @param endpoint corresponding websocket client
     * @return boolean true if endpoint already in lobby
     */
    public boolean hasPlayer(Endpoint endpoint) {
        return endpoints.contains(endpoint);
    }

    /**
     * returns the current gamemode
     * @return Gamemode
     */
    public Gamemode getGamemode() {
        return gamemode;
    }

    /**
     *
     * @throws Exception
     */
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

        map.shuffleSpawnPoints();
        gamemode.setMap(map);

        //setPlayerColors();

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

                    for (Endpoint endpoint : endpoints) {
                        endpoint.send(data);
                    }
                }

                data = gamemode.gameLoop();//if doesn't send a string throw exception
                log.trace("gameLoop data: " + data);

                for (Endpoint endpoint : endpoints) {
                    endpoint.send(data);
                }
            } catch (GameNotInitializedException e) {
                log.error(e.getMessage());
            } catch (GameOverException e) {
                log.info("Game ended from lobby " + joinCode);
                java.util.Map<String, Integer> highscores = gamemode.getScores();
                try(var sqlConnection = new SQLConnection()){

                    for (var score:highscores.entrySet()) {
                        sqlConnection.insertSnakeHighscore(score.getKey(),score.getValue());
                    }

                }catch(Exception ex){
                    log.error(ex.getMessage());
                }
                running = false;
                executor.shutdown();
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * gives each player of the list a different color
     */
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

    /**
     * returns true if the game is running if not false
     * @return boolean running
     */
    public boolean hasStarted() {
        return running;
    }

    /**
     *
     */
    private void createDefaultMap() {
        try {
            this.mapName = "default";
            this.map = new Map(ResourceManager.getMapString(mapName));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * looks if all player endpoints signal that they are ready
     * @return boolean true if all players are ready
     */
    public boolean isReadyToStart() {
        // TODO: 02.01.2022 check if all players are ready, Player needs a isReady() method
        return true;
    }

    /**
     * returns the List of players
     * @return Player List
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * changes the current map with a new map that was selected
     * @param fileName of the Map string
     * @throws IOException
     */
    public void setMap(String fileName) throws IOException {
        this.map = new Map(ResourceManager.getMapString(fileName));
        this.mapName = fileName;
        log.debug("Changed Map to: " + map + " for lobby " + joinCode);
    }

    /**
     * returns the name of the selected map
     * @return String mapName
     */
    public String getMapName(){
        return mapName;
    }

    /**
     * returns the selected map
     * @return Map
     */
    public Map getMap(){
        return map;
    }

    /**
     * overwritten toString method. Displays the important lobby infos
     * @return String LobbyInfos
     */
    @Override
    public String toString() {
        return "Lobby{" +
                "joinCode=" + joinCode +
                ", running=" + running +
                ", gamemode=" + gamemode.getClass().getName() +
                '}';
    }
}
