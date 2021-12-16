package logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Map {

 String map;
 //map can kind of stay as string. The Material class is a bit unnecessary

 public String getMap() {
     return map;
 }

 //TODO Add LoadMap from DB
 public void loadMap(Path mapPath) throws IOException {
     this.map = Files.readString(mapPath, StandardCharsets.UTF_8);
 }


}
