package database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
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
       assertEquals(highscore, Integer.valueOf(receiveData()));
       log.info("received Data");
   }

   // @Test
   // void deleteDatatest(){
   //     deleteData();
   //     assertFalse(deleteData());
   // }

  // @Test
  // void failedInsert(){
  //     name = "IammuchlongerthanonlysixtywordsbecauseihavetocreateanBIGError!";
  //     assertFalse(sendData());

  // }


  public String receiveData(){
      String returnString = "";
      try {
          ResultSet resultSet = SQLConnection.executeMyQuery("Select highscore from highscores where Name = '" + name + "';", "testdb");
          while (resultSet.next()) {
              returnString = resultSet.getString(1);
          }
      } catch (SQLException e) {
          log.info(e);
      }
      SQLConnection.closeConnections();
      return returnString;
  }
  public boolean sendData(){

      return (SQLConnection.InsertSnakeHighscore(name,highscore));
  }

  public boolean deleteData(){
      return (SQLConnection.deleteHighscore(name, "testdb"));
  }


}
