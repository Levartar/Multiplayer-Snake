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
}
