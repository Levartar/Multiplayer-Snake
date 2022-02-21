package database;

import java.io.IOException;
import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


public final class SQLConnection implements AutoCloseable {
    private static final Logger log = LogManager.getLogger(SQLConnection.class);

    private Connection connection;
    private String DBUser;
    private String DBUserPW;
    private String DBIP;
    private Statement stmt;
    static String databasename = "testdb";
    private static int localPort = 3360;



    /**
     * Constructor for the SQL Connection
     */
    public SQLConnection() {
        setLoginDetails();
        connectToDataBase();
    }


    /**
     * Sets the Connection Variables via System environment variables for Docker CICD pipelines.
     * if no enviroment variables are found, a local Database is expected for test purposes and can be connected via e.g. DockerDesktop.
     */
    private void setLoginDetails() {
        DBUser = System.getenv("DBUserName");
        DBUserPW = System.getenv("DBUserPassword");
        DBIP = System.getenv("Server_IP");

        if (DBIP == null || DBIP.equals("")) {
            DBIP = "localhost";
            String hostname = System.getenv("DATABASE_HOSTNAME");
            if (hostname == null || hostname.equals("")) hostname = "localhost";
            DBIP = hostname;
            DBUser = "root";
            DBUserPW = System.getenv("rootPW");
            if (DBUserPW == null || DBUserPW.equals("")) {
                DBUserPW = "testpw!";
                log.info("rootPW not found! set as normal.");
            }
            log.info("DB expected locally");
            localPort = 3306;
        }

    }

    /**
     * Connection Methode
     * when the connection has a timeout an Exception will be thrown
     * and caught so the game can also run, but the Highscore won´t get saved.
     * This is also shown in the Frontend See: {@link networking.requests.GetHighscores#doGet(HttpServletRequest, HttpServletResponse)networking}
     * JDBC Driver: Mariadb
     */
    private void connectToDataBase() {

        String url = "jdbc:mariadb://" + DBIP + ":" + localPort + "/" + databasename;

        try {
            connection = DriverManager.getConnection(url, DBUser, DBUserPW);
            if (connection == null) {
                throw new SQLException("Cannot connect to Database !");

            }
            stmt = connection.createStatement();
            log.info("Connection to Database server successful!:" + DBIP);
        } catch (SQLException e) {

            log.error("connectToDataBase Error: " + e.getMessage());
        }
    }


    /**
     *created the SQL Statement based on the Input
     * the ? are for the prepared statment.
     * @param name the name of the Player
     * @param highscore the player´s highscore
     */
    public void insertSnakeHighscore(String name, int highscore) {
        String statement = "INSERT IGNORE INTO `Highscore`" +
                " SET `score_id` = NULL," +
                "`player_name` =  ?, " + "`score` = ?";
        insertStatement(statement, name, highscore);
    }


    /**
     * Checks name and highscore and if an connection and statement is available
     * Creates a new PreparedStatement, inserts the values(name,highscore) and executes it.
     * When successful the statement and the amount of affected rows will be logged
     * @param statement the statement which should be executed
     * @param name the player´s name
     * @param highscore his highscore
     */
    private void insertStatement(String statement, String name, int highscore) {
        try {
            if (highscore < 0 ) {
                throw new IllegalArgumentException("Highscore must be positiv or 0!");
            }
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Name can´t be null or blank!");
            }

            if (connection == null || stmt == null) {
                throw new SQLException("Cannot connect to Database !");
            }
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, name);
            ps.setInt(2, highscore);
            int rowAffected = ps.executeUpdate();
            log.debug(rowAffected + "rows affected");
            log.info("InsertStatement executed " + ps + "| Rows affected:" + rowAffected);
        } catch (SQLException e) {
            log.error("InsertStatement Error: " + e);
        }
    }


    /**
     * Not yet implemented in the Backend / Frontend
     * deletes all entries from a player based on his name
     * @param name the players name of which entries should be deleted
     * @return the amount of rows which has been changed
     */
    public int deleteHighscore(String name) {
        String delete = "delete from " + databasename + ".`Highscore` where `player_name` LIKE ?;";
        int result = 0;
        try {
            if (connection == null || stmt == null) {
                throw new SQLException("Cannot connect to Database !");
            }
            PreparedStatement ps = connection.prepareStatement(delete);
            ps.setString(1, "%" +name+"%");
            result = ps.executeUpdate();
            log.info("Database connection success");
            log.info("Deleted rows: " + result);
            return result;
        } catch (SQLException e) {
            log.error("deleteHighscore Error: " + e.getMessage());
            return result;
        }
    }


    /**
     * Return the first (best) playernames and scores in a Map
     * @param maxCount How many entries should be given back
     * @return a Map which contains the player name (Key) and the score(value)
     */
    public Map<String, Integer> getScores(int maxCount) {
        if (maxCount <= 0) {
            return new HashMap<>();
        }


        String statement = "Select * from testdb.Highscore order by score DESC";
        ResultSet resultSet;

        try {
            if (connection == null || stmt == null) {
                throw new SQLException("Cannot connect to Database !");
            }

            resultSet = stmt.executeQuery(statement);
            Map<String, Integer> scores = new HashMap<>();
            int i = 0;
            while (resultSet.next() && i < maxCount) {
                scores.put(resultSet.getString("player_name"), Integer.parseInt(resultSet.getString("score")));
                i++;
            }
            return scores;
        } catch (SQLException e) {
            log.error("getScores Error : " + e.getMessage());
            return new HashMap<>();
        }

    }

    /**
     * Autocloseable for SQL Connection
     * Closes the connection after try-catch block
     *
     * @throws IOException if this resource cannot be closed
     */
    @Override
    public void close() throws IOException {
        try {
            if (connection != null && !connection.isClosed()) {
                log.info("Closing Database Connection");
                connection.close();
            }
        } catch (SQLException e) {
            log.error("closeDataBaseConnection Error: " + e.getMessage());
        }
    }

}
