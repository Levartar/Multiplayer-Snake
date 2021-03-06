package networking.requests;

import networking.Lobby;
import networking.LobbyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class SelectMap extends HttpServlet {
    private static final Logger log = LogManager.getLogger(SelectMap.class);

    /**
     * changes the Map of the lobby with the corresponding joinCode to the given map
     * @param req request that gets read from the URL, should be joinCode and mapName
     * @param resp response that is send to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug(req);
        //extracts the joinCode from the request
        String parameter = req.getParameter("code");
        if (parameter == null) {
            log.warn("no 'code' parameter in " + req);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "no 'code' parameter");
            return;
        }
        int joinCode = Integer.parseInt(parameter);

        //extracts the mapName
        BufferedReader reader = req.getReader();
        String mapFileName = reader.readLine();
        log.debug("mapName: " + mapFileName);
        reader.close();

        try {
            Lobby lobby;
            try {
                lobby = LobbyManager.getLobby(joinCode);
                lobby.setMap(mapFileName);
                log.info("New Map selected: " + mapFileName);
            } catch (Exception e) {
                log.error(e.getMessage());
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Lobby does not exist");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "no map with name " + mapFileName);
        }
    }
}
