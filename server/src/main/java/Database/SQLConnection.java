package Database;

import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;


public class SQLConnection {
    private static final Logger log = LogManager.getLogger(SQLConnection.class);


    private static Connection connection;
    private static String DBUser;
    private static String DBUserPW;
    private static String DBIP;
    static Statement stmt;
    static String databasename;

    public static void setLoginDetails() {
        DBUser = System.getenv("DBUser");
        DBUserPW = System.getenv("DBUserPW");
        DBIP = System.getenv("Server_IP");
        databasename = "testdb";
        log.debug(DBUser + DBUserPW + DBIP);

    }

    public static void connectToServer(String dataBaseName) {
        setLoginDetails();
        connectToDataBase(dataBaseName);
        log.info("Successfully connected to Database: " + dataBaseName);
    }


    public static void main(String[] args) {
        getName("");
    }


    private static void connectToDataBase(String dataBaseName) {
        int localPort = 3360;
        String url = "jdbc:mariadb://" + DBIP + ":" + localPort + "/" + dataBaseName;

        try {
            connection = DriverManager.getConnection(url, DBUser, DBUserPW);
            stmt = connection.createStatement();
            log.info("Connection to server successful!:" + connection + "\n\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeDataBaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                log.info("Closing Database Connection");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static boolean InsertSnakeHighscore(String name, int highscore) {

        String statement = "INSERT IGNORE INTO `Highscore` SET `score_id` = NULL," +
                "`player_name` = '" + name + "'," +
                "`score` = " + highscore + ";";
        return InsertStatement(statement);
    }

    public static boolean InsertStatement(String insert) {
        try {
            connectToServer("testdb");
            stmt.executeUpdate(insert);
            stmt.close();
            log.info("InsertStatement executed");
            closeDataBaseConnection();
            return true;
        } catch (SQLException e) {
            log.error(e);
            return false;
        }
    }

    public static boolean deleteHighscore(String name) {
        String delete = "delete from testdb.`Highscore` where `player_name` ='" + name + "';";
        try {
            connectToServer("testdb");
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(delete);
            log.info("Database connection success");
            return true;
        } catch (SQLException e) {
            log.error(e);
            return false;
        }
    }


    // works ONLY FOR  single query (one SELECT or one DELETE etc)
    public static String getScore(String name) {
        ResultSet resultSet;
        String result = "";
        try {
            connectToServer("testdb");
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery("Select score from testdb.Highscore where player_name = '" + name + "';");
            log.info("Database connection success");
            closeDataBaseConnection();
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }
            log.debug("returning score:" + result);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error!";
    }

    public static String getName(String name) {
        ResultSet resultSet;
        String result = "";
        try {
            connectToServer("testdb");
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery("SELECT player_name FROM testdb.Highscore where player_name ='" + name + "';");
            closeDataBaseConnection();
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }
            log.debug("Database querry for name:" + name +" : "+ result);
            if(result.equals("")){
                log.debug("Database: no Matches for:" +name );
                return "Could not find name: " + name;
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Could not find name:" + name;
    }


}
