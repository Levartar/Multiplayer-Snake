package logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents the player with the chosen name and contains the input which controls the
 * movement of the snake.
 */
public class Player {

    private static final Logger log = LogManager.getLogger(Player.class);

    private String name;
    private char input;
    private String color;

    /**
     * Constructor used for tests because no Name is necessary.
     */
    public Player() {
    }

    /**
     * The constructor for actual the {@link Player} instance. Also sets a random {@link Player#color} in hex format.
     * @param name The name of the player.
     */
    public Player(String name) {
        log.trace("player"+ name+ " created");
        this.name = name;
        this.color = String.format("#%02x%02x%02x",(int)Math.floor(Math.random()*255),(int)Math.floor(Math.random()*255),(int)Math.floor(Math.random()*255));
        log.debug("Generated " + this);
    }

    /**
     *
     * @param name The new name of the Player.
     */
    public void setName(String name) {
        log.trace("player " + this.name + " changed name to " + name);
        this.name = name;
    }

    /**
     *
     * @return The input transferred by the frontend ('w','a','s','d') indicating the next movement of the snake.
     */
    public char getInput() {
        return input;
    }

    /**
     *
     * @return The color of the player and its snake in hex format.
     */
    public String getColor() {
        return color;
    }

    /**
     *
     * @param color The color to set.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @param input The input transferred by the frontend ('w','a','s','d') indicating the next movement of the snake.
     */
    public void setInput(char input){
        this.input = Character.toLowerCase(input);
        log.trace("player "+name+ " set input to "+ input );
    }

    /**
     *
     * @return The name of the Player.
     */
    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
