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
        {
            Position actual = new Position(1, 1);

            actual.add(Direction.right);
            Position expected = new Position(2, 1);
            assertEquals(expected, actual);

            actual.add(Direction.down);
            expected = new Position(2, 2);
            assertEquals(expected, actual);

            actual.add(Direction.left);
            expected = new Position(1, 2);
            assertEquals(expected, actual);

            actual.add(Direction.up);
            expected = new Position(1, 1);
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

    @Test
    void testHashCode() {
        {
            int hash1 = new Position(1, 2).hashCode();
            int hash2 = new Position(2, 1).hashCode();

            assertNotEquals(hash1, hash2);
        }
        {
            int hash1 = new Position(1, 1).hashCode();
            int hash2 = new Position(1, 1).hashCode();

            assertEquals(hash1, hash2);
        }
    }
}