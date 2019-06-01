import java.awt.*;

public class Door extends Terrain {
    private int id;
    private boolean unlocked;
    public Door(int x, int y, int id) {
        super(x, y, MainFrame.gridScreenRatio, MainFrame.gridScreenRatio);
        this.id = id;
    }

    @Override
    public Rectangle getHitbox() {
        return super.getHitbox();
    }

    @Override
    public void setHitbox(Rectangle hitBox) {
        super.setHitbox(hitBox);
    }

    public int getId() {
        return id;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean collide(Rectangle pBox) {
        if (pBox.intersects(getHitbox())) {
            return false;
        }
        return true;
    }
}

