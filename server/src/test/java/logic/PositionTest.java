package logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void add() {
        {
            Position actual = new Position(1, 1);
            actual.add(1, 5);

            Position expected = new Position(2, 6);
            assertEquals(expected, actual);
        }
        {
            Position actual = new Position(1, 1);
            actual.add(new Position(1, 5));

            Position expected = new Position(2, 6);
            assertEquals(expected, actual);
        }
    }

    @Test
    void set() {
        {
            Position actual = new Position(1, 1);
            actual.set(3, 5);

            Position expected = new Position(3, 5);
            assertEquals(expected, actual);
        }
        {
            Position actual = new Position(1, 1);
            actual.set(new Position(3, 5));

            Position expected = new Position(3, 5);
            assertEquals(expected, actual);
        }
    }
}