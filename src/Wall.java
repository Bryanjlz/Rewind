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
     * Checks collision with a specified hitbox.
     * @param hitbox The hitbox to check collision with.
     * @return A boolean that represents if the two are touching.
     */
    public boolean collide(Rectangle hitbox) {
        return hitbox.intersects(getHitbox());
    }
}
