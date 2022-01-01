package logic;

public enum Direction {
    up,
    down,
    left,
    right;

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
