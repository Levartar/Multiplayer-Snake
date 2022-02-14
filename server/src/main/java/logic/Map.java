package logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@link Map} class contains all information about the field except the positions of the snakes.
 * It contains information like size and set materials or spawnpoints of the specific map.
 */
public class Map {

    private static final Logger log = LogManager.getLogger(Map.class);

    private Material[][] map;
    private List<Position> spawnPoints = new ArrayList<>();
    private String mapString;

    /**
     * Copy Constructor. Creates a new instance of a given map object.
     * @param map The original map to create a copy.
     */
    public Map(Map map) {
        this(map.toString());
        this.spawnPoints = List.copyOf(map.spawnPoints);
    }

    /**
     * Constructor reads map string and creates a {@link Material} array. The usable characters should be listed in
     * the {@link Material} class.
     * @param mapString The String that represents a map.
     */
    public Map(String mapString) {
        this.mapString = mapString;
        parseMapString(mapString);
        updateMapString();
        shuffleSpawnPoints();
        log.debug("Map created: \n" +mapString);
    }

    /**
     * Changes the {@link Material} in the {@link Map#map} array.
     * @param pos The {@link Position} where to change the {@link Material}
     * @param material The new {@link Material} to set.
     */
    public void changeMaterial(Position pos, Material material) {
        map[pos.getX()][pos.getY()] = material;
        updateMapString();
        log.debug("Material changed:"+ pos+" -> "+ material );
    }

    /**
     * Changes the {@link Material} in the {@link Map#map} array.
     * @param x The x coordinate where to change the {@link Material}
     * @param y The y coordinate where to change the {@link Material}
     * @param material The new {@link Material} to set.
     */
    public void changeMaterial(int x, int y, Material material) {
        map[x][y] = material;
        updateMapString();
        log.debug("Material changed:"+ "x:" + x + "," + "y:" + y+" -> "+ material );
    }

    /**
     *
     * @return The reference of a new copy instance of this {@link Map} instance.
     */
    public Material[][] getMap() { //maybe useless
        Material[][] output = new Material[getWidth()][getHeight()];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                output[i][j] = map[i][j];
            }
        }
        return output;
    }

    /**
     * Returns a {@link Material} at a given {@link Position}
     * @param pos {@link Position} where you want to get the {@link Material}
     * @return {@link Material} at a specific position in the {@link Material} array
     */
    public Material getMaterialAt(Position pos) {
        return map[pos.getX()][pos.getY()];
    }

    /**
     * Returns a {@link Material} at a given x and y coordinate
     * @param x The x coordinate
     * @param y The y coordinate
     * @return {@link Material} at a specific position in the {@link Material} array
     */
    public Material getMaterialAt(int x, int y) {
        return map[x][y];
    }

    /**
     * Shuffles the spawnpoints. Used before every new round to spawn at different positions.
     */
    public void shuffleSpawnPoints() {
        Collections.shuffle(spawnPoints);
    }

    /**
     * With the given String, representing the map, this method creates the {@link Map#map} array and fills it with
     * the materials corresponding to the String. Also fills the list {@link Map#spawnPoints} with the {@link Position}s
     * @param mapString The String that represents a map.
     */
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

    /**
     * This removes all \r if they exist
     * @param str The original String
     * @return The String without \r
     */
    private String parseString(String str) {
        return str.replace("\r","");
    }

    @Override
    public String toString() {
        return mapString;
    }

    /**
     * If something changes in the {@link Map#map} this method creates a new String based on {@link Map#map}.
     * So every change will update the {@link Map#mapString}.
     */
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

    /**
     *
     * @return The width of the {@link Map#map}.
     */
    public int getWidth() {
        return map.length;
    }

    /**
     *
     * @return The height of the {@link Map#map}.
     */
    public int getHeight() {
        return map[0].length;
    }

    /**
     *
     * @return The list of all spawnpoints
     */
    public List<Position> getSpawnPoints() {
        return spawnPoints;
    }

}
