package logic;

/**
 * This class interprets the input of a Player and converts into a {@link Direction}.
 */
public enum Direction {
    up,
    down,
    left,
    right;

    /**
     * Returns the matching {@link Direction} to the given player input.
     * @param input character input from the player.
     * @return enum that represents the direction.
     */
    public static Direction getDirection(char input) {
        return switch (input) {
            case 'w' -> Direction.up;
            case 'a' -> Direction.left;
            case 's' -> Direction.down;
            case 'd' -> Direction.right;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case up -> "up";
            case down -> "down";
            case left -> "left";
            case right -> "right";
        };
    }
}
