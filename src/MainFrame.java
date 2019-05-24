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
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int GRID_SCREEN_RATIO = HEIGHT / 25;
    public MainFrame () {
        // create jframe
        super("Not a Nice Game");
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);

        game = new GameThread();
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
                    game.getPlayer().setHoldUp(true);
                    break;
                case KeyEvent.VK_S:
                    game.getPlayer().setHoldDown(true);
                    break;
                case KeyEvent.VK_A:
                    game.getPlayer().setHoldLeft(true);
                    break;
                case KeyEvent.VK_D:
                    game.getPlayer().setHoldRight(true);
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    game.getPlayer().setHoldUp(false);
                    break;
                case KeyEvent.VK_S:
                    game.getPlayer().setHoldDown(false);
                    break;
                case KeyEvent.VK_A:
                    game.getPlayer().setHoldLeft(false);
                    game.getPlayer().getAcc().setxAcc(game.getPlayer().getAcc().getxAcc() * -1);
                    break;
                case KeyEvent.VK_D:
                    game.getPlayer().setHoldRight(false);
                    game.getPlayer().getAcc().setxAcc(game.getPlayer().getAcc().getxAcc() * -1);
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
