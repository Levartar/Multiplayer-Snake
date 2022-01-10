package logic;

public class Player {
    private String name;
    private char input;
    private String color;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
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

    public void setInput(char input){
        switch (input) {
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
        }
    }


    public String getName(){
        return name;
    }
}
