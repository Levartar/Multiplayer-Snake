package database;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {

    String name = "test";
    int x = 420;

    @Test
    @Order(1)
    public void connection(){
        try(SQLConnection sqlConnection = new SQLConnection()){
            int i = 0;
        }catch (Exception e){
            System.out.println("Error!" + e);
        }
    }

    @Test
    @Order(2)
    public void negativScore() {
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            SQLConnection sqlCon = new SQLConnection();
            sqlCon.insertSnakeHighscore(name, -10);
        });
    }




    @Test
    @Order(3)
    public void postData(){
        try(SQLConnection sqlCon = new SQLConnection()){
            sqlCon.insertSnakeHighscore(name,x);
        }catch (IOException IOe){
            IOe.printStackTrace();
        }
    }

    @Test
    @Order(4)
    public void getHighscores() throws Exception {
        SQLConnection sqlCon = new SQLConnection();
        sqlCon.insertSnakeHighscore("test",420);
        try (SQLConnection sqlConnection = new SQLConnection()) {
            var test = sqlConnection.getScores(1);
            Map<String, Integer> referenzmap = new HashMap<>();
            referenzmap.put("test",420);
            assertEquals(referenzmap,test);
        }
    }
    @Test
    @Order(5)
    public void delete() {
        SQLConnection sqlCon = new SQLConnection();
        sqlCon.insertSnakeHighscore("test", 10);
        SQLConnection sqlConnection = new SQLConnection();
        assertTrue(0 < sqlConnection.deleteHighscore("test"));
    }

    @Test
    @Order(6)
    void deleteMultiple() {
        var TestMap= new HashMap<String,Integer>();
        TestMap.put("Tim_Test",20);
        TestMap.put("Timo_Test",Integer.MAX_VALUE);
        TestMap.put("Felix_TEST",21);
        TestMap.put("Anjo_TEST",30);

        try(SQLConnection sqlCon = new SQLConnection()){
            for (var score: TestMap.entrySet()) {
                sqlCon.insertSnakeHighscore(score.getKey(),score.getValue());
            }

            assertEquals(4,sqlCon.deleteHighscore("Test"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

