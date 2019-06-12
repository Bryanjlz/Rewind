import java.awt.Rectangle;

/**
 * Wall
 * A class that represents a wall.
 * @author Bryan Zhang
 */
public class Wall extends Terrain {

    /**
     * Constructor for a wall with specified position.
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public Wall(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the hitbox of the wall.
     * @return The hitbox.
     */
    @Override
    public Rectangle getHitbox() {
        return super.getHitbox();
    }

    /**
     * Sets the hitbox of the wall.
     * @param hitBox The hitbox.
     */
    @Override
    public void setHitbox(Rectangle hitBox) {
        super.setHitbox(hitBox);
    }

    /**
     * Checks collision with a specified hitbox.
     * @param hitbox The hitbox to check collision with.
     * @return A boolean that represents if the two are touching.
     */
    public boolean collide(Rectangle hitbox) {
        return hitbox.intersects(getHitbox());
    }
}
