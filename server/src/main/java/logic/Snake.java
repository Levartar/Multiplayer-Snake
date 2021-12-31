package logic;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private List<Position> positions;
    private Direction direction = null;
    private boolean dead;
    private Player player;

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

    public void move(){
        this.direction = Direction.getDirection(player.getInput());

        for (int i = positions.size() - 1; i > 0; i--) {
            Position thisElement = positions.get(i);
            Position priorElement = positions.get(i - 1);
            thisElement.set(priorElement);
        }

        Position head = positions.get(0);
        direction.addDirection(direction,head);
    }

    //TODO what happens when snakes dies
    public void die(){
        dead = true;
        positions.clear();
    }

    public String getName() {
        return player.getName();
    }

    public Position getHead() {return positions.get(0);}

    public List<Position> getPositions() {
        return positions;
    }

    public Direction getDirection() {
        return direction;
    }
}
