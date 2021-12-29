package logic;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    List<Position> positions;
    String name;
    Direction direction = null;
    boolean dead;

    public Snake(Position spawn, int length, String name) {
        this.dead = true;
        this.name = name;

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
        this.direction = input;

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

    public void die(){
        this.dead=true;
    }

    public String getName() {
        return name;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Direction getDirection() {
        return direction;
    }
}
