package logic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SnakeTest {

    @Test
    void getPositions() {
        int length = 5;
        Position spawnPosition = new Position(1, 1);

        List<Position> testPositions = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            testPositions.add(spawnPosition);
        }

        Snake snake = new Snake(spawnPosition, 5,new Player());


        assertEquals(testPositions, snake.getPositions());

        for (int i = 0; i < snake.getPositions().size() - 1; i++) {
            assertNotSame(snake.getPositions().get(i), snake.getPositions().get(i + 1));
        }
    }

    @Test
    void moveHead() {
        //testHead
        List<Position> testPositions = new ArrayList<>();
        testPositions.add(new Position(1,2));

        Player testPlayer = new Player();
        testPlayer.setInput('w');

        Snake testSnake = new Snake(new Position(1,1), 1, testPlayer);
        testSnake.move();

        assertEquals(testPositions, testSnake.getPositions());

        for (int i = 0; i < testSnake.getPositions().size() - 1; i++) {
            assertNotSame(testSnake.getPositions().get(i), testSnake.getPositions().get(i + 1));
        }
    }

    @Test
    void moveSnake() {
        List<Position> testPositions = new ArrayList<>();
        testPositions.add(new Position(3,1));
        testPositions.add(new Position(2,1));
        testPositions.add(new Position(1,1));

        Player testPlayer = new Player();
        testPlayer.setInput('d');

        Snake testSnake = new Snake(new Position(1,1), 3, testPlayer);

        for (int i = 0; i < 2 ; i++) { //move right 2 times
            testSnake.move();
        }

        assertEquals(testPositions, testSnake.getPositions());

        for (int i = 0; i < testSnake.getPositions().size() - 1; i++) {
            assertNotSame(testSnake.getPositions().get(i), testSnake.getPositions().get(i + 1));
        }
    }

    @Test
    void complexMoveSnake(){
        List<Position> testPositions = new ArrayList<>();
        testPositions.add(new Position(3,3));
        testPositions.add(new Position(3,2));
        testPositions.add(new Position(2,2));
        testPositions.add(new Position(2,1));

        Player testPlayer = new Player();

        Snake testSnake = new Snake(new Position(1,1), 4, testPlayer);

        testPlayer.setInput('d');
        testSnake.move();
        testPlayer.setInput('w');
        testSnake.move();
        testPlayer.setInput('d');
        testSnake.move();
        testPlayer.setInput('w');
        testSnake.move();

        assertEquals(testPositions, testSnake.getPositions());
    }
}