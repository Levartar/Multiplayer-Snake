package logic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        for (int i = 0; i < snake.getPositions().size() - 1; i++) {
            assertNotSame(snake.getPositions().get(i), snake.getPositions().get(i + 1));
        }
    }

    @Test
    void moveHead() {
        //testHead
        List<Pos> testPositions = new ArrayList<>();
        testPositions.add(new Pos(1,2));
        Snake testSnake = new Snake(new Pos(1,1), 1, 1);
        testSnake.move(Snake.Direction.up);

        assertEquals(testPositions, testSnake.getPositions());

        for (int i = 0; i < testSnake.getPositions().size() - 1; i++) {
            assertNotSame(testSnake.getPositions().get(i), testSnake.getPositions().get(i + 1));
        }
    }

    @Test
    void moveSnake() {
        //testLongersnake
        List<Pos> testPositions = new ArrayList<>();
        testPositions.add(new Pos(3,1));
        testPositions.add(new Pos(2,1));
        testPositions.add(new Pos(1,1));

        Snake testSnake = new Snake(new Pos(1,1), 1, 3);
        for (int i = 0; i < 2 ; i++) { //move right 2 times
            testSnake.move(Snake.Direction.right);
        }

        assertEquals(testPositions, testSnake.getPositions());

        for (int i = 0; i < testSnake.getPositions().size() - 1; i++) {
            assertNotSame(testSnake.getPositions().get(i), testSnake.getPositions().get(i + 1));
        }
    }
}