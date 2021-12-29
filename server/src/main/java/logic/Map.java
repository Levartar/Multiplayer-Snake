package logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Map {

    HashMap<Position, Material> Positions = new HashMap<>();
    List<Position> spawnPoints = new ArrayList<>();
    int spawnPointWidth;
    int spawnPointHeight;
    String mapString;

    public void loadMap(String mapString){//Overloaded Method to Load Map via String or Path
        this.mapString = mapString;
        parseMapString(mapString);
    }

    public void loadMap(Path mapPath) throws IOException {
        this.mapString = Files.readString(mapPath, StandardCharsets.UTF_8);
        parseMapString(mapString);
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
    }

    public Map() {
        spawnPoints.add(new Position(2,2));
        spawnPointWidth = 10;
        spawnPointHeight = 10;
    }

    public Position getSpawn() {
        Position newSpawn = spawnPoints.get(0);
        while (spawnPoints.contains(newSpawn)/*&&Positions.get(newSpawn)!=Material.WALL*/){
            Random rand = new Random();
            //10 is max steps. Could lead to Map out of bounds Problems
            newSpawn = new Position(spawnPoints.get(0).x+rand.nextInt(10)*spawnPointWidth,spawnPoints.get(0).y+rand.nextInt(10)*spawnPointHeight);
        }
        spawnPoints.add(newSpawn);
        //TODO return specific OR random spawns from map
        return newSpawn;
    }

    @Override
    public String toString() {
        //TODO parse Positions back into string
        //char[] returnString = new char[0];
        //Positions.forEach();
        //for (int i = 0; i < Positions.size(); i++) {
        //    returnString[i] = Positions.get(i);
        //}
        return mapString; //can stay like this as long as the map stays the same
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




}
