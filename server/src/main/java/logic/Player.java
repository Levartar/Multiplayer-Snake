package logic;

public class Player {
    String name;
    char input;
    String color;
    int id;

    int highScore;

    public Direction getDirection(){
        switch (this.input) {
            case 'w':return Direction.up;
            case 'a':return Direction.left;
            case 's':return Direction.down;
            case 'd':return Direction.right;
        }
        return null;
    }

    public void setInput(char input){
        this.input = input;
    }

    public void writeHighScoreToDB() {
        //TODO write HS to DataBase
    }

    public String getName(){
        return name;
    }
}
