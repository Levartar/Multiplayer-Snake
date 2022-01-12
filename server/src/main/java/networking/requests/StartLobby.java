package networking.requests;

import exceptions.NoSuchLobbyException;
import networking.Lobby;
import networking.LobbyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StartLobby extends HttpServlet {
    private static final Logger log = LogManager.getLogger(StartLobby.class);

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
        Lobby lobby;

        try {
            lobby = LobbyManager.getLobby(joinCode);
            try {
                lobby.start();
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                log.warn(e.getMessage());
                resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
            }
        } catch (NoSuchLobbyException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Lobby does not exist");
        }

    }
}
