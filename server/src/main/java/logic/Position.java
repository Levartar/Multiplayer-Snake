package logic;

import java.util.Objects;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // copy constructor
    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void add(Position position) {
        this.x = x + position.x;
        this.y = y + position.y;
    }

    public void add(Direction direction) {
        switch (direction) {
            case up -> this.add(0, -1);
            case down -> this.add(0, 1);
            case left -> this.add(-1, 0);
            case right -> this.add(1, 0);
        }
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "x:" + x + "," + "y:" + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
