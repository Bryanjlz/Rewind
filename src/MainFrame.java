import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

public class MainFrame extends JFrame {
    private GameThread game;
    private GamePanel panel;
    private Player player;
    static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    static int gridScreenRatio;

    /**
     * Constructor for the JFrame of the game
     */
    public MainFrame () {
        // Create jframe
        super("Not a Nice Game");
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);

        // Create player and start game loop thread
        player = new Player();
        game = new GameThread(player);
        Thread thread = new Thread(game);
        thread.start();

        // Add the game panel
        panel = new GamePanel(game);
        panel.setThread(thread);
        this.add(panel);

        // Add key listener
        MyKeyListener keyListener = new MyKeyListener();
        this.addKeyListener(keyListener);

        // Add mouse listener
        MyMouseListener mouseListener = new MyMouseListener();
        this.addMouseListener(mouseListener);

        this.requestFocusInWindow();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setVisible(true);

        // Load and play background music
        try {
            File music = new File("assets/sounds/bgm.wav");
            AudioInputStream sound = AudioSystem.getAudioInputStream(music);
            DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
            Clip bgm = (Clip) AudioSystem.getLine(info);
            bgm.open(sound);
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
                    if (!player.isReversing() && !player.isHoldRight()) {
                        player.setHoldLeft(true);
                        if (player.isHoldingCrate() && player.getDirection().equals("right")) {
                            Rectangle crateBox = new Rectangle(player.getHeldCrate().getHitbox());
                            int xMove = (int)-(crateBox.getWidth() + player.getHitbox().getWidth());
                            crateBox.translate(xMove, 0);
                            Terrain[][] terrain = game.getCurrentLevel().getTerrain();
                            boolean wallClip = false;
                            for (int i = 0; i < terrain.length && !wallClip; i++) {
                                for (int j = 0; j < terrain[0].length && !wallClip; j++) {
                                    if (terrain[i][j] != null && crateBox.intersects(terrain[i][j].getHitbox())) {
                                        wallClip = true;
                                    }
                                }
                            }
                            MyArrayList<Crate> crates = game.getCurrentLevel().getCrates();
                            for (int i = 0; i < crates.size() && !wallClip; i++) {
                                if (crates.get(i) != player.getHeldCrate() && crateBox.intersects(crates.get(i).getHitbox())) {
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
                    if (!player.isReversing() && !player.isHoldLeft()) {
                        player.setHoldRight(true);
                        if (player.isHoldingCrate() && player.getDirection().equals("left")) {
                            Rectangle crateBox = new Rectangle(player.getHeldCrate().getHitbox());
                            int xMove = (int)(player.getHitbox().getWidth() + crateBox.getWidth());
                            crateBox.translate(xMove, 0);
                            Terrain[][] terrain = game.getCurrentLevel().getTerrain();
                            boolean wallClip = false;
                            for (int i = 0; i < terrain.length && !wallClip; i++) {
                                for (int j = 0; j < terrain[0].length && !wallClip; j++) {
                                    if (terrain[i][j] != null && crateBox.intersects(terrain[i][j].getHitbox())) {
                                        player.setDirection("right");
                                        wallClip = true;
                                    }
                                }
                            }
                            MyArrayList<Crate> crates = game.getCurrentLevel().getCrates();
                            for (int i = 0; i < crates.size() && !wallClip; i++) {
                                if (crates.get(i).getHitbox() != crateBox && crateBox.intersects(crates.get(i).getHitbox())) {
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
                MyArrayList<Crate> crates = game.getCurrentLevel().getCrates();
                boolean foundReverse = false;
                for (int i = 0; i < crates.size() && !foundReverse; i++) {
                    if (crates.get(i).getHitbox().contains(mousePos)) {
                        foundReverse = true;
                        player.setReversing(true);
                        crates.get(i).setReverse(true);
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
            MyArrayList<Crate> crates = game.getCurrentLevel().getCrates();
            boolean foundReverse = false;
            Terrain[][] terrain = game.getCurrentLevel().getTerrain();
            for (int i = 0; i < crates.size() && !foundReverse; i++) {
                Crate crate = crates.get(i);
                if (crate.isReverse()) {
                    foundReverse = true;
                    crate.setReverse(false);
                    boolean foundCollide = false;
                    if (crate.getHitbox().intersects(player.getHitbox())) {
                        foundCollide = true;
                        panel.setTransition(true);
                        game.setRestartLevel(true);
                    }
                    for (int j = 0; j < crates.size() && !foundCollide; j++) {
                        if (crate != crates.get(j) && crate.getHitbox().intersects(crates.get(j).getHitbox())) {
                            foundCollide = true;
                            panel.setTransition(true);
                            game.setRestartLevel(true);
                        }
                    }
                    for (int j = 0; j < terrain.length && !foundCollide; j++) {
                        for (int k = 0; k < terrain[0].length && !foundCollide; k++) {
                            if (terrain[j][k] instanceof Wall && crate.getHitbox().intersects(terrain[j][k].getHitbox())) {
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
                for (int j = 0; j < crates.size() && !foundCollide; j++) {
                    if (player.getHitbox().intersects(crates.get(j).getHitbox())) {
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
