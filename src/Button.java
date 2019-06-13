import java.awt.Rectangle;

/**
 * Button
 * Class that represents a button
 * @author Bryan Zhang
 */
public class Button extends Terrain {
    private boolean isPressed;
    private int id;

    /**
     * Constructor to create a button at specified coordinates and id.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param id The ID.
     */
    Button (int x, int y, int id) {
        super (x, y);
        this.id = id;
    }

    /**
     * Gets the ID of the button (used to associate buttons with moving walls.
     * @return The ID of the button.
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if the button is pressed.
     * @return A boolean that represents if the button is pressed.
     */
    public boolean isPressed() {
        return isPressed;
    }

    /**
     * Sets the boolean that represents if the button is pressed.
     * @param pressed A boolean that represents if the button is pressed.
     */
    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    /**
     * Checks if anything is pressing the button
     * @param hitbox The hitbox.
     * @return A boolean that represents if anything is touching the button.
     */
    public boolean collide (Rectangle hitbox) {
        if (getHitbox().intersects(hitbox)) {
            setPressed(true);
            return true;
        }
        return false;
    }
}
