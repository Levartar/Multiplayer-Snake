package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import Database.SQLConnection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private static final Logger log = LogManager.getLogger(Database.SQLConnection.class);
    String name = "Jens_TEST";
    int highscore = 2048;

    @Test
    void postDatatest() {
        assertTrue(sendData());
        log.info("send Data to Database!");
    }

    @Test
    void receiveDatatest() {
        Database.SQLConnection.InsertSnakeHighscore(name, highscore);
        int i;
        try{
            i = Integer.parseInt(Database.SQLConnection.getName(name));
        }catch ( NumberFormatException Ne){
            log.error(Ne);
            i = -1;
        }
        assertEquals(highscore, i);
        log.info("received Data");
        Database.SQLConnection.deleteHighscore("Jens_TEST");
    }

    @Test
    void deleteDatatest() {
        name = "Felix_TEST";
        SQLConnection.InsertSnakeHighscore("Tim_TEST", 20);
        SQLConnection.InsertSnakeHighscore("Timo_TEST", 21);
        SQLConnection.InsertSnakeHighscore("Felix_TEST", 23);
        SQLConnection.InsertSnakeHighscore("Anjo_TEST", 24);
        assertEquals(1,SQLConnection.deleteHighscore("Felix_TEST"));
        assertEquals("Could not find name: " + name, SQLConnection.getName("Felix_TEST"));
        SQLConnection.deleteHighscore("Tim_TEST");
        SQLConnection.deleteHighscore("Timo_TEST");
        SQLConnection.deleteHighscore("Felix_TEST");
        SQLConnection.deleteHighscore("Anjo_TEST");
    }

    public boolean sendData() {

        return (SQLConnection.InsertSnakeHighscore(name, highscore));
    }


}
