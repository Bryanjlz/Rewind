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
    static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    static int gridScreenRatio;
    public MainFrame () {
        // create jframe
        super("Not a Nice Game");
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        player = new Player();
        System.out.println(HEIGHT + " " + WIDTH);
        game = new GameThread(player);
        Thread thread = new Thread(game);
        thread.start();

        panel = new GamePanel(game);
        panel.setThread(thread);
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
                    if (!player.isReversing()) {
                        player.setHoldUp(true);
                    }
                    break;
                case KeyEvent.VK_A:
                    if (!player.isHoldRight() && !player.isReversing()) {
                        player.setHoldLeft(true);
                        if (player.isHoldingStone() && player.getDirection().equals("right")) {
                            Rectangle stoneBox = new Rectangle(player.getHeldStone().getHitbox());
                            int xMove = (int)-(stoneBox.getWidth() + player.getHitbox().getWidth());
                            stoneBox.translate(xMove, 0);
                            Terrain[][] terrain = game.getCurrentLevel().getTerrain();
                            boolean wallClip = false;
                            for (int i = 0; i < terrain.length && !wallClip; i++) {
                                for (int j = 0; j < terrain[0].length && !wallClip; j++) {
                                    if (terrain[i][j] != null && stoneBox.intersects(terrain[i][j].getHitbox())) {
                                        wallClip = true;
                                    }
                                }
                            }
                            MyArrayList<Stone> stones = game.getCurrentLevel().getStones();
                            for (int i = 0; i < stones.size() && !wallClip; i++) {
                                if (stones.get(i) != player.getHeldStone() && stoneBox.intersects(stones.get(i).getHitbox())) {
                                    wallClip = true;
                                }
                            }
                            if (!wallClip) {
                                player.setDirection("left");
                            }
                        } else {
                            player.setDirection("left");
                        }
                    }
                    break;
                case KeyEvent.VK_D:
                    if (!player.isHoldLeft() && !player.isReversing()) {
                        player.setHoldRight(true);
                        if (player.isHoldingStone() && player.getDirection().equals("left")) {
                            Rectangle stoneBox = new Rectangle(player.getHeldStone().getHitbox());
                            int xMove = (int)(player.getHitbox().getWidth() + stoneBox.getWidth());
                            stoneBox.translate(xMove, 0);
                            Terrain[][] terrain = game.getCurrentLevel().getTerrain();
                            boolean wallClip = false;
                            for (int i = 0; i < terrain.length && !wallClip; i++) {
                                for (int j = 0; j < terrain[0].length && !wallClip; j++) {
                                    if (terrain[i][j] != null && stoneBox.intersects(terrain[i][j].getHitbox())) {
                                        player.setDirection("right");
                                        wallClip = true;
                                    }
                                }
                            }
                            MyArrayList<Stone> stones = game.getCurrentLevel().getStones();
                            for (int i = 0; i < stones.size() && !wallClip; i++) {
                                if (stones.get(i).getHitbox() != stoneBox && stoneBox.intersects(stones.get(i).getHitbox())) {
                                    player.setDirection("right");
                                    wallClip = true;
                                }
                            }
                            if (!wallClip) {
                                player.setDirection("right");
                            } else {
                                player.setDirection("left");
                            }
                        } else {
                            player.setDirection("right");
                        }
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
                case KeyEvent.VK_A:
                    player.setHoldLeft(false);
                    player.getAcc().setX(player.getAcc().getX() * -1);
                    break;
                case KeyEvent.VK_D:
                    player.setHoldRight(false);
                    player.getAcc().setX(player.getAcc().getX() * -1);
                    break;
                case KeyEvent.VK_R:
                    if (!game.isMenu()) {
                        panel.setTransition(true);
                        game.setRestartLevel(true);
                    }
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
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            if (!game.isMenu()) {
                MyArrayList<Stone> stones = game.getCurrentLevel().getStones();
                boolean foundReverse = false;
                for (int i = 0; i < stones.size() && !foundReverse; i++) {
                    if (stones.get(i).getHitbox().contains(mousePos)) {
                        foundReverse = true;
                        player.setReversing(true);
                        stones.get(i).setReverse(true);
                    }

                }
                if (!foundReverse && player.getHitbox().contains(mousePos)) {
                    player.setReverse(true);
                    player.setReversing(true);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            if (game.isMenu()) {
                if (game.getPlayHitbox().contains(mousePos)) {
                    game.setMenu(false);

                }
            }
            player.setReversing(false);
            MyArrayList<Stone> stones = game.getCurrentLevel().getStones();
            boolean foundReverse = false;
            Terrain[][] terrain = game.getCurrentLevel().getTerrain();
            for (int i = 0; i < stones.size() && !foundReverse; i++) {
                Stone stone = stones.get(i);
                if (stone.isReverse()) {
                    foundReverse = true;
                    stone.setReverse(false);
                    boolean foundCollide = false;
                    if (stone.getHitbox().intersects(player.getHitbox())) {
                        foundCollide = true;
                        panel.setTransition(true);
                        game.setRestartLevel(true);
                    }
                    for (int j = 0; j < stones.size() && !foundCollide; j++) {
                        if (stone != stones.get(j) && stone.getHitbox().intersects(stones.get(j).getHitbox())) {
                            foundCollide = true;
                            panel.setTransition(true);
                            game.setRestartLevel(true);
                        }
                    }
                    for (int j = 0; j < terrain.length && !foundCollide; j++) {
                        for (int k = 0; k < terrain[0].length && !foundCollide; k++) {
                            if (terrain[j][k] instanceof Wall && stone.getHitbox().intersects(terrain[j][k].getHitbox())) {
                                foundCollide = true;
                                panel.setTransition(true);
                                game.setRestartLevel(true);
                            }
                        }
                    }
                }

            }
            if (!foundReverse && player.isReverse()) {
                player.setReverse(false);
                boolean foundCollide = false;
                for (int j = 0; j < stones.size() && !foundCollide; j++) {
                    if (player.getHitbox().intersects(stones.get(j).getHitbox())) {
                        foundCollide = true;
                        panel.setTransition(true);
                        game.setRestartLevel(true);
                    }
                }
                for (int j = 0; j < terrain.length && !foundCollide; j++) {
                    for (int k = 0; k < terrain[0].length && !foundCollide; k++) {
                        if (terrain[j][k] instanceof Wall && player.getHitbox().intersects(terrain[j][k].getHitbox())) {
                            foundCollide = true;
                            panel.setTransition(true);
                            game.setRestartLevel(true);
                        }
                    }
                }
            }
        }
    }
}
