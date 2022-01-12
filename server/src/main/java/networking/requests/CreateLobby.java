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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException , IOException {
        log.info(req);

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
