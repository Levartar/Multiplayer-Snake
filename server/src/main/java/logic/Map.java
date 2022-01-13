package logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Map {

    private static final Logger log = LogManager.getLogger(Map.class);

    private Material[][] map;
    private List<Position> spawnPoints = new ArrayList<>();
    private String mapString;
    //private final int spawnDistanceX = 0;
    //private final int spawnDistanceY = 0;

    public Map(String mapString) {//Overloaded Method to Load Map via String or Path
        this.mapString = mapString;
        parseMapString(mapString);
        updateMapString();
        shuffleSpawnPoints();
        log.debug("Map created: \n" +mapString);
    }

    public Map(Path mapPath) throws IOException {
        this.mapString = Files.readString(mapPath, StandardCharsets.UTF_8);
        parseMapString(mapString);
        updateMapString();
        shuffleSpawnPoints();
        log.debug("Map from "+mapPath+" created: \n" +mapString);
    }

    public void changeMaterial(Position pos, Material material) {
        map[pos.getX()][pos.getY()] = material;
        updateMapString();
        log.debug("Material changed:"+ pos+" -> "+ material );
    }

    public void changeMaterial(int x, int y, Material material) {
        map[x][y] = material;
        updateMapString();
        log.debug("Material changed:"+ "x:" + x + "," + "y:" + y+" -> "+ material );
    }

    public Material[][] getMap() { //maybe useless
        return map;
    }

    public Material getMaterialAt(Position pos) {
        return map[pos.getX()][pos.getY()];
    }

    public Material getMaterialAt(int x, int y) {
        return map[x][y];
    }

    private void shuffleSpawnPoints() {
        Collections.shuffle(spawnPoints);
    }

    private void parseMapString(String mapString) {
        mapString = parseString(mapString);
        String[] splitStrings = mapString.split("\n");
        //have to remove '\n' = carriage return as well if it exists
        map = new Material[splitStrings[0].length()][splitStrings.length];

        for (int i = 0; i < splitStrings.length; i++) {
            char[] tmp = splitStrings[i].toCharArray();
            for (int j = 0; j < tmp.length; j++) {
                if (tmp[j] == 's'){
                    map[j][getHeight() - i - 1] = Material.FREESPACE;
                    spawnPoints.add(new Position(j,i));
                } else {
                    map[j][getHeight() - i - 1] = Material.getMaterial(tmp[j]);
                }
            }
        }
    }

    //this removes all \r if they exist
    private String parseString(String str) {
        return str.replace("\r","");
    }

    @Override
    public String toString() {
        return mapString;
    }

    private void updateMapString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                sb.append(map[j][getHeight() - 1 - i]);
            }
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        this.mapString = sb.toString();
    }

    public int getWidth() {
        return map.length;
    }

    public int getHeight() {
        return map[0].length;
    }

    public List<Position> getSpawnPoints() {
        return spawnPoints;
    }

}
