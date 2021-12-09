package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SnakeTest {

    @Test
    void getPositions() {
        int length = 5;
        Pos spawnPosition = new Pos(1, 1);

        List<Pos> testPositions = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            testPositions.add(spawnPosition);
        }

        Snake snake = new Snake(spawnPosition, 0, 5);

        assertEquals(testPositions, snake.getPositions());
    }
}