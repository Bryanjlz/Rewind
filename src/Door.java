import java.awt.Rectangle;

/**
 * Door
 * Class that represents a door.
 * @author Bryan Zhang
 * @since June 13/2019
 */
public class Door extends Wall {
    private int id;
    private boolean unlocked;

    /**
     * Constructor for a door with specified position and id.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param id The ID that matches a key.
     */
    public Door(int x, int y, int id) {
        super(x, y);
        this.id = id;
    }

    /**
     * Gets the ID.
     * @return The int ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if the door is unlocked or not.
     * @return A boolean that represents if the door is unlocked or not.
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Sets the boolean that represents if the door is unlocked or not.
     * @param unlocked A boolean that represents if the door is unlocked or not.
     */
    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    /**
     * Checks the collision between the door and a specified hitbox.
     * @param hitbox The hitbox to check collision with.
     * @return A boolean that represents if the two are touching.
     */
    public boolean collide(Rectangle hitbox) {
        return hitbox.intersects(getHitbox());
    }
}

