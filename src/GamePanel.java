import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.io.File;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * GamePanel
 * The JPanel for the game to display graphics.
 */
public class GamePanel extends JPanel {
    private GameThread game;
    private Player player;
    private Level currentLevel;
    private int menuFrame = 0;
    private boolean transition;
    private double timeAlpha;
    private int transAlpha;
    private boolean passedBlack;
    private static final int ALPHA_CHANGE = 15;
    private static final Color PURPLE = new Color(173, 102, 249);
    private static final Color BLACK = Color.BLACK;
    private static final int NOT_HOLD_CRATE = 0;
    private static final int HOLD_CRATE = 1;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int FACE_FORWARD = 2;
    private static final int LOCKED = 0;
    private static final int UNLOCKED = 1;
    private BufferedImage[][][] run;
    private BufferedImage[][] stand;
    private BufferedImage[][] slide;
    private BufferedImage[][] jump;
    private BufferedImage wall;
    private BufferedImage key;
    private BufferedImage crate;
    private BufferedImage[] door;
    private BufferedImage[] menu;

    /**
     * Creates the panel.
     * @param game Used to be able to reference the info about the current level, player, etc.
     */
    public GamePanel (GameThread game) {
        super();
        this.game = game;
        this.player = game.getPlayer();
        this.currentLevel = game.getCurrentLevel();
        transAlpha = 0;
        passedBlack = false;
        run = new BufferedImage[2][2][18];
        stand = new BufferedImage[3][8];
        slide = new BufferedImage[2][2];
        jump = new BufferedImage[2][2];
        door = new BufferedImage[2];
        menu = new BufferedImage[18];
        loadImages();
    }

    /**
     * Sets the transition boolean.
     * @param transition A boolean that represents if the Panel should run transition.
     */
    public void setTransition(boolean transition) {
        this.transition = transition;
    }

