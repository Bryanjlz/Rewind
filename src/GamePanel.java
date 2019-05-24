import javax.swing.*;
import java.awt.*;
public class GamePanel extends JPanel {
    private GameThread game;
    private Player player;
    private Level currentLevel;
    public GamePanel (GameThread game) {
        super();
        this.game = game;
        this.player = game.getPlayer();
        this.currentLevel = game.getCurrentLevel();
    }
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        //TODO: graphics

        g.drawRect((int)player.getHitBox().getX(), (int)player.getHitBox().getY(), (int)player.getHitBox().getWidth(), (int)player.getHitBox().getHeight());
        Terrain[][] terrain = currentLevel.getTerrain();
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if (terrain[i][j] != null) {
                    Rectangle hitBox = terrain[i][j].getHitBox();
                    //System.out.println(hitBox.getX() + " " + hitBox.getY());
                    g.drawRect((int) hitBox.getX(), (int) hitBox.getY(), (int) hitBox.getWidth(), (int) hitBox.getHeight());
                }
            }
        }
        this.repaint();
    }
}
