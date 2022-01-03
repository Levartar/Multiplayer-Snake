package logic;

public class Player {
    private String name;
    private char input;
    private String color;
    private int id;
    private int highScore;

    public Player() {
    }

    public Player(String name, String color, int id) {
        this.name = name;
        this.color = color;
        this.id = id;
    }

    public void setName(String name) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        if (highScore>this.highScore){
            this.highScore = highScore;
        }
    }

    public void setInput(char input){
        switch (input) {
            case 'w':if (!(this.input=='s')){this.input = 'w';}
            case 'a':if (!(this.input=='d')){this.input = 'a';}
            case 's':if (!(this.input=='w')){this.input = 's';}
            case 'd':if (!(this.input=='a')){this.input = 'd';}
        }
        this.input = input;
    }

    public void writeHighScoreToDB() {
        //TODO write HS to DataBase
    }

    public String getName(){
        return name;
    }
}