    /**
     * Loads the images that are required for the game.
     */
    private void loadImages() {
        // Load menu images
        for (int i = 0; i < menu.length; i++) {
            File file = new File ("assets/images/menu/menu" + (i + 1) + ".png");
            try {
                menu[i] = ImageIO.read(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Load player run images
        for (int i = 0; i < run.length; i++) {
            for (int j = 0; j < run[0].length; j++) {
                for (int k = 0; k < run[0][0].length; k++) {
                    int playerNum = i * run[0].length * run[0][0].length + j * run[0][0].length + k + 1;
                    File file = new File("assets/images/player/run/player" + playerNum + ".png");
                    try {
                        run[i][j][k] = ImageIO.read(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Load player stand images
        for (int i = 0; i < stand.length; i++) {
            for (int j = 0; j < stand[0].length; j++) {
                int playerNum = i * stand[0].length + j + 1;
                File file = new File("assets/images/player/stand/player" + playerNum + ".png");
                try {
                    stand[i][j] = ImageIO.read(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Load player slide images
        for (int i = 0; i < slide.length; i++) {
            for (int j = 0; j < slide[0].length; j++) {
                int playerNum = i * slide[0].length + j + 1;
                File file = new File ("assets/images/player/slide/player" + playerNum + ".png");
                try {
                    slide[i][j] = ImageIO.read(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Load player jump images
        for (int i = 0; i < jump.length; i++) {
            for (int j = 0; j < jump[0].length; j++) {
                int playerNum = i * jump[0].length + j + 1;
                File file = new File ("assets/images/player/jump/player" + playerNum + ".png");
                try {
                    jump[i][j] = ImageIO.read(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Load terrain blocks
        try {
            key = ImageIO.read(new File("assets/images/terrain/key.png"));
            wall = ImageIO.read(new File("assets/images/terrain/wall.png"));
            crate = ImageIO.read(new File("assets/images/terrain/crate.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < door.length; i++) {
            try {
                door[i] = ImageIO.read(new File("assets/images/terrain/door" + (i + 1) + ".png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to draw graphics on the JPanel.
     * @param g Used to draw graphics.
     */
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        // Draw menu
        if (game.isMenu()) {
            drawMenu(g);
        } else {

            // Draw Background
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);

            drawPlayer(g);
            drawTerrain(g);

            // Draw FPS
            g.setColor(Color.black);
            g.drawString(Integer.toString(game.getFps()), 100, 100);

            // If level is finished or the player died, set transition to true
            if (player.isDead() || currentLevel.isLevelFinished()) {
                transition = true;
            }

            // Time rewind overlay
            drawTimeOverlay(g);
        }

        // Run transition
        if (transition) {
            drawTransition (g);
        }

        // Repaint
        this.repaint();
    }

    /**
     * Draws purple overlay when player is reversing time.
     * @param g Used to draw overlay.
     */
    private void drawTimeOverlay (Graphics g) {
        if (timeAlpha < 75 && player.isReversing()) {
            timeAlpha += 0.2;
        } else if (timeAlpha > 0 && !player.isReversing()) {
            timeAlpha -= 0.2;
        }

        if (timeAlpha != 0) {
            g.setColor(new Color (PURPLE.getRed(), PURPLE.getGreen(), PURPLE.getBlue(), (int)timeAlpha));
            g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        }
    }

    /**
     * Draw the transition
     * @param g Used to draw graphics.
     */
    private void drawTransition (Graphics g) {
        // Fade to black
        if (transAlpha < 255 && !passedBlack) {
            transAlpha += ALPHA_CHANGE;
        } else {

            // Wait one second if screen is black
            if (transAlpha == 255) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Fade to not black
            passedBlack = true;
            transAlpha -= ALPHA_CHANGE;

            // Transition is finished
            if (transAlpha == 0) {
                transition = false;
                passedBlack = false;
            }
        }

        // Small delay while fading
        try {
            Thread.sleep(27);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Draw the black overlay
        if (transAlpha != 0) {
            g.setColor(new Color(BLACK.getRed(), BLACK.getGreen(), BLACK.getBlue(), transAlpha));
            g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        }
    }

    /**
     * Draws the player.
     * @param g Used to draw the player.
     */
    private void drawPlayer (Graphics g) {
        int x;
        int y;
        int w;
        int h;
        Image img;

        // Find direction player is facing
        int direction = RIGHT;
        if (player.getDirection().equals("left")) {
            direction = LEFT;
        }

        // Checks if the player is holding a crate or not
        int holdCrate = NOT_HOLD_CRATE;
        if (player.isHoldingCrate()) {
            holdCrate = HOLD_CRATE;
        }

        // Jumping player
        if (!player.isOnGround()) {
            w = (int)(Player.SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
            h = MainFrame.gridScreenRatio;
            y = (int)player.getHitbox().getY();
            x = (int)player.getHitbox().getX();

            // Adjust width of the image if player is holding a crate
            if (player.isHoldingCrate()) {
                w += MainFrame.gridScreenRatio;

                // Adjust x position of image if player is facing left
                if (direction == LEFT) {
                    x -= MainFrame.gridScreenRatio;
                }
            }

            // Get the image
            img = jump[holdCrate][direction];

        // If the player is holding left or right
        } else if (player.isHoldLeft() || player.isHoldRight()) {
            h = MainFrame.gridScreenRatio;
            y = (int) player.getHitbox().getY();
            w = (int)(Player.SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
            x = (int)player.getHitbox().getX();

            // Adjust width if player is holding crate
            if (player.isHoldingCrate()) {
                w += MainFrame.gridScreenRatio;

                // Adjust x position if player is facing left
                if (direction == LEFT) {
                    x -= MainFrame.gridScreenRatio;
                }
            }

            // Set frame number to 0 if it's past the past frame of run animation
            if (player.getFrame() >= run[0][0].length - 1) {
                player.setFrame(0);
            }

            // Gets the iamge
            img = run[holdCrate][direction][player.getFrame()];

        // If the player is sliding
        }  else if ((player.getAcc().getX() > 0 && player.getVel().getX() < 0) || (player.getAcc().getX() < 0 && player.getVel().getX() > 0)){
            w = MainFrame.gridScreenRatio;
            h = MainFrame.gridScreenRatio;
            y = (int)player.getHitbox().getY();
            x = 0;

            // If player is holding crate, adjust the width
            if (player.isHoldingCrate()) {
                w += (int)(Player.SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);

                // Adjust x position if player is facing left
                if (direction == LEFT) {
                    x -= MainFrame.gridScreenRatio;
                }

            // Adjust y position and height if player isn't holding crate
            } else {
                h /= 2;
                y += h;
            }
            x += (int)(player.getHitbox().getX());

            // Get image
            img = slide[holdCrate][direction];

        // If player is standing stll
        }else {
            w = MainFrame.gridScreenRatio;
            h = MainFrame.gridScreenRatio;
            y = (int)player.getHitbox().getY();
            x = (int)player.getHitbox().getX();

            // Adjust the width if player is holding crate
            if (player.isHoldingCrate()) {
                w += (int)(Player.SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);

                // Adjust x position if player is facing left
                if (direction == LEFT) {
                    x -= MainFrame.gridScreenRatio;
                }
            } else {

                // Change direction if player isn't holding a crate
                direction = FACE_FORWARD;

            }

            // Set frame number to 0 of it's greater than the last frame
            if (player.getFrame() >= (stand[0].length * 2) - 1) {
                player.setFrame(0);
            }

            // Gets the image
            img = stand[direction][player.getFrame() / 2];
        }

        // Draw the player image on the screen
        g.drawImage(img, x, y, w, h, null);
    }

    /**
     * Draws the menu.
     * @param g Used to draw.
     */
    private void drawMenu (Graphics g) {
        if (menuFrame > menu.length- 1) {
            menuFrame = 0;
        }
        try {
            Thread.sleep(30);
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(menu[menuFrame], 0, 0, MainFrame.WIDTH, MainFrame.HEIGHT, null);
        menuFrame++;
    }

    /**
     * Draw the terrain.
     * @param g Used to help draw the terrain.
     */
    private void drawTerrain(Graphics g) {
        // Gets the terrain from the current level
        Terrain[][] terrain = currentLevel.getTerrain();

        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                // Draw exit
                if (terrain[i][j] instanceof Exit) {
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    g.setColor(Color.BLACK);
                    g.fillRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
                    g.setColor(Color.BLACK);

                // Draw door
                } else if (terrain[i][j] instanceof Door) {
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    Image img = door[0];
                    if (((Door)terrain[i][j]).isUnlocked()) {
                        img = door[1];
                    }
                    g.drawImage(img, (int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight(), null);

                    // Draw wall
                } else if (terrain[i][j] instanceof Wall) {
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    g.drawImage(wall, (int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight(), null);
                }
            }
        }

        // Draw crates
        for (int i = 0; i < currentLevel.getCrates().size(); i++) {
            Rectangle hitbox = currentLevel.getCrates().get(i).getHitbox();
            if (currentLevel.getCrates().get(i) != player.getHeldCrate()) {
                g.drawImage(crate, (int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight(), null);
            }
        }

        // Draw keys
        for (int i = 0; i < player.getKeys().size(); i++) {
            if (!player.getKeys().get(i).isPickedUp()) {
                Rectangle hitbox = player.getKeys().get(i).getHitbox();
                g.drawImage(key, (int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight(), null);
            }
        }
    }
}
