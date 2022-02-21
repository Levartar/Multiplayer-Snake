package networking.requests;

import database.SQLConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;

public class GetHighscores extends HttpServlet {
    private static final Logger log = LogManager.getLogger(GetHighscores.class);

    private final JSONArray jsonArray = new JSONArray();

    /**
     * sends the top 20 scores as a JSONArray to the frontend
     * @param req request that gets read from the URL, should be empty
     * @param resp response that is send to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info(req);

        try (var sqlConnection = new SQLConnection()){
            var scores = sqlConnection.getScores(20);
            scores.forEach(this::addToJSON);

        }catch (NullPointerException nullE){
            log.error("No resultset found!" + nullE);
            resp.sendError(500,"Database not found!");
        }catch(Exception e){
            log.error("External for loop Error:  " + e.getMessage());
        }
        try {
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().print(jsonArray);
            jsonArray.clear();
            resp.getWriter().close();
        }
    }

    private void addToJSON(final String name, final int score) {
        JSONObject entry = new JSONObject();
        entry.put("name", name);
        entry.put("score", score);
        jsonArray.put(entry);
    }
}
