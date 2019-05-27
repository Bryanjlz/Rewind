import java.awt.*;

public class Wall extends Terrain {
  public Wall(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  @Override
  public Rectangle getHitbox() {
    return super.getHitbox();
  }

  @Override
  public void setHitbox(Rectangle hitBox) {
    super.setHitbox(hitBox);
  }

  public boolean collide(Rectangle pBox) {
    if (pBox.intersects(getHitbox())) {
      return false;
    }
    return true;
  }
}
