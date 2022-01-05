package helpers;

import java.net.URI;
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
        Path resourceDirectory = Paths.get("src","main","resources");
        return  Path.of(resourceDirectory.toFile().getAbsolutePath());
    }

    public static Path getMapPath(String mapName){
        return GetResourceDirectory().resolve("maps/"+mapName);
    }
}
