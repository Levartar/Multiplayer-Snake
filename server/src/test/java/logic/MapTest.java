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
        Map squareMap = new Map();
        squareMap.Map(testMap);
        assertEquals(squareMap.get(new Position(0, 0)), Material.WALL);
        assertEquals(squareMap.get(new Position(1, 1)), Material.FREESPACE);
        assertEquals(squareMap.get(new Position(2, 2)), Material.APPLE);
        assertEquals(squareMap.get(new Position(4, 3)), Material.WALL);
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
        Map squareMap = new Map();
        squareMap.Map(startMap);
        squareMap.changeMaterial(new Position(0,0),Material.APPLE);
        String testMap = """
                @####
                #   #
                # @ #
                #   #
                #####""";
        assertEquals(squareMap.toString(),testMap);
    }

    @Test
    void testToStringUpdate2(){
        String startMap = """
                #####
                #   #
                # @ #
                #   #
                #####""";
        Map squareMap = new Map();
        squareMap.Map(startMap);
        squareMap.changeMaterial(new Position(0,0),Material.APPLE);
        squareMap.changeMaterial(new Position(2,1),Material.APPLE);
        String testMap = """
                @####
                # @ #
                # @ #
                #   #
                #####""";
        assertEquals(squareMap.toString(),testMap);
    }

}