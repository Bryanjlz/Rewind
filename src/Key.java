import java.awt.*;

public class Key extends Terrain {
    private int id;
    private boolean pickedUp;
    public Key(int x, int y, int id) {
        super(x, y, MainFrame.gridScreenRatio, MainFrame.gridScreenRatio);
        this.id = id;
        pickedUp = false;
    }

    public Key (Key key) {
        super ((int)key.getHitbox().getX(), (int)key.getHitbox().getY());
        id = key.getId();
        pickedUp = key.isPickedUp();
    }

    public void clone (Key key) {
        setHitbox(new Rectangle(key.getHitbox()));
        id = key.getId();
        pickedUp = key.isPickedUp();
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

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    public boolean collide(Rectangle pBox) {
        if (pBox.intersects(getHitbox())) {
            return true;
        }
        return false;
    }
}
