import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GamePanel extends JPanel {
    private GameThread game;
    private Thread thread;
    private Player player;
    private Level currentLevel;
    private boolean transition;
    private int transAlpha;
    private boolean passedBlack;
    static final int NOT_HOLD_CRATE = 0;
    static final int HOLD_CRATE = 1;
    static final int RIGHT = 0;
    static final int LEFT = 1;
    static final int FACE_FORWARD = 2;
    static final int LOCKED = 0;
    static final int UNLOCKED = 1;
    private BufferedImage[][][] run;
    private BufferedImage[][] stand;
    private BufferedImage[][] slide;
    private BufferedImage[][] jump;
    private BufferedImage wall;
    private BufferedImage key;
    private BufferedImage crate;
    private BufferedImage[] door;

    public GamePanel (GameThread game) {
        super();
        this.game = game;
        this.player = game.getPlayer();
        this.currentLevel = game.getCurrentLevel();
        transAlpha = 0;
        passedBlack = false;
        run = new BufferedImage[2][2][16];
        stand = new BufferedImage[3][8];
        slide = new BufferedImage[2][2];
        jump = new BufferedImage[2][2];
        door = new BufferedImage[2];
        loadImages();
    }
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setTransition(boolean transition) {
        this.transition = transition;
    }

    private void loadImages() {
        // Load player run images
        for (int i = 0; i < run.length; i++) {
            for (int j = 0; j < run[0].length; j++) {
                for (int k = 0; k < run[0][0].length; k++) {
                    int playerNum = i * run[0].length * run[0][0].length + j * run[0][0].length + k + 1;
                    File file = new File("assets/images/player/run/player" + playerNum + ".png");
                    //System.out.println("assets/images/player/run/player" + playerNum + ".png");
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

        // Load terrain block
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

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        //TODO: graphics

        if (game.isMenu()) {
            drawMenu(g);
        } else {
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
            drawPlayer(g);
            //g.setColor(Color.CYAN);
            //g.fillRect((int) player.getHitbox().getX(), (int) player.getHitbox().getY(), (int) player.getHitbox().getWidth(), (int) player.getHitbox().getHeight());
            if (player.isReverse()) {
                g.setColor(Color.BLACK);
                g.drawString("Rewiinndd", (int) (player.getHitbox().getX() + player.getHitbox().getWidth() / 4), (int) (player.getHitbox().getY() + player.getHitbox().getHeight() / 2));
            }

            drawTerrain(g);
            g.setColor(Color.black);
            g.drawString(Integer.toString(game.getFps()), 100, 100);

            if (player.isDead() || currentLevel.isLevelFinished()) {
                transition = true;
            }
            /*
            if (currentLevel.isLevelFinished()) {
                g.setFont(new Font("Arial", Font.PLAIN, 200));
                g.setColor(Color.BLACK);
                g.drawString("YOU WIINN!!!", MainFrame.WIDTH / 5 + 25, MainFrame.HEIGHT / 2);
                for (int i = 0; i < game.pews.size(); i++) {
                    g.setColor(game.pews.get(i).colour);
                    g.setFont(game.pews.get(i).font);
                    g.drawString(Pew.pew, game.pews.get(i).x, game.pews.get(i).y);
                }
            }
            */
            g.setFont(new Font("Arial", Font.PLAIN, 12));
        }
        if (transition) {
            if (transAlpha < 255 && !passedBlack) {
                transAlpha += 15;
            } else {
                if (transAlpha == 255) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                passedBlack = true;
                transAlpha -= 15;
                if (transAlpha == 0) {
                    transition = false;
                    passedBlack = false;
                }
            }
            try {
                Thread.sleep(27);
            } catch (Exception e) {
                e.printStackTrace();
            }
            g.setColor(new Color(0, 0, 0, transAlpha));
            g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        }
        this.repaint();
    }

    private void drawPlayer (Graphics g) {
        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;
        Image img = null;
        int direction = RIGHT;
        if (player.getDirection() == "left") {
            direction = LEFT;
        }
        int holdCrate = NOT_HOLD_CRATE;
        if (player.isHoldingCrate()) {
            holdCrate = HOLD_CRATE;
        }
        if (!player.isOnGround()) {
            w = MainFrame.gridScreenRatio;
            h = MainFrame.gridScreenRatio;
            y = (int)player.getHitbox().getY();
            if (player.isHoldingCrate()) {
                w += (int)(Player.SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
            }
            x = (int)(player.getHitbox().getX() + player.getHitbox().getWidth() / 2 - w / 2);
            img = jump[holdCrate][direction];

        } else if (player.isHoldLeft() || player.isHoldRight()) {
            h = MainFrame.gridScreenRatio;
            y = (int) player.getHitbox().getY();
            w = (int)(Player.SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
            if (player.isHoldingCrate()) {
                w += MainFrame.gridScreenRatio;
            }
            x = (int)(player.getHitbox().getX() + player.getHitbox().getWidth() / 2 - w / 2);
            if (player.getFrame() >= run[0][0].length) {
                player.setFrame(0);
            }
            img = run[holdCrate][direction][player.getFrame()];
        } else if (player.getVel().getX() == 0) {
            w = MainFrame.gridScreenRatio;
            h = MainFrame.gridScreenRatio;
            y = (int)player.getHitbox().getY();
            if (!player.isHoldingCrate()) {
                direction = FACE_FORWARD;
                x = (int)(player.getHitbox().getX() + player.getHitbox().getWidth() / 2 - w / 2);
            } else {
                w += (int)(Player.SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
                x = (int)(player.getHitbox().getX() + player.getHitbox().getWidth() / 2 - w / 2);
            }
            if (player.getFrame() >= stand[0].length) {
                player.setFrame(0);
            }
            img = stand[direction][player.getFrame()];
        } else {
            w = MainFrame.gridScreenRatio;
            h = MainFrame.gridScreenRatio;
            y = (int)player.getHitbox().getY();
            if (!player.isHoldingCrate()) {
                h /= 2;
                y += h;
                x = (int)(player.getHitbox().getX() + player.getHitbox().getWidth() / 2 - w / 2);
            } else {
                w += (int)(Player.SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
                x = (int)(player.getHitbox().getX() + player.getHitbox().getWidth() / 2 - w / 2);
            }
            img = slide[holdCrate][direction];
        }
        g.drawImage(img, x, y, w, h, null);
    }

    private void drawMenu (Graphics g) {
        g.setColor(Color.white);
        g.drawRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        g.setFont(new Font("Arial", Font.PLAIN, 130));
        g.setColor(Color.black);
        g.drawString("iT's ReWInd tIMe!!1!", 100, 350);
        g.drawRect((int)game.getPlayHitbox().getX(), (int)game.getPlayHitbox().getY(), (int)game.getPlayHitbox().getWidth(), (int)game.getPlayHitbox().getHeight());
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.drawString("PlAy", (int)game.getPlayHitbox().getX() + 75, (int)game.getPlayHitbox().getY() + (int)(game.getPlayHitbox().getHeight() / 1.5));
    }

    private void drawTerrain(Graphics g) {
        Terrain[][] terrain = currentLevel.getTerrain();
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {

                if (terrain[i][j] instanceof Exit) {
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    //System.out.println(hitBox.getX() + " " + hitBox.getY());
                    g.setColor(Color.BLACK);
                    g.fillRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
                    g.setColor(Color.BLACK);
                } else if (terrain[i][j] instanceof Door) {
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    g.drawImage(door[0], (int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight(), null);
                } else if (terrain[i][j] instanceof Wall) {
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    g.drawImage(wall, (int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight(), null);
                }
            }
        }
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < currentLevel.getCrates().size(); i++) {
            Rectangle hitbox = currentLevel.getCrates().get(i).getHitbox();
            if (currentLevel.getCrates().get(i) != player.getHeldCrate()) {
                g.drawImage(crate, (int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight(), null);
            }
            if (currentLevel.getCrates().get(i).isReverse()) {
                g.setColor(Color.BLACK);
                g.drawString ("Rewiinndd",(int)(hitbox.getX()+ hitbox.getWidth() / 4), (int)(hitbox.getY() + hitbox.getHeight() / 2));
            }
        }

        for (int i = 0; i < player.getKeys().size(); i++) {
            if (!player.getKeys().get(i).isPickedUp()) {
                Rectangle hitbox = player.getKeys().get(i).getHitbox();
                g.setColor(Color.YELLOW);
                g.fillRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
            }
        }
    }
}
