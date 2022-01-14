package logic;

import helpers.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    private static final Logger log = LogManager.getLogger(MapTest.class);

    @Test
    void getMaterialAt() {
        String testMap = """
                #####
                # @ #
                #   #
                #   #
                #  ##""";
        Map squareMap = new Map(testMap);
        assertEquals('#', squareMap.getMaterialAt(new Position(0, 0)).getSymbol());
        assertEquals('#', squareMap.getMaterialAt(new Position(1, 0)).getSymbol());
        assertEquals('#', squareMap.getMaterialAt(new Position(2, 0)).getSymbol());
        assertEquals('#', squareMap.getMaterialAt(new Position(3, 0)).getSymbol());
        assertEquals('#', squareMap.getMaterialAt(new Position(4, 0)).getSymbol());

        assertEquals('#', squareMap.getMaterialAt(new Position(0, 1)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(1, 1)).getSymbol());
        assertEquals('@', squareMap.getMaterialAt(new Position(2, 1)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(3, 1)).getSymbol());
        assertEquals('#', squareMap.getMaterialAt(new Position(4, 1)).getSymbol());

        assertEquals('#', squareMap.getMaterialAt(new Position(0, 2)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(1, 2)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(2, 2)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(3, 2)).getSymbol());
        assertEquals('#', squareMap.getMaterialAt(new Position(4, 2)).getSymbol());

        assertEquals('#', squareMap.getMaterialAt(new Position(0, 3)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(1, 3)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(2, 3)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(3, 3)).getSymbol());
        assertEquals('#', squareMap.getMaterialAt(new Position(4, 3)).getSymbol());

        assertEquals('#', squareMap.getMaterialAt(new Position(0, 4)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(1, 4)).getSymbol());
        assertEquals(' ', squareMap.getMaterialAt(new Position(2, 4)).getSymbol());
        assertEquals('#', squareMap.getMaterialAt(new Position(3, 4)).getSymbol());
        assertEquals('#', squareMap.getMaterialAt(new Position(4, 4)).getSymbol());
    }

    @Test
    void loadMap() {
        //Build TestString
        String testMap = """
                #####
                #   #
                # @ #
                #   #
                #####""";
        Map squareMap = new Map(testMap);
        assertEquals(squareMap.toString(), testMap);
        log.info("Test "+"loadMap" +" passed");
    }

    @Test
    void getMap() {
        String testMap = """
                #####
                #   #
                # @ #
                #   #
                #####""";
        Map squareMap = new Map(testMap);
        assertEquals(Material.WALL, squareMap.getMaterialAt(new Position(0, 0)));
        assertEquals(Material.FREESPACE, squareMap.getMaterialAt(new Position(1, 1)));
        assertEquals(Material.APPLE, squareMap.getMaterialAt(new Position(2, 2)));
        assertEquals(Material.WALL, squareMap.getMaterialAt(new Position(4, 3)));
        log.info("Test "+"getMap" +" passed");
    }

    @Test
    void getSpawnpoint() {

    }

    @Test
    void testToString(){
        String testMap = """
                #####
                #   #
                # @ #
                #   #
                #####""";
        Map squareMap = new Map(testMap);
        assertEquals(squareMap.toString(),testMap);
        log.info("Test "+"testToString" +" passed");
    }

    @Test
    void testToStringUpdate(){
        String startMap = """
                #####
                #   #
                # @ #
                #   #
                #####""";
        Map squareMap = new Map(startMap);
        squareMap.changeMaterial(new Position(0,0),Material.APPLE);
        String testMap = """
                @####
                #   #
                # @ #
                #   #
                #####""";
        assertEquals(squareMap.toString(),testMap);
        log.info("Test "+"testToStringUpdate" +" passed");
    }

    @Test
    void testToStringUpdate2(){
        String startMap = """  
                #####
                #   #
                # @ #
                #   #
                #####""";
        Map squareMap = new Map(startMap);
        squareMap.changeMaterial(new Position(0,4),Material.APPLE);
        squareMap.changeMaterial(new Position(2,3),Material.APPLE);
        String testMap = """
                #####
                #   #
                # @ #
                # @ #
                @####""";
        assertEquals(squareMap.toString(),testMap);
        log.info("Test "+"testToStringUpdate2" +" passed");
    }

    @Test
    void testMapConstructor(){
        String mapString = """
                #####
                #   #
                # s #
                #   #
                #####""";
        Map map = new Map(mapString);
        log.info("Test "+"testMapConstructor" +" passed");
    }

    @Test
    void testSpawnPoints() throws IOException {
        Map map = new Map(ResourceManager.getMapPath("BasicMap50x50"));
        assertTrue(map.getSpawnPoints().contains(new Position(35,35)));
        assertTrue(map.getSpawnPoints().contains(new Position(35,15)));
        assertTrue(map.getSpawnPoints().contains(new Position(15,35)));
        assertTrue(map.getSpawnPoints().contains(new Position(15,15)));
        log.info("Test "+"testSpawnPoints" +" passed");
    }

}