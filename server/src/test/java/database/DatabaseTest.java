package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import Database.SQLConnection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private static final Logger log = LogManager.getLogger(SQLConnection.class);
    String name = "Jens";
    int highscore = 2048;

    @Test
    void postDatatest() {
        assertTrue(sendData());
        log.info("send Data to Database!");
    }

    @Test
    void receiveDatatest() {
        assertEquals(highscore, Integer.valueOf(SQLConnection.getScore(name)));
        log.info("received Data");
    }

    @Test
    void deleteDatatest() {
        name = "Felix";
        SQLConnection.InsertSnakeHighscore("Tim", 20);
        SQLConnection.InsertSnakeHighscore("Timo", 21);
        SQLConnection.InsertSnakeHighscore("Felix", 23);
        SQLConnection.InsertSnakeHighscore("Anjo", 24);
        assertEquals(1,SQLConnection.deleteHighscore("Felix"));


        assertEquals("Could not find name: " + name, SQLConnection.getName("Felix"));
    }

    public boolean sendData() {

        return (SQLConnection.InsertSnakeHighscore(name, highscore));
    }


}
