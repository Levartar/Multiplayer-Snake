package networking.requests;

import Database.SQLConnection;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info(req);
        ResultSet resultSet = SQLConnection.getScores();
        try{
            for (int i = 0; i < 20 &&resultSet.next(); i++) {
                try{
                    addToJSON(resultSet.getString("player_name"), Integer.parseInt(resultSet.getString("score")));
                }catch(Exception e){
                    log.error("For loop Error: " + e.getMessage());
                }
            }
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