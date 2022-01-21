package networking.requests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
//import Database.SQLConnector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetHighscores extends HttpServlet {
    private static final Logger log = LogManager.getLogger(GetHighscores.class);

    private final JSONArray jsonArray = new JSONArray();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info(req);

        //executeMyQuery("Select * from highscores order by highscore DESC, "Database")
        // TODO: 19.01.2022 stream highscore data into JSON Object and send to frontend, X amount of entries

        // for every database entry
        addToJSON("sample", 100000);
        addToJSON("sample2", 42);

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
