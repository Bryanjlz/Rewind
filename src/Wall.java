import java.awt.*;

public class Wall extends Terrain {
  public Wall(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  @Override
  public Rectangle getHitBox() {
    return super.getHitBox();
  }

  @Override
  public void setHitBox(Rectangle hitBox) {
    super.setHitBox(hitBox);
  }

  public boolean collide(Rectangle pBox) {
    if (pBox.intersects(getHitBox())) {
      return false;
    }
    return true;
  }
}
