package logic;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    List<Position> positions;
    Direction direction = null;
    boolean dead;
    Player player;

    public Snake(Position spawn, int length, Player player) {
        this.dead = true;
        this.player = player;

        createSnakeOnSpawn(spawn, length);
    }

    private void createSnakeOnSpawn(Position spawn, int length) {
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

    //TODO snakes can move to the other side
    public void move(){
        this.direction = player.getDirection();

        for (int i = positions.size() - 1; i > 0; i--) {
            Position thisElement = positions.get(i);
            Position priorElement = positions.get(i - 1);
            thisElement.set(priorElement);
        }

        Position head = positions.get(0);

        switch (direction) { //switch case can be edited to support more directions. Also maybe put it in directions class
            case up -> head.add(0,1);
            case down -> head.add(0,-1);
            case left -> head.add(-1,0);
            case right -> head.add(1,0);
        }
    }

    //TODO what happens when snakes dies
    public void die(){
        this.dead=true;
    }

    public String getName() {
        return player.getName();
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Direction getDirection() {
        return direction;
    }
}
