import java.awt.Rectangle;

public class Button extends Terrain {
    private boolean isPressed;
    private int id;
    Button (int x, int y, int id) {
        super (x, y);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean collide (Rectangle hitbox) {
        if (getHitbox().intersects(hitbox)) {
            setPressed(true);
            return true;
        }
        return false;
    }
}
