import java.awt.*;

public class Wall extends Terrain {
  private double friction;
  public Wall(int x, int y, int width, int height) {
    super(x, y, width, height);
    friction = 1;
  }

  @Override
  public Rectangle getHitBox() {
    return super.getHitBox();
  }

  @Override
  public void setHitBox(Rectangle hitBox) {
    super.setHitBox(hitBox);
  }

  @Override
  void collide(Player player) {
    Rectangle pBox = player.getHitBox();
    if (pBox.intersects(getHitBox())) {
      Vector pVel = player.getVel();
      int xPos = 0;
      int yPos = 0;
      if (pVel.getX() > 0) {
        xPos = (int)(getHitBox().getX() - pBox.getWidth());
      } else if (pVel.getX() < 0) {
        xPos = (int)(getHitBox().getX() + getHitBox().getWidth());
      }

      if (pVel.getY() > 0) {
        player.getAcc().setY(0);
        pVel.setY(0);
        player.setOnGround(true);
        yPos = (int)(getHitBox().getY() - pBox.getHeight());
        if (!(player.isHoldLeft()) && !(player.isHoldRight()) && (pVel.getX() != 0)) {
          if (pVel.getX() > 0) {
            player.getAcc().setX(-friction);
          } else {
            player.getAcc().setX(friction);
          }
        }
      } else if (pVel.getY() < 0) {
        pVel.setY(0);
        yPos = (int)(getHitBox().getY() + getHitBox().getHeight());
      }
      pBox.setLocation(xPos, yPos);
    }
  }
}
