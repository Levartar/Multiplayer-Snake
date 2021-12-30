package logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Map {

    private Material[][] map;
    private List<Position> spawnPoints = new ArrayList<>();
    private String mapString;
    //private final int spawnDistanceX = 0;
    //private final int spawnDistanceY = 0;

    public Map(String mapString) {//Overloaded Method to Load Map via String or Path
        this.mapString = mapString;
        parseMapString(mapString);
        generateRandomSortedSpawns();
    }

    public Map(Path mapPath) throws IOException {
        this.mapString = Files.readString(mapPath, StandardCharsets.UTF_8);
        parseMapString(mapString);
        generateRandomSortedSpawns();
    }

    public void changeMaterial(Position pos, Material material) {
        map[pos.getX()][pos.getY()] = material;
        updateMapString();
    }

    public void changeMaterial(int x, int y, Material material) {
        map[x][y] = material;
        updateMapString();
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

    private void generateRandomSortedSpawns() {
//        Position spawnUpLeft = new Position(spawnDistanceX,getHeight() - spawnDistanceY);
//        Position spawnUpRight = new Position(getWidth() - spawnDistanceX,getHeight() - spawnDistanceY);
//        Position spawnDownLeft = new Position(spawnDistanceX,spawnDistanceY);
//        Position spawnDownRight = new Position(getWidth() - spawnDistanceX,spawnDistanceY);
//
//        spawnPoints.add(spawnUpLeft);
//        spawnPoints.add(spawnUpRight);
//        spawnPoints.add(spawnDownLeft);
//        spawnPoints.add(spawnDownRight);
        Collections.shuffle(spawnPoints);
    }

    private void parseMapString(String mapString) {
        String[] splitStrings = mapString.split("\n");
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

    //public Position getSpawn() {
    //    Position newSpawn = spawnPoints.get(0);
    //    while (spawnPoints.contains(newSpawn)/*&&Positions.get(newSpawn)!=Material.WALL*/){
    //        Random rand = new Random();
    //        //10 is max steps. Could lead to Map out of bounds Problems
    //        newSpawn = new Position(spawnPoints.get(0).x+rand.nextInt(10)*spawnPointWidth,spawnPoints.get(0).y+rand.nextInt(10)*spawnPointHeight);
    //    }
    //    spawnPoints.add(newSpawn);
    //    return newSpawn;
    //}

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
