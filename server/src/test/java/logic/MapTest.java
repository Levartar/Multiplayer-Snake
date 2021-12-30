package logic;

import org.junit.jupiter.api.Test;

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
        Map squareMap = new Map();
        squareMap.Map(testMap);
        assertTrue(squareMap.toString().equals(testMap));
    }

    @Test
    void get() {
        String testMap = """
                #####
                #   #
                # @ #
                #   #
                #####""";
        Map squareMap = new Map();
        squareMap.Map(testMap);
        assertTrue(squareMap.get(new Position(1,1)).equals(Material.WALL));
        assertTrue(squareMap.get(new Position(2,2)).equals(Material.FREESPACE));
        assertTrue(squareMap.get(new Position(3,3)).equals(Material.APPLE));
        assertTrue(squareMap.get(new Position(5,4)).equals(Material.WALL));
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
        Map squareMap = new Map();
        squareMap.Map(testMap);
        System.out.println(squareMap);
        assertEquals(squareMap.toString(),testMap);
    }

}