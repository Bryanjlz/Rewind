import java.awt.Rectangle;

/**
 * Key
 * Class that represents a key.
 * @author Bryan Zhang
 */
public class Key extends Terrain {
    private int id;
    private boolean pickedUp;

    /**
     * Constructor for a key.
     * @param x The x coordinate of the key.
     * @param y The y coordinate of the key.
     * @param id The ID of the key, which matches a door.
     */
    public Key(int x, int y, int id) {
        super(x, y);
        this.id = id;
        pickedUp = false;
    }

    /**
     * Copy constructor for a key that creates a deep copy of the key.
     * @param key The key to copy.
     */
    public Key (Key key) {
        super ((int)key.getHitbox().getX(), (int)key.getHitbox().getY());
        id = key.getId();
        pickedUp = key.isPickedUp();
    }

    /**
     * Gets the ID of the key.
     * @return The int ID of the key.
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if the key has been picked up by the player.
     * @return A boolean that represents if the key has been picked up.
     */
    public boolean isPickedUp() {
        return pickedUp;
    }

    /**
     * Sets the boolean that represents if it has been picked up by the player.
     * @param pickedUp A boolean that represents if it has been picked up by the player.
     */
    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    /**
     * Checks if the key is colliding with a specified box.
     * @param hitbox The hitbox to check collision with.
     * @return A boolean that represents if the key is touching the specified hitbox.
     */
    public boolean collide(Rectangle hitbox) {
        return hitbox.intersects(getHitbox());
    }
}
