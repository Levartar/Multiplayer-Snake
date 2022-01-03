package networking;

import exceptions.GameOverException;
import helpers.ResourceManager;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import logic.gamemodes.BasicSnake;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private final Set<CommunicationEndpoint> endpoints = new CopyOnWriteArraySet<>();
    private boolean running = false;
    private Gamemode gamemode;
    private Map map;

    public Lobby(int joinCode) {
        this.joinCode = joinCode;
        try {
            this.map = new Map(ResourceManager.getMapPath("BasicMap50x50"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public int getJoinCode() {
        return joinCode;
    }

    public void join(CommunicationEndpoint endpoint) throws Exception {
        if (players.size() >= 4) {
            throw new Exception("The lobby is already full. lobby size = " + players.size());
        }
        if (running) {
            throw new Exception("The game is already running.");
        }
        players.add(endpoint.getPlayer());
        endpoints.add(endpoint);
    }

    public void removePlayer(CommunicationEndpoint endpoint) {
        players.remove(endpoint.getPlayer());
        endpoints.remove(endpoint);
    }

    public boolean hasPlayer(CommunicationEndpoint endpoint) {
        return endpoints.contains(endpoint);
    }

    public void setGamemode(String gamemode) {
        switch (gamemode) {
            case "basic_snake" -> this.gamemode = new BasicSnake(players, map);
        }
    }

    public void start() throws Exception {
        if (gamemode == null) {
            throw new Exception("Cannot start game. No Gamemode was selected.");
        }
        if (!isReadyToStart()) {
            throw new Exception("Not enough players or not every player is ready");
        }

        running = true;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            String data = gamemode.gameLoop();
            try {
                for (CommunicationEndpoint endpoint : endpoints) {
                    endpoint.send(data);
                }
            } catch (GameOverException e) {
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
    }
}
