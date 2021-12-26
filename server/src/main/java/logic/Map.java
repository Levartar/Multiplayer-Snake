package logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Map {

    HashMap<Position, Material> Positions = new HashMap<>();
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

    @Override
    public String toString() {
        //Maybe parse Positions back into string for tests but this is very unnecessary
        //char[] returnString = new char[0];
        //Positions.forEach();
        //for (int i = 0; i < Positions.size(); i++) {
        //    returnString[i] = Positions.get(i);
        //}
        return mapString;
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
