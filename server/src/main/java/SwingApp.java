import exceptions.GameNotInitializedException;
import exceptions.GameOverException;
import logic.Gamemode;
import logic.Map;
import logic.Player;
import logic.gamemodes.BasicSnake;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class SwingApp extends JFrame implements KeyListener {

    private final String TUTORIAL_TEXT = "Player 1: use WASD to move\nPlayer 2: use ←↑↓→ to move\n";

    private JTextPane textPane;

    private Gamemode gamemode;
    private Player player1;
    private Player player2;
    private boolean gameOver = false;

    public SwingApp(String title) throws HeadlessException, InterruptedException, GameNotInitializedException {
        super(title);
        // swing init
        swinit();
        // snake init
        sninit();
        countdown(3);
        while (!gameOver) {
            Thread.sleep(400);
            update();
            render();
        }
    }

    private void swinit() {
        setBounds(0, 0, 1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textPane = new JTextPane();
        textPane.setBackground(Color.BLACK);
        textPane.setForeground(Color.LIGHT_GRAY);
        textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 45));
        textPane.setEditable(false);

        // line spacing
        textPane.selectAll();
        MutableAttributeSet set = new SimpleAttributeSet(textPane.getParagraphAttributes());
        StyleConstants.setLineSpacing(set, -0.35f);
        textPane.setParagraphAttributes(set, false);

        textPane.addKeyListener(this);

        getContentPane().add(textPane);
        setVisible(true);
    }

    private void sninit() {
        String mapString = """
                ####################################
                #                                  #
                #                                  #
                #                                  #
                #                                  #
                #                                  #
                #            s   @   s             #
                #                                  #
                #                                  #
                #                                  #
                ####################################""";
        Map map = new Map(mapString);

        List<Player> players = new ArrayList<>(2);
        player1 = new Player();
        player2 = new Player();
        players.add(player1);
        players.add(player2);

        gamemode = new BasicSnake(players, map,0);
        gamemode.init();
    }

    private void countdown(int seconds) throws InterruptedException {
        for (int i = seconds; i > 0; i--) {
            textPane.setText(TUTORIAL_TEXT + i);
            Thread.sleep(1000);
        }
        render();
    }

    private void update() throws GameNotInitializedException {
        try {
            gamemode.gameLoop();
        } catch (GameOverException e) {
            gameOver = true;
        }
    }

    private void render() {
        if (gameOver) textPane.setText("Game Over!");
        else textPane.setText(TUTORIAL_TEXT + "time left: " + gamemode.getTimeLeft() + " seconds\n" + gamemode.toString());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char input = e.getKeyChar();
        switch (input) {
            // wasd
            case 'w', 'a', 's', 'd' -> player1.setInput(input);
            // arrow keys
            case '\uFFFF' -> {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case 37 -> player2.setInput('a'); // left
                    case 38 -> player2.setInput('w'); // up
                    case 39 -> player2.setInput('d'); // right
                    case 40 -> player2.setInput('s'); // down
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) throws InterruptedException, GameNotInitializedException {
        new SwingApp("Snake.io");
    }
}
