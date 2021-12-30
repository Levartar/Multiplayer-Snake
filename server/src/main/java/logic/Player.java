package logic;

public class Player {
    private String name;
    private char input;
    private String color;
    private int id;
    private int highScore;

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
        this.highScore = highScore;
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
