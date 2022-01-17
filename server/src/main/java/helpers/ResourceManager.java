package helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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
}
