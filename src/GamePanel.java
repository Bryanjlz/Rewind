import javax.swing.*;
import java.awt.*;
public class GamePanel extends JPanel {
    private GameThread game;
    private Thread thread;
    private Player player;
    private Level currentLevel;
    private boolean transition;
    private int transAlpha;
    private boolean passedBlack;

    public GamePanel (GameThread game) {
        super();
        this.game = game;
        this.player = game.getPlayer();
        this.currentLevel = game.getCurrentLevel();
        transAlpha = 0;
        passedBlack = false;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean isTransition() {
        return transition;
    }

    public void setTransition(boolean transition) {
        this.transition = transition;
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        //TODO: graphics

        if (game.isMenu()) {
            drawMenu(g);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect((int) player.getHitbox().getX(), (int) player.getHitbox().getY(), (int) player.getHitbox().getWidth(), (int) player.getHitbox().getHeight());
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
                transAlpha += 1;
            } else {
                passedBlack = true;
                transAlpha -= 1;
                if (transAlpha == 0) {
                    transition = false;
                    passedBlack = false;
                }
            }
            try {
                Thread.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            g.setColor(new Color(0, 0, 0, transAlpha));
            g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        }
        this.repaint();
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
                if (terrain[i][j] instanceof Wall) {
                    g.setColor(Color.BLACK);
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    //System.out.println(hitBox.getX() + " " + hitBox.getY());
                    g.drawRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
                } else if (terrain[i][j] instanceof Exit) {
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    //System.out.println(hitBox.getX() + " " + hitBox.getY());
                    g.setColor(Color.BLACK);
                    g.fillRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
                    g.setColor(Color.BLACK);
                }
            }
        }
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < currentLevel.getStones().size(); i++) {
            Rectangle hitbox = currentLevel.getStones().get(i).getHitbox();
            g.fillRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
            if (currentLevel.getStones().get(i).isReverse()) {
                g.setColor(Color.BLACK);
                g.drawString ("Rewiinndd",(int)(hitbox.getX()+ hitbox.getWidth() / 4), (int)(hitbox.getY() + hitbox.getHeight() / 2));
            }
        }
    }
}
