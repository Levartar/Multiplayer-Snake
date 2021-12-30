package logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Map {

    Material[][] Positions;
    //HashMap<Position, Material> Positions = new HashMap<>();
    List<Position> spawnPoints = new ArrayList<>();
    String mapString;

    public Map(String mapString) {//Overloaded Method to Load Map via String or Path
        this.mapString = mapString;
        parseMapString(mapString);
    }

    public Map(Path mapPath) throws IOException {
        this.mapString = Files.readString(mapPath, StandardCharsets.UTF_8);
        parseMapString(mapString);
    }

    public void changeMaterial(Position pos, Material material) {
        Positions[pos.getY()][pos.getX()] = material;
        updateMapString();
    }

    public void changeMaterial(int x, int y, Material material) {
        Positions[y][x] = material;
        updateMapString();
    }

    public Material[][] getPositions() {
        return Positions;
    }

    public Material get(Position pos) {
        return Positions[pos.getY()][pos.getX()];
    }

    public Material get(int x, int y) {
        return Positions[y][x];
    }

    private void parseMapString(String mapString) {
        this.Positions = getNewMaterialArray(mapString);

        int x = 0;
        int y = 0;
        char[] mapCharArray = mapString.toCharArray();
        for (char c : mapCharArray) {
            if (c == '\n') {
                y++;
                x = 0;
            } else {
                Material material = Material.getMaterial(c);
                this.Positions[y][x] = material;
                if (material == Material.SPAWN) {
                    spawnPoints.add(new Position(x - 1, y - 1));
                }
                x++;
            }
        }
        //TODO get randomized Spawnpoints and write them into Spawnpoints list
        // spawns should be marked as 's'
    }

    private Material[][] getNewMaterialArray(String mapString){
        int x = 0;
        int y = 0;
        while (mapString.endsWith("\n")) {
            mapString = mapString.substring(0,mapString.length()-1);
        }
        char[] mapCharArray = mapString.toCharArray();
        for (char a : mapCharArray) {
            if (a == '\n') {
                y++;
                x = 1;
            } else {
                x++;
            }
        }
        return new Material[y + 1][x - 1];
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
        for (Material[] subMaterial : Positions) {
            for (int i = 0; i < subMaterial.length; i++) {
                sb.append(subMaterial[i]);
            }
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        this.mapString = sb.toString();
    }

    public int getWidth() {
        return Positions.length;
    }

    public int getHeight() {
        return Positions[0].length;
    }

    //private void calculateWidthAndHeight(){
    //    Object[] posArray = Positions.keySet().toArray();
    //    for (int i = 0; i < posArray.length; i++) {
    //        Position pos = (Position) posArray[i];
    //        if (pos.getX()> width){
    //            width = pos.getX();
    //        }
    //        if (pos.getY()> height){
    //            height = pos.getY();
    //        }
    //    }
    //}

    public List<Position> getSpawnPoints() {
        return spawnPoints;
    }

}
