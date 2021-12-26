package logic;

public enum Material {
    WALL ('#'),
    FREESPACE (' '),
    APPLE ('@');

    private final char material;

    Material(char symbol) {

        this.material = symbol;

    }

    public char getSymbol() {
        return this.material;
    }
}


