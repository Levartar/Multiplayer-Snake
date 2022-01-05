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

    public static void connectToServer(String dataBaseName) throws SQLException {
        connectSSH();
        connectToDataBase(dataBaseName);
        log.info("Successfully connected to Database: " + dataBaseName);
    }

    public static void main(String[] args) {

        List<String> dbNames = getAllDBNames();
        System.out.println("DBNames:");
        for (String s: dbNames
             ) {
            System.out.println(s);

        }
        closeConnections();
    }

    private static void connectSSH() {
        String sshHost = "193.196.53.28";
        String sshuser = "se3dbUser";
        String SshKeyFilepath = "ser22pentes";

        int localPort = 8740; // any free port can be used
        String remoteHost = "127.0.0.1";
        int remotePort = 3306;


        try {
            java.util.Properties config = new java.util.Properties();
            JSch jsch = new JSch();
            session = jsch.getSession(sshuser, sshHost, 22);
            jsch.addIdentity(SshKeyFilepath);
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

    private static void connectToDataBase(String dataBaseName)  {
        String dbuserName = "x";
        String dbpassword = "x";
        int localPort = 8740; // any free port can be used
        String localSSHUrl = "x";
        try {
            //mysql database connectivity
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(localSSHUrl);
           dataSource.setPortNumber(localPort);
           dataSource.setUser(dbuserName);
           dataSource.setAllowMultiQueries(true);

            dataSource.setPassword(dbpassword);
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
    private static ResultSet executeMyQuery(String query, String dataBaseName) {
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
        } finally {
            closeConnections();
        }
        return organisationDbNames;
    }


    
}
