package logic;

public enum Material {
    WALL('#'),
    FREESPACE(' '),
    APPLE('@'),
    SNAKE('o'),
    SPAWN('+');

    private final char symbol;

    Material(char symbol) {

        this.symbol = symbol;

    }

    public char getSymbol() {
        return this.symbol;
    }

    @Override
    public String toString() {
        return Character.toString(getSymbol());
    }

    public static Material getMaterial(char symbol) {
        for (Material material : values()) {
            if (symbol == material.symbol) {
                return material;
            }
        }
        return null;
    }
}


