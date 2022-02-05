package networking.requests;

import exceptions.NoSuchLobbyException;
import helpers.ResourceManager;
import logic.Gamemode;
import logic.Player;
import logic.gamemodes.BasicSnake;
import networking.Lobby;
import networking.LobbyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GetGameInfo extends HttpServlet {
    private static final Logger log = LogManager.getLogger(GetGameInfo.class);

    /**
     * formulates a message containing important information about the lobby and sends it to the frontend
     * @param req request that gets read from the URL, should contain the joinCode
     * @param resp response that is send to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info(req);

        //extracts the joinCode from the request
        String parameter = req.getParameter("code");
        if (parameter == null) {
            log.warn("no 'code' parameter in " + req);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "no 'code' parameter");
            return;
        }
        int joinCode = Integer.parseInt(parameter);
        Lobby lobby;
        //creates a JSONObject containing important information about a lobby
        JSONObject jsonMessage = new JSONObject();
        try {
            lobby = LobbyManager.getLobby(joinCode);

            jsonMessage.put("exists", true);

            boolean hasStarted = lobby.hasStarted();
            jsonMessage.put("hasStarted", hasStarted);

            List<Player> players = lobby.getPlayers();
            List<String> playerNames = players.stream().map(Player::getName).collect(Collectors.toList());
            JSONArray jsonPlayerNames = new JSONArray();
            playerNames.forEach(jsonPlayerNames::put);
            jsonMessage.put("playerNames", jsonPlayerNames);

            Gamemode gamemode = lobby.getGamemode();
            if (gamemode instanceof BasicSnake) jsonMessage.put("gamemode", Gamemode.BASIC_SNAKE);
            else if (gamemode == null) log.warn("gamemode is null, lobby: " + lobby);
            else log.warn("gamemode is unknown, add a new if clause here");

            String map = lobby.getMapName();
            jsonMessage.put("selectedMap", map);

            try {
                jsonMessage.put("mapNames", ResourceManager.getMapNames());
            } catch (IOException e) {
                log.error(e.getMessage());
            }

        } catch (NoSuchLobbyException e) {
            log.warn(e.getMessage());
            jsonMessage.put("exists", false);
        } finally {
            log.info("jsonMessage = " + jsonMessage);
        }

        try {
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().print(jsonMessage);
            resp.getWriter().close();
        }
    }
}
