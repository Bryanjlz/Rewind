import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    private GameThread game;
    private GamePanel panel;
    private Player player;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int GRID_SCREEN_RATIO = (int)(HEIGHT / 9.0);
    public MainFrame () {
        // create jframe
        super("Not a Nice Game");
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        player = new Player(MainFrame.GRID_SCREEN_RATIO * 6, MainFrame.GRID_SCREEN_RATIO * 5);
        System.out.println(HEIGHT + " " + WIDTH);
        game = new GameThread(player);
        Thread thread = new Thread(game);
        thread.start();

        panel = new GamePanel(game);
        this.add(panel);

        MyKeyListener keyListener = new MyKeyListener();
        this.addKeyListener(keyListener);

        MyMouseListener mouseListener = new MyMouseListener();
        this.addMouseListener(mouseListener);

        this.requestFocusInWindow();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setVisible(true);
    }

    private class MyKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setHoldUp(true);
                    break;
                case KeyEvent.VK_S:
                    player.setHoldDown(true);
                    break;
                case KeyEvent.VK_A:
                    if (!player.isHoldRight()) {
                        player.setHoldLeft(true);
                        player.setDirection("left");
                    }
                    break;
                case KeyEvent.VK_D:
                    if (!player.isHoldLeft()) {
                        player.setHoldRight(true);
                        player.setDirection("right");
                    }
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setHoldUp(false);
                    break;
                case KeyEvent.VK_S:
                    player.setHoldDown(false);
                    break;
                case KeyEvent.VK_A:
                    player.setHoldLeft(false);
                    player.getAcc().setX(player.getAcc().getX() * -1);
                    break;
                case KeyEvent.VK_D:
                    player.setHoldRight(false);
                    player.getAcc().setX(player.getAcc().getX() * -1);
                    break;
                case KeyEvent.VK_SPACE:
                    player.interact();
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
            }

        }
    }

    private class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }
    }
}
