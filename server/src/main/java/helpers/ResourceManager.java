package helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>ResourceManager class.</p>
 * Collection for static methods for dealing with Paths and Streams
 *
 * @version $Id: $Id
 */
public class ResourceManager {
    private static final Logger log = LogManager.getLogger(ResourceManager.class);

    public static String getMapString(String fileName) throws IOException {
        InputStream inputStream = ResourceManager.class.getClassLoader().getResourceAsStream("maps/" + fileName);
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }
        String fileContent = buf.toString(StandardCharsets.UTF_8);
        log.debug("file content = " + fileContent);
        return fileContent;
    }

    public static List<String> getMapNames() throws IOException {
        List<String> mapNames = new ArrayList<>();

//        String directory = ResourceManager.class.getClassLoader().;
//
//        log.info("directory = " + directory);

        URI uri = null;
        try {
            uri = ResourceManager.class.getClassLoader().getResource("maps/").toURI();
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }

        Path path;
        if (uri.getScheme().equals("jar")) {
            // if in a jar, create a FileSystem
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
            path = fileSystem.getPath("/maps");
        } else {
            path = Paths.get(uri);
        }

        Stream<Path> list = Files.list(path);
        for (Iterator<Path> it = list.iterator(); it.hasNext(); ) {
            mapNames.add(it.next().getFileName().toString());
        }
        return mapNames;
    }
}
