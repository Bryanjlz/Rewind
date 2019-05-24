import javax.swing.*;
import java.awt.*;
public class GamePanel extends JPanel {
    private GameThread game;
    private Player player;
    public GamePanel (GameThread game) {
        super();
        this.game = game;
        this.player = game.getPlayer();
    }
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        //TODO: graphics

        g.drawRect((int)player.getHitBox().getX(), (int)player.getHitBox().getY(), (int)player.getHitBox().getWidth(), (int)player.getHitBox().getHeight());
        this.repaint();
    }
}
