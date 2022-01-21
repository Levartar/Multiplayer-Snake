package logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private static final Logger log = LogManager.getLogger(Snake.class);

    private final Player player;
    private List<Position> positions;
    private Direction direction = Direction.up;

    public Snake(Position spawn, int length, Player player) {
        this.player = player;
        createSnakeOnSpawn(spawn, length);
        log.debug("snake \""+ player.getName() + "\" created");
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

        updateDirection();

        // move head
        Position head = positions.get(0);
        head.add(direction);
        log.trace("snake \""+ player.getName() + "\" moved to "+ head);
    }

    private void updateDirection() {
        Direction newDirection = Direction.getDirection(player.getInput());
        // only change direction, if a correct input is set
        if (newDirection != null) {
        switch (newDirection) {
            case up -> {
                if (this.direction == Direction.down) newDirection = Direction.down;
            }
            case left -> {
                if (this.direction == Direction.right) newDirection = Direction.right;
            }
            case down -> {
                if (this.direction == Direction.up)newDirection = Direction.up;
            }
            case right -> {
                if (this.direction == Direction.left) newDirection = Direction.left;
            }
        }
            this.direction = newDirection;
        }
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
        updateDirection();
        return direction;
    }

    public void grow(int count) {
        Position lastPosition = positions.get(positions.size() - 1);
        for (int i = 0; i < count; i++) {
            positions.add(new Position(lastPosition));
        }
        log.trace("snake \""+ player.getName() + "\" grew by "+ count );
    }

    public int length() {
        return positions.size();
    }

    public Player getPlayer(){
        return player;
    }

    @Override
    public String toString() {
        return "Snake{" +
                "player=" + player +
                '}';
    }
}
