package Database;

import java.sql.*;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection {
    private static final Logger log = LogManager.getLogger(SQLConnection.class);


    private static Connection connection = null;
    private static Session session = null;
    private static String DBUser;
    private static String DBUserPW;
    private static String DBIP;
    private static String SSHPrivatKey;


    public static void connectToServer(String dataBaseName) throws SQLException {
        connectSSH();
        connectToDataBase(dataBaseName);
        log.info("Successfully connected to Database: " + dataBaseName);
    }

    public static void main(String[] args) {
        DBUser = args[0];
        DBUserPW = args[1];
        DBIP = args[2];
        SSHPrivatKey = "C:\\Users\\nikoh\\.ssh\\id_rsa";
        System.out.println(DBUser + DBUserPW + DBIP + "Privat Key: " + SSHPrivatKey);

        try {
            ResultSet resultSet = executeMyQuery("Select * from highscores;", "testdb");
            while (resultSet.next()) {
                log.debug(resultSet.getString(1));
            }
        } catch (SQLException e) {
            log.info(e);
        }
        closeConnections();
    }

    private static void connectSSH() {
        String sshHost = DBIP;
        String sshuser = "root";


        int localPort = 8000; // any free port can be used
        String remoteHost = DBIP;
        int remotePort = 3306;


        try {
            java.util.Properties config = new java.util.Properties();
            JSch jsch = new JSch();
            session = jsch.getSession(sshuser, sshHost, 22);
            jsch.addIdentity(SSHPrivatKey);
            config.put("StrictHostKeyChecking", "no");
            config.put("ConnectionAttempts", "3");
            session.setConfig(config);
            session.connect();

            System.out.println("SSH Connected");


            int assinged_port = session.setPortForwardingL(localPort, remoteHost, remotePort);

            System.out.println("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
            System.out.println("Port Forwarded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void connectToDataBase(String dataBaseName) {

        int localPort = 8000; // any free port can be used
        String localSSHUrl = "127.0.0.1";
        try {
            //mysql database connectivity
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(localSSHUrl);
            dataSource.setPortNumber(localPort);
            dataSource.setUser(DBUser);
            dataSource.setAllowMultiQueries(true);

            dataSource.setPassword(DBUserPW);
            dataSource.setDatabaseName(dataBaseName);
            connection = dataSource.getConnection();

            System.out.print("Connection to server successful!:" + connection + "\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void closeConnections() {
        CloseDataBaseConnection();
        CloseSSHConnection();
        log.info("Successfully disconnected from Database: ");
    }

    private static void CloseDataBaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                log.info("Closing Database Connection");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            log.info("Closing SSH Connection");
            session.disconnect();
        }
    }


    // works ONLY FOR  single query (one SELECT or one DELETE etc)
    public static ResultSet executeMyQuery(String query, String dataBaseName) {
        ResultSet resultSet = null;

        try {
            connectToServer(dataBaseName);
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            log.info("Database connection success");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static List<String> getAllDBNames() {
        // get all live db names incentral DB
        List<String> organisationDbNames = new ArrayList<>();
        ResultSet resultSet = executeMyQuery("show databases", "testdb");
        try {
            while (resultSet.next()) {
                String actualValue = resultSet.getString("Database");
                organisationDbNames.add(actualValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organisationDbNames;
    }


}
