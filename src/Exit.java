import java.awt.*;

public class Exit extends Terrain {
  private Level currentLevel;
  public Exit (int x, int y) {
    super (x, y, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
  }
  public void collide(Rectangle pBox) {
    if (pBox.intersects(getHitbox())) {
      currentLevel.setLevelFinished(true);
    }
  }
  public void setCurrentLevel(Level currentLevel) {
    this.currentLevel = currentLevel;
  }
}
