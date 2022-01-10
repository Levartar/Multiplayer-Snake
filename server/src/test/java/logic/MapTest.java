package logic;

import helpers.ResourceManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

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
    }

    @Test
    void get() {
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
                #####
                #   #
                # @ #
                #   #
                @####""";
        assertEquals(squareMap.toString(),testMap);
    }

    @Test
    void testToStringUpdate2(){
        String startMap = """  
                #####
                #   #
                # @ #
                #   #
                #####                
                """;
        Map squareMap = new Map(startMap);
        squareMap.changeMaterial(new Position(0,4),Material.APPLE);
        squareMap.changeMaterial(new Position(2,3),Material.APPLE);
        String testMap = """
                @####
                # @ #
                # @ #
                #   #
                #####""";
        assertEquals(squareMap.toString(),testMap);
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
    }

    @Test
    void testSpawnPoints() throws IOException {
        Map map = new Map(ResourceManager.getMapPath("BasicMap50x50"));
        assertTrue(map.getSpawnPoints().contains(new Position(35,35)));
        assertTrue(map.getSpawnPoints().contains(new Position(35,15)));
        assertTrue(map.getSpawnPoints().contains(new Position(15,35)));
        assertTrue(map.getSpawnPoints().contains(new Position(15,15)));
    }

}