import java.awt.Rectangle;

/**
 * Exit
 * Class that represents a door.
 * @author Bryan Zhang
 */
public class Exit extends Terrain {
  private Level currentLevel;

  /**
   * Constructor for an exit at a specified position.
   * @param x X coordinate.
   * @param y Y coordinate.
   */
  public Exit (int x, int y) {
    super (x, y);
  }

  /**
   * Checks collision a specified hitbox, which is the player.
   * @param pBox The player hitbox.
   */
  public void collide(Rectangle pBox) {
    if (pBox.intersects(getHitbox())) {
      currentLevel.setLevelFinished(true);
    }
  }

  /**
   * Sets the reference this has to the current level, used to change level finished boolean.
   * @param currentLevel Reference to current level.
   */
  public void setCurrentLevel(Level currentLevel) {
    this.currentLevel = currentLevel;
  }
}
