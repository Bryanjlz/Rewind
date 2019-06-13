import java.awt.Rectangle;

/**
 * Terrain
 * An abstract class that represents terrain, used with polymorphism to store the different types of terrain.
 * @author Bryan Zhang
 * @since June 13/2019
 */
abstract class Terrain {
    private Rectangle hitbox;

    /**
     * Constructor to create a terrain with a blank hitbox.
     */
    Terrain () {
        this.hitbox = new Rectangle(0, 0, 0, 0);
    }

    /**
     * Constructor to create a terrain with specified coordinates, dimensions of one grid square.
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    Terrain (int x, int y) {
        this.hitbox = new Rectangle(x, y, MainFrame.gridScreenRatio, MainFrame.gridScreenRatio);
    }

    /**
     * Gets the hitbox of the wall.
     * @return The hitbox.
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * Sets the hitbox of the wall.
     * @param hitbox The hitbox.
     */
    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

}
