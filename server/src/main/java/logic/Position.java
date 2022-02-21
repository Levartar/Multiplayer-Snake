package logic;

import java.util.Objects;

/**
 * The {@link Position} class stores the x and y coordinate. Representing the positing within the {@link Map}.
 */
public class Position {

    private int x;
    private int y;

    /**
     * The standard constructor. Setting the x and y coordinate.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor to generate a copy of a given {@link Position}.
     * @param position The original {@link Position} of the copy.
     */
    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    /**
     * Changes the represented {@link Position} by adding values to the x and y coordinate.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Changes the represented {@link Position} by adding values to the x and y coordinate
     * according to the amount of x and y of the given position.
     * @param position The {@link Position} adding onto this {@link Position}.
     */
    public void add(Position position) {
        this.x = x + position.x;
        this.y = y + position.y;
    }

    /**
     * Changes the represented {@link Position} depending on the given {@link Direction}.
     * @param direction The {@link Direction} indicating how to change x and y coordinate.
     */
    public void add(Direction direction) {
        switch (direction) {
            case up -> this.add(0, -1);
            case down -> this.add(0, 1);
            case left -> this.add(-1, 0);
            case right -> this.add(1, 0);
        }
    }

    /**
     * Set the x and y coordinate by a given value.
     * @param x The value of x to set.
     * @param y The value of y to set.
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the x and y coordinate to a given {@link Position}.
     * @param position The original {@link Position} for the x and y coordinate.
     */
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

    /**
     *
     * @return The x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return The y coordinate.
     */
    public int getY() {
        return y;
    }
}
