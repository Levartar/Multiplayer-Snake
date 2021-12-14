package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Snake {

    List<Pos> positions;
    boolean dead;
    Integer speed;

    enum Direction{ //maybe create new class for enum or put in Pos class
        up,
        down,
        left,
        right
    }

    public Snake(Pos spawn, int speed, int length) {
        this.dead = true;
        this.speed = speed;

        spawnSnake(spawn, length);
    }

    private void spawnSnake(Pos spawn, int length) {
        if (positions == null) {
            this.positions = new ArrayList<>();
        } else {
            positions.clear();
        }

        for (int i = 0; i < length; i++) {
            positions.add(new Pos(spawn));
        }
        this.dead = false; //snake spawn and is alive
    }

    public void move(Direction input){
        //move the head and set the tail to each previous Element
        //head is first element in posList
        //first copy tail then set head
        for (int i = positions.size() - 1; i > 0; i--) {
            positions.set(i,positions.get(i-1)); //sets all elements to the previous element
        }

        Pos head = new Pos(positions.get(0));

        switch (input) { //switch case can be edited to support more directions. Also maybe put it in directions class
            case up -> head.add(0,1);
            case down -> head.add(0,-1);
            case left -> head.add(-1,0);
            case right -> head.add(1,0);
        }
        //set first to new head
        positions.set(0,head);

    }



    public List<Pos> getPositions() {
        return positions;
    }
}
