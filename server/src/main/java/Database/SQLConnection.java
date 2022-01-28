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
    private static int localPort = 3360;

    public static void main(String[] args) {
        InsertSnakeHighscore("NikoLocal",22);
    }

    /**
     *
     * if no enviroment variables expect local db for test purposes
     */
    public static void setLoginDetails() {
        DBUser = System.getenv("DBUserName");
        DBUserPW = System.getenv("DBUserPassword");
        DBIP = System.getenv("Server_IP");

        if(DBIP == null || DBIP.equals("")){
            DBIP = "localhost";
            String hostname = System.getenv("DATABASE_HOSTNAME");
            if(hostname == null || hostname.equals("")) hostname = "localhost";
            DBIP = hostname;
            DBUser = "root";
            DBUserPW = System.getenv("rootPW");
            if(DBUserPW == null || DBUserPW.equals("")){
                DBUserPW = "testpw!";
                log.info("rootPW not found! set as normal.");
            }
            log.info("DB expected locally");
            localPort = 3306;
        }

    }

    public static void connectToServer(String dataBaseName) {
        setLoginDetails();
        connectToDataBase(dataBaseName);
        log.info("Successfully connected to Database: " + dataBaseName);
    }


    private static void connectToDataBase(String dataBaseName) {

        String url = "jdbc:mariadb://" + DBIP + ":" + localPort + "/" + dataBaseName;

        try {
            connection = DriverManager.getConnection(url, DBUser, DBUserPW);
            stmt = connection.createStatement();
            log.info("Connection to server successful!:" + DBIP);
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
        String test = name.toUpperCase();
        if(test.length() >= 100 || test.contains("FROM")||
                test.contains("DELETE") || test.contains("INSERT") || test.contains("DROP") ||
                test.contains(";") || test.contains("1=1") || test.contains("REPLACE") || test.contains("SELECT")){
            return false;
        }
        String statement = "INSERT IGNORE INTO `Highscore`" +
                " SET `score_id` = NULL," +
                "`player_name` =  ?, " +"`score` = ?";
        return InsertStatement(statement, name, highscore);
    }

    public static boolean InsertStatement(String insert, String name, int highscore) {
        try {

            connectToServer(databasename);
            PreparedStatement ps = connection.prepareStatement(insert);
            ps.setString(1, name);
            ps.setInt(2, highscore);
            int rowAffected = ps.executeUpdate();
            log.debug(rowAffected + "rows affected");
            stmt.close();
            log.info("InsertStatement executed" + ps);
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
