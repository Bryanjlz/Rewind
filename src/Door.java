import java.awt.Rectangle;

/**
 * Door
 * Class that represents a door.
 * @author Bryan Zhang
 */
public class Door extends Wall {
    private int id;
    private boolean unlocked;
    public Door(int x, int y, int id) {
        super(x, y);
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
        return pBox.intersects(getHitbox());
    }
}

