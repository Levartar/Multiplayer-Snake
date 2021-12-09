package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Snake {

    List<Pos> positions;
    boolean dead;
    Integer speed;

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
            positions.add(spawn);
        }
    }

    public List<Pos> getPositions() {
        return positions;
    }
}
