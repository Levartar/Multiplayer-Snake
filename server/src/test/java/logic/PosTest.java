package logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PosTest {

    @Test
    void addXY() {
        Pos actual = new Pos(1, 1);
        actual.add(1,5);

        Pos expected = new Pos(2, 6);
        assertEquals(expected, actual);
    }

    @Test
    void testPosition() {
        Pos actual = new Pos(1, 1);
        actual.add(new Pos(1, 5));

        Pos expected = new Pos(2, 6);
        assertEquals(expected, actual);
    }
}