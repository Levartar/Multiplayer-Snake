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

    public Map(Map map) {
        this(map.toString());
        this.spawnPoints = List.copyOf(map.spawnPoints);
    }

    public Map(String mapString) {
        this.mapString = mapString;
        parseMapString(mapString);
        updateMapString();
        shuffleSpawnPoints();
        log.debug("Map created: \n" +mapString);
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
        Material[][] output = new Material[getWidth()][getHeight()];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                output[i][j] = map[i][j];
            }
        }
        return output;
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
        try {
            mapString = parseString(mapString);
            String[] splitStrings = mapString.split("\n");
            //have to remove '\n' = carriage return as well if it exists
            for (int i = 0; i < splitStrings.length - 1; i++) {
                if (splitStrings[i].length() != splitStrings[i+1].length()){
                    throw new Exception("Length of line in mapstring doesnt fit.");
                }
            }
            map = new Material[splitStrings[0].length()][splitStrings.length];

            for (int y = 0; y < splitStrings.length; y++) {
                char[] tmp = splitStrings[y].toCharArray();
                for (int x = 0; x < tmp.length; x++) {
                    if(tmp[x] == 's'){
                        map[x][y] = Material.FREESPACE;
                        spawnPoints.add(new Position(x,y));
                    } else {
                        map[x][y] = Material.getMaterial(tmp[x]);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
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

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                sb.append(map[x][y]);
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
