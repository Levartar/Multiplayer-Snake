package logic;

public enum Direction {
    up,
    down,
    left,
    right;

    public static Position addDirection(Direction direction, Position head){
        switch (direction) {
            case up -> head.add(0,-1);
            case down -> head.add(0,1);
            case left -> head.add(-1,0);
            case right -> head.add(1,0);
        }
        return head;
    }

    public static Direction getDirection(char input){
        switch (input) {
            case 'w':return Direction.up;
            case 'a':return Direction.left;
            case 's':return Direction.down;
            case 'd':return Direction.right;
        }
        return null;
    }

}
