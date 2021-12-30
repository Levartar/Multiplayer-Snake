package logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Map {

    HashMap<Position, Material> Positions = new HashMap<>();
    List<Position> spawnPoints = new ArrayList<>();
    String mapString;
    int width =0;
    int height =0;

    public void Map(String mapString){//Overloaded Method to Load Map via String or Path
        this.mapString = mapString;
        parseMapString(mapString);
        calculateWidthAndHeight();
    }

    public void Map(Path mapPath) throws IOException {
        this.mapString = Files.readString(mapPath, StandardCharsets.UTF_8);
        parseMapString(mapString);
        calculateWidthAndHeight();
    }

    public void changeMaterial(Position pos, Material material){
        Positions.put(pos,material);
        updateMapString();
    }

    public HashMap<Position, Material> getPositions(){
        return Positions;
    }

    public Material get(Position pos){
        return Positions.get(pos);
    }

    private void parseMapString(String mapString) {
        this.Positions.clear();
        char[] mapCharArray = mapString.toCharArray();
        int x=1;
        int y=1;
        for (char c : mapCharArray) {
            if (c == '\n') {
                y++;
                x = 1;

            } else {
                Position newPos = new Position(x, y);
                this.Positions.put(newPos, getMaterial(c));
                x++;
            }
        }
        //TODO get randomized Spawnpoints and write them into Spawnpoints list
        // spawns should be marked as 's'
    }



    //public Position getSpawn() {
    //    Position newSpawn = spawnPoints.get(0);
    //    while (spawnPoints.contains(newSpawn)/*&&Positions.get(newSpawn)!=Material.WALL*/){
    //        Random rand = new Random();
    //        //10 is max steps. Could lead to Map out of bounds Problems
    //        newSpawn = new Position(spawnPoints.get(0).x+rand.nextInt(10)*spawnPointWidth,spawnPoints.get(0).y+rand.nextInt(10)*spawnPointHeight);
    //    }
    //    spawnPoints.add(newSpawn);
    //    //TODO return specific OR random spawns from map
    //    return newSpawn;
    //}

    @Override
    public String toString() {
        return mapString;
    }

    private void updateMapString() {
        char[][] charArray = new char[width][height];
        Positions.forEach((position, material) -> {
            charArray[position.x-1][position.y-1] = material.getSymbol();//hope this works
        });
        StringBuilder sb = new StringBuilder();
        for (char[] subArray : charArray) {
            sb.append(subArray);
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length()-1);
        mapString =  sb.toString();
    }

    private void calculateWidthAndHeight(){
        Object[] posArray = Positions.keySet().toArray();
        for (int i = 0; i < posArray.length; i++) {
            Position pos = (Position) posArray[i];
            if (pos.x> width){
                width = pos.x;
            }
            if (pos.y> height){
                height = pos.y;
            }
        }
    }

        //TODO this should be in Material enum but i dont know how to put it there
        private Material getMaterial(char symbol) {
            switch (symbol) {
                case '#' -> {return Material.WALL;}
                case '@' -> {return Material.APPLE;}
                case ' ' -> {return Material.FREESPACE;}
            }
            return null;
        }


    public List<Position> getSpawnPoints() {
        return spawnPoints;
    }
}
