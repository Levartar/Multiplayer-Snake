package Logic;

import java.util.Arrays;
import java.util.List;

public class Snake {

    List<Pos> snake;
    boolean dead;
    Integer speed;

    public Snake(Pos spawn, Integer speed) {
        this.snake = Arrays.asList(spawn,spawn,spawn,spawn,spawn); //
        this.dead = true;
        this.speed = speed;
    }

    private List<Pos> spawnSnake(Pos spawn, Integer length){
        return null;
    }
    private List<Pos> spawnSnake(Pos spawn){ //default length 5
        Integer length = 5;
        return null;
    }

}
