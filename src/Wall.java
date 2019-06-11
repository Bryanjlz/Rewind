import java.awt.*;

public class Wall extends Terrain {
    public Wall(int x, int y) {
        super(x, y, MainFrame.gridScreenRatio, MainFrame.gridScreenRatio);
    }

    @Override
    public Rectangle getHitbox() {
        return super.getHitbox();
    }

    @Override
    public void setHitbox(Rectangle hitBox) {
        super.setHitbox(hitBox);
    }

    public boolean collide(Rectangle pBox) {
        return pBox.intersects(getHitbox());
    }
}
