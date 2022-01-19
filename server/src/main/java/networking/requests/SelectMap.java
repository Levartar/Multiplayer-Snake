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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info(req);

        String parameter = req.getParameter("code");
        if (parameter == null) {
            log.warn("no 'code' parameter in " + req);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "no 'code' parameter");
            return;
        }
        int joinCode = Integer.parseInt(parameter);

        BufferedReader reader = req.getReader();
        String mapFileName = reader.readLine();
        log.info("mapName: " + mapFileName);
        reader.close();

        try {
            Lobby lobby;
            try {
                lobby = LobbyManager.getLobby(joinCode);
                lobby.setMap(mapFileName);
                log.info("New Map selected.");
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
