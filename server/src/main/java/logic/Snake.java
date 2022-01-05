package logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private static final Logger logger = LogManager.getLogger(Snake.class);
    private final Player player;
    private List<Position> positions;
    private Direction direction = null;

    public Snake(Position spawn, int length, Player player) {
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
    }

    public void move() {
        // move body parts except head
        for (int i = positions.size() - 1; i > 0; i--) {
            Position thisElement = positions.get(i);
            Position priorElement = positions.get(i - 1);
            thisElement.set(priorElement);
        }

        Direction newDirection = Direction.getDirection(player.getInput());
        // only change direction, if a correct input is set
        if (newDirection != null) {
            this.direction = newDirection;
        }

        // move head
        Position head = positions.get(0);
        head.add(direction);
    }

    public String getName() {
        return player.getName();
    }

    public Position getHead() {
        return positions.get(0);
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Direction getDirection() {
        return direction;
    }

    public void grow(int count) {
        Position lastPosition = positions.get(positions.size() - 1);
        for (int i = 0; i < count; i++) {
            positions.add(new Position(lastPosition));
        }
    }

    public int length() {
        return positions.size();
    }

    public Player getPlayer(){
        return player;
    }
}