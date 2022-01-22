package logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player {

    private static final Logger log = LogManager.getLogger(Player.class);

    private String name;
    private char input;
    private String color;

    public Player() {
    }

    public Player(String name) {
        log.trace("player"+ name+ " created");
        this.name = name;
    }

    public void setName(String name) {
        log.trace("player " + this.name + " changed name to " + name);
        this.name = name;
    }

    public char getInput() {
        return input;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setInput(char input){
        /*switch (input) {
            case 'w' -> {
                if (this.input != 's') this.input = 'w';
            }
            case 'a' -> {
                if (this.input != 'd') this.input = 'a';
            }
            case 's' -> {
                if (this.input != 'w') this.input = 's';
            }
            case 'd' -> {
                if (this.input != 'a') this.input = 'd';
            }
        }*/
        this.input = Character.toLowerCase(input);
        log.debug("player "+name+ " set input to "+ input );
    }

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
