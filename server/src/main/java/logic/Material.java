package logic;

/**
 * This class contains all possible materials in the game as enums.
 */
public enum Material {
    WALL('#'),
    FREESPACE(' '),
    APPLE('@'),
    APPLETREE('T'),
    SNAKEHEAD('H'),
    SNAKE('o'),
    SPAWN('s');

    private final char symbol;

    Material(char symbol) {
        this.symbol = symbol;
    }

    /**
     *
     * @return The character representing the material.
     */
    public char getSymbol() {
        return this.symbol;
    }

    @Override
    public String toString() {
        return Character.toString(getSymbol());
    }

    /**
     * Returns the enum matching the given character.
     * @param symbol Character that represents an enum of a {@link Material}.
     * @return the enum matching the given character.
     */
    public static Material getMaterial(char symbol) {
        for (Material material : values()) {
            if (symbol == material.symbol) {
                return material;
            }
        }
        return null;
    }
}


