package helpers;

import logic.Map;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>ResourceManager class.</p>
 * Collection for static methods for dealing with Paths and Streams
 * @version $Id: $Id
 */
public class ResourceManager {

    /**
     * <p>GetResourceDirectory.</p>
     * gets the individual Path to the current resource directory
     * @return a {@link Path} object.
     */
    public static Path GetResourceDirectory(){
        try{
            Path dir = Paths.get(ResourceManager.class.getResource("/").toURI()).getParent().getParent();
            Path resourceDirectory = Paths.get(dir.toString(),"src/main/resources");
            return resourceDirectory;
        }catch (Exception e){
            System.out.println("Resource Directory not Found. error in getResourceDirectory() wrong Code");
            return null;
        }
    }

    public static Path getMapPath(String mapName){
        return GetResourceDirectory().resolve("maps/"+mapName);
    }
}
