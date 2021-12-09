package Logic;

public class Pos {

    Integer x;
    Integer y;

    public Pos(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void add(Integer number) {
        this.x = x+number;
        this.y = y+number;
    }

    public void add(Pos position) {
        this.x = x+position.x;
        this.y = y+position.y;
    }
}
