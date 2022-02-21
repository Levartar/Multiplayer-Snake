package networking.requests;

import networking.LobbyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateLobby extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CreateLobby.class);

    /**
     * creates a Lobby object and sends the joinCode as a response to the frontend
     * @param req request that gets read from the URL, should be empty
     * @param resp response that is send to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException , IOException {
        log.debug(req);

        int lobbyJoinCode = LobbyManager.createLobby();
        try {
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().print(lobbyJoinCode);
            resp.getWriter().close();
        }
    }
}
