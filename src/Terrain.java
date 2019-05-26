
import java.awt.*;
abstract class Terrain {
    private Rectangle hitBox;

    Terrain () {
        this.hitBox = new Rectangle(0, 0, 0, 0);
    }

    Terrain (int x, int y) {
        this.hitBox = new Rectangle(x, y, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
    }

    Terrain (int x, int y, int width, int height) {
        this.hitBox = new Rectangle(x, y, width, height);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

}
