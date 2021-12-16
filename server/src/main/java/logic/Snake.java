package logic;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    List<Position> positions;
    boolean dead;
    Integer speed;

    enum Direction{ //maybe create new class for enum or put in Pos class
        up,
        down,
        left,
        right
    }

    public Snake(Position spawn, int speed, int length) {
        this.dead = true;
        this.speed = speed;

        spawnSnake(spawn, length);
    }

    private void spawnSnake(Position spawn, int length) {
        if (positions == null) {
            this.positions = new ArrayList<>();
        } else {
            positions.clear();
        }

        for (int i = 0; i < length; i++) {
            positions.add(new Position(spawn));
        }
        this.dead = false; //snake spawn and is alive
    }

    public void move(Direction input){

        for (int i = positions.size() - 1; i > 0; i--) {
            Position thisElement = positions.get(i);
            Position priorElement = positions.get(i - 1);
            thisElement.set(priorElement);
        }

        Position head = positions.get(0);

        switch (input) { //switch case can be edited to support more directions. Also maybe put it in directions class
            case up -> head.add(0,1);
            case down -> head.add(0,-1);
            case left -> head.add(-1,0);
            case right -> head.add(1,0);
        }
    }



    public List<Position> getPositions() {
        return positions;
    }
}
