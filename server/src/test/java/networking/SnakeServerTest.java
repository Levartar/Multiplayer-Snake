package networking;

import org.junit.jupiter.api.Test;

class SnakeServerTest {

    @Test
    void run() {
        SnakeServer server = new SnakeServer(80);
        server.run();
    }
}