package logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Snake} class is responsible for spawning a snake, its movements and let it grow if a Snake eats an apple.
 */
public class Snake {

    private static final Logger log = LogManager.getLogger(Snake.class);

    /**
     * The {@link Player} instance belonging to this {@link Snake} instance.
     */
    private final Player player;
    /**
     * List of {@link Position} representing the position of the snakes body.
     */
    private List<Position> positions;
    /**
     * The {@link Direction} which the head of the snake is facing to.
     */
    private Direction direction = Direction.up;

    /**
     * Builds a snake instance by setting the associated {@link Snake#player} and creating
     * a list {@link Snake#positions} representing the body at the desired spawnpoint.
     * @param spawn A {@link Position} of the spawnpoint of the snake.
     * @param length Represents the initial length of the snake.
     * @param player Sets the {@link Snake#player}.
     */
    public Snake(Position spawn, int length, Player player) {
        this.player = player;
        createSnakeOnSpawn(spawn, length);
        log.debug("snake \""+ player.getName() + "\" created");
    }

    /**
     * Creates the list {@link Snake#positions} with the positions of the snake body.
     * @param spawn The {@link Position} of the spawnpoint.
     * @param length The initial length of the snake.
     */
    private void createSnakeOnSpawn(Position spawn, int length) {
        if (positions == null) {
            this.positions = new ArrayList<>();
        } else {
            positions.clear();
        }

        for (int i = 0; i < length; i++) {
            positions.add(new Position(spawn));
        }
        log.trace("Snake:\"" + this + "\" was created.");
    }

    /**
     * Moves the snake one tile in the given {@link Snake#direction}
     */
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

    /**
     * Depending on the return of {@link Player#getInput()} the {@link Snake#direction} will be updated.
     */
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

    /**
     *
     * @return The Name of the {@link Snake#player}.
     */
    public String getName() {
        return player.getName();
    }

    /**
     *
     * @return The {@link Position} of the snakes head.
     */
    public Position getHead() {
        return positions.get(0);
    }

    /**
     *
     * @return The snakes body represented by the list {@link Snake#positions}.
     */
    public List<Position> getPositions() {
        return positions;
    }

    /**
     *
     * @return The {@link Direction} the snakes head is facing.
     */
    public Direction getDirection() {
        updateDirection();
        return direction;
    }

    /**
     * Growing the snake by a certain value.
     * @param count Represents how much the snake should grow.
     */
    public void grow(int count) {
        Position lastPosition = positions.get(positions.size() - 1);
        for (int i = 0; i < count; i++) {
            positions.add(new Position(lastPosition));
        }
        log.trace("snake \""+ player.getName() + "\" grew by "+ count );
    }

    /**
     *
     * @return The length of snake.
     */
    public int getLength() {
        return positions.size();
    }

    /**
     *
     * @return The instance of {@link Snake#player} associated with this snake.
     */
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
