import javax.swing.*;
import java.awt.*;
public class GamePanel extends JPanel {
    private GameThread game;
    private Player player;
    private Level currentLevel;
    private boolean transition;
    private int transAlpha;
    public GamePanel (GameThread game) {
        super();
        this.game = game;
        this.player = game.getPlayer();
        this.currentLevel = game.getCurrentLevel();
        transAlpha = 0;
    }
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        //TODO: graphics
        g.drawRect((int)player.getHitbox().getX(), (int)player.getHitbox().getY(), (int)player.getHitbox().getWidth(), (int)player.getHitbox().getHeight());
        drawTerrain(g);
        g.drawString(Integer.toString(game.getFps()), 100, 100);

        if (player.isDead()) {
            transition = true;
        }
        if (transition) {
            if (player.isDead() && transAlpha < 255) {
                transAlpha += 1;
            } else {
                transAlpha -= 1;
                if (transAlpha == 0) {
                    transition = false;
                }
            }
            try {
                Thread.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            g.setColor(new Color(0,0, 0, transAlpha));
            g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        }
        this.repaint();
    }

    private void drawTerrain(Graphics g) {
        Terrain[][] terrain = currentLevel.getTerrain();
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if (terrain[i][j] instanceof Wall) {
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    //System.out.println(hitBox.getX() + " " + hitBox.getY());
                    g.drawRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
                } else if (terrain[i][j] instanceof Exit) {
                    Rectangle hitbox = terrain[i][j].getHitbox();
                    //System.out.println(hitBox.getX() + " " + hitBox.getY());
                    g.setColor(Color.BLUE);
                    g.fillRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
                    g.setColor(Color.BLACK);
                }
            }
        }
        g.setColor(Color.BLACK);
        for (int i = 0; i < currentLevel.getStones().size(); i++) {
            Rectangle hitbox = currentLevel.getStones().get(i).getHitbox();
            g.fillRect((int) hitbox.getX(), (int) hitbox.getY(), (int) hitbox.getWidth(), (int) hitbox.getHeight());
        }
    }
}
