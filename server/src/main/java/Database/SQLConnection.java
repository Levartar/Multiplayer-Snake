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
    static String databasename = "testdb";

    public static void setLoginDetails() {
        DBUser = System.getenv("DBUserName");
        DBUserPW = System.getenv("DBUserPassword");
        DBIP = System.getenv("Server_IP");

    }

    public static void connectToServer(String dataBaseName) {
        setLoginDetails();
        connectToDataBase(dataBaseName);
        log.info("Successfully connected to Database: " + dataBaseName);
    }


    private static void connectToDataBase(String dataBaseName) {
        int localPort = 3360;
        String url = "jdbc:mariadb://" + DBIP + ":" + localPort + "/" + dataBaseName;

        try {
            connection = DriverManager.getConnection(url, DBUser, DBUserPW);
            stmt = connection.createStatement();
            log.info("Connection to server successful!:" + connection + "\n\n");
        } catch (SQLException e) {
            log.error("connectToDataBase Error: " + e.getMessage());
        }
    }

    public static void closeDataBaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                log.info("Closing Database Connection");
                connection.close();
            }
        } catch (SQLException e) {
            log.error("closeDataBaseConnection Error: " + e.getMessage());
        }

    }


    public static boolean InsertSnakeHighscore(String name, int highscore) {
        if(name.length() >= 100){
            return false;
        }
        String statement = "INSERT IGNORE INTO `Highscore` " +"SET `score_id` = NULL," +
                "`player_name` = '" + name + "'," +
                "`score` = " + highscore + ";";
        return InsertStatement(statement);
    }

    public static boolean InsertStatement(String insert) {
        try {
            connectToServer(databasename);
            int rowAffected = stmt.executeUpdate(insert);
            log.debug(rowAffected + "rows affected");
            stmt.close();
            log.info("InsertStatement executed" + insert);
            closeDataBaseConnection();
            return true;
        } catch (SQLException e) {
            log.error("InsertStatement Error: " + e);
            return false;
        }
    }

    public static int deleteHighscore(String name) {
        String delete = "delete from testdb.`Highscore` where `player_name` ='" + name + "';";
        int result = 0;
        try {
            connectToServer(databasename);
            Statement stmt = connection.createStatement();
            result = stmt.executeUpdate(delete);
            log.info("Database connection success");
            log.info("Deleted" + result + "rows");
            return result;
        } catch (SQLException e) {
            log.error("deleteHighscore Error: " + e.getMessage());
            return result;
        }
    }


    // works ONLY FOR  single query (one SELECT or one DELETE etc)
    public static String getScore(String name) {
        ResultSet resultSet;
        String result = "";
        try {
            connectToServer(databasename);
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
            log.error("getScore Error: " + e.getMessage());
        }
        return "Error!";
    }

    public static ResultSet getScores(){
        String statement = "Select * from testdb.Highscore order by score DESC";
        ResultSet resultSet;

        try{
            connectToServer(databasename);
            resultSet = stmt.executeQuery(statement);
            closeDataBaseConnection();
            return resultSet;
        }catch(SQLException e){
            log.error("getScores Error : " + e.getMessage());
        }

        return null;
    }

    public static String getName(String name) {
        ResultSet resultSet;
        String result = "";
        try {
            connectToServer(databasename);
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
            log.error("getName Error: " + e.getMessage());
        }
        return "Could not find name: " + name;
    }


}
