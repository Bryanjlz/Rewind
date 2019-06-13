import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JFrame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * MainFrame
 * Main JFrame of the game
 * @author Bryan Zhang
 */
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

        // Load and play background music in a loop
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

    /**
     * MyKeyListener
     * Inner class key listener that detects when keys are pressed
     * @author Bryan Zhang
     */
    private class MyKeyListener implements KeyListener {

        /**
         * Detects when keys are typed.
         * @param e Used to see what key has been typed.
         */
        @Override
        public void keyTyped(KeyEvent e) {
        }

        /**
         * Detects when keys are pressed.
         * @param e Used to see what key has been pressed.
         */
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {

                // Jump
                case KeyEvent.VK_W:
                    // Allow key pressed if player is not reversing
                    if (!player.isReversing() && player.isOnGround()) {
                        player.setJumpStartTime(System.nanoTime() / 1000000000.0);
                        player.setHoldUp(true);
                    }
                    break;

                // Move left
                case KeyEvent.VK_A:
                    // Allow key pressed if player is not reversing and not holding right
                    if (!(player.isReversing()) && !(player.isHoldRight())) {
                        player.setHoldLeft(true);

                        // if the player is holding crate and is looking right
                        if ((player.isHoldingCrate()) && (player.getDirection().equals("right"))) {

                            // Player try to turn around and check if turning around results in clipping into a wall
                            Rectangle crateBox = new Rectangle(player.getHeldCrate().getHitbox());
                            int xMove = (int)-(crateBox.getWidth() + player.getHitbox().getWidth());
                            crateBox.translate(xMove, 0);

                            // Check collision with walls
                            Terrain[][] terrain = game.getCurrentLevel().getTerrain();
                            boolean wallClip = false;
                            for (int i = 0; i < terrain.length && !wallClip; i++) {
                                for (int j = 0; j < terrain[0].length && !wallClip; j++) {
                                    if (terrain[i][j] instanceof Wall && crateBox.intersects(terrain[i][j].getHitbox())) {
                                        wallClip = true;
                                    }
                                }
                            }

                            // Check collision with crates
                            MyArrayList<Crate> crates = game.getCurrentLevel().getCrates();
                            for (int i = 0; i < crates.size() && !wallClip; i++) {
                                if ((crates.get(i).getHitbox() != crateBox) && (crateBox.intersects(crates.get(i).getHitbox()))) {
                                    wallClip = true;
                                }
                            }

                            // If it's not clipping into wall, then set player direction to left
                            if (!wallClip) {
                                player.setDirection("left");
                            }

                        // If player isn't holding a crate or is already facing left, set player direction to left
                        } else {
                            player.setDirection("left");
                        }
                    }
                    break;

                // Move right
                case KeyEvent.VK_D:
                    // Allow key pressed if player is not reversing and not holding left
                    if (!(player.isReversing()) && !(player.isHoldLeft())) {
                        player.setHoldRight(true);

                        // If player is holding a crate and looking left
                        if ((player.isHoldingCrate()) && (player.getDirection().equals("left"))) {

                            // Try to make player turn around
                            Rectangle crateBox = new Rectangle(player.getHeldCrate().getHitbox());
                            int xMove = (int)(player.getHitbox().getWidth() + crateBox.getWidth());
                            crateBox.translate(xMove, 0);

                            // Check if crate is now clipping into wall
                            Terrain[][] terrain = game.getCurrentLevel().getTerrain();
                            boolean wallClip = false;
                            for (int i = 0; i < terrain.length && !wallClip; i++) {
                                for (int j = 0; j < terrain[0].length && !wallClip; j++) {
                                    if (terrain[i][j] instanceof Wall && crateBox.intersects(terrain[i][j].getHitbox())) {
                                        wallClip = true;
                                    }
                                }
                            }

                            // Check crate collision with other crates
                            MyArrayList<Crate> crates = game.getCurrentLevel().getCrates();
                            for (int i = 0; i < crates.size() && !wallClip; i++) {
                                if ((crates.get(i).getHitbox() != crateBox) && (crateBox.intersects(crates.get(i).getHitbox()))) {
                                    wallClip = true;
                                }
                            }

                            // If it's not clipping into wall, then set player direction to right
                            if (!wallClip) {
                                player.setDirection("right");
                            }

                        // If the player isn't holding a crate or is already looking right, then set direction to right
                        } else {
                            player.setDirection("right");
                        }
                    }
                    break;
            }
        }

        /**
         * Detects when keys are released.
         * @param e Used to detect when keys are released.
         */
        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                // Release jump
                case KeyEvent.VK_W:
                    player.setHoldUp(false);
                    break;

                // Release left
                case KeyEvent.VK_A:
                    player.setHoldLeft(false);
                    break;

                // Release right
                case KeyEvent.VK_D:
                    player.setHoldRight(false);
                    break;

                // Restart level
                case KeyEvent.VK_R:
                    if (!game.isMenu()) {
                        panel.setTransition(true);
                        game.setRestartLevel(true);
                    }

                // Interact with objects in front of player
                case KeyEvent.VK_SPACE:
                    player.interact();
                    break;

                // Exit game
                case KeyEvent.VK_ESCAPE:
                    game.setRunning(false);
                    System.exit(0);
            }

        }
    }

    /**
     * MyMouseListener
     * Used to detect when player clicks and where.
     * @author Bryan Zhang
     */
    private class MyMouseListener implements MouseListener {

        /**
         * Detects when the mouse clicks.
         * @param e Used to detect mouse clicks.
         */
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        /**
         * Detects when the mouse enters screen.
         * @param e Used to detect mouse entering.
         */
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        /**
         * Detects when mouse exits screen.
         * @param e Used to detect mouse exiting.
         */
        @Override
        public void mouseExited(MouseEvent e) {

        }

        /**
         * Detects when the mouse presses down.
         * @param e Used to detect the mouse pressing down.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            // Get position of mouse
            Point mousePos = MouseInfo.getPointerInfo().getLocation();

            if (!game.isMenu()) {
                // Check if mouse is clicking any crates
                MyArrayList<Crate> crates = game.getCurrentLevel().getCrates();
                boolean foundReverse = false;
                for (int i = 0; i < crates.size() && !foundReverse; i++) {

                    // If mouse is clicking a crate, reverse the crate back in time
                    if (crates.get(i).getHitbox().contains(mousePos)) {
                        foundReverse = true;
                        player.setReversing(true);
                        crates.get(i).setReverse(true);
                    }
                }

                // Check if mouse is clicking player, and if so, reverse player back in time
                if (!foundReverse && player.getHitbox().contains(mousePos)) {
                    player.setReverse(true);
                    player.setReversing(true);
                }
            }
        }

        /**
         * Detects when the mouse is released.
         * @param e Used to detect when the mouse is released.
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            // Gets the position of the mouse
            Point mousePos = MouseInfo.getPointerInfo().getLocation();

            // If menu, check if player is clicking play
            if (game.isMenu()) {
                if (game.getPlayHitbox().contains(mousePos)) {
                    game.setMenu(false);

                }
            } else {
                // Stop player reversing
                player.setReversing(false);

                // Find what was being reversed, look through crates
                MyArrayList<Crate> crates = game.getCurrentLevel().getCrates();
                boolean foundReverse = false;
                Terrain[][] terrain = game.getCurrentLevel().getTerrain();
                for (int i = 0; i < crates.size() && !foundReverse; i++) {
                    Crate crate = crates.get(i);

                    // If found crate being reversed, set to be not reversed
                    if (crate.isReverse()) {
                        foundReverse = true;
                        crate.setReverse(false);
                        boolean foundCollide = false;

                        // If crate is clipping into any wall or player, restart level
                        // Check if crate clipping into player
                        if (crate.getHitbox().intersects(player.getHitbox())) {
                            foundCollide = true;
                            panel.setTransition(true);
                            game.setRestartLevel(true);
                        }

                        // Check if crate is clipping into other crates
                        for (int j = 0; j < crates.size() && !foundCollide; j++) {
                            if ((crate != crates.get(j)) && (crate.getHitbox().intersects(crates.get(j).getHitbox()))) {
                                foundCollide = true;
                                panel.setTransition(true);
                                game.setRestartLevel(true);
                            }
                        }

                        // Check if crate is clipping into wall
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

                // If player is being reversed
                if (!foundReverse && player.isReverse()) {

                    // Set player to not be reversed
                    player.setHoldLeft(false);
                    player.setHoldRight(false);
                    player.setReverse(false);
                    boolean foundCollide = false;

                    // Restart if player is clipping into crates
                    for (int j = 0; j < crates.size() && !foundCollide; j++) {
                        if (player.getHitbox().intersects(crates.get(j).getHitbox())) {
                            foundCollide = true;
                            panel.setTransition(true);
                            game.setRestartLevel(true);
                        }
                    }

                    // Restart if player is clipping into terrain
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
}
