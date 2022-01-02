package networking;

import exceptions.GameOverException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Lobby {

    private final int joinCode;
    private final List<Player> players = new ArrayList<>();
    private final Set<CommunicationEndpoint> endpoints = new CopyOnWriteArraySet<>();
    private boolean running = false;
    private Gamemode gamemode;


    public Lobby(int joinCode, Gamemode gamemode) {
        this.joinCode = joinCode;
        this.gamemode = gamemode;
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
        players.add(CommunicationEndpoint.getPlayer());
        endpoints.add(endpoint);
    }

    public void removePlayer(CommunicationEndpoint endpoint) {
        players.remove(endpoint.getPlayer());
        endpoints.remove(endpoint);
    }

    public boolean hasPlayer(CommunicationEndpoint endpoint) {
        return endpoints.contains(endpoint);
    }

    public void setGamemode(Gamemode gamemode) {
        // TODO: 02.01.2022
    }

    public void start() throws Exception {
        if (!isReadyToStart()) {
            throw new Exception("Not enough players or not every player is ready");
        }

        running = true;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            try {
                String data = gamemode.gameLoop();
                endpoints.forEach(endpoint -> endpoint.send(data)); // TODO: 02.01.2022 send method
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

}
