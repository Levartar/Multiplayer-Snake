package logic;

public enum Material {
    WALL ('.'),
    FREESPACE (' '),
    APPLE ('#');

    private final char material;

    Material(char symbol) {

        this.material = symbol;

    }

    public char getSymbol() {
        return this.material;
    }

    //Propably isnt needed
    //public char getStatus() {
    //    switch (this.material) {
    //        case WALL -> {return 'd';}
    //        case APPLE -> {return 'g';}
    //        case FREESPACE -> {return ' ';}
    //    }
    //}
}


