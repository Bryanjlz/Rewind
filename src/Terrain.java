
import java.awt.*;
abstract class Terrain {
    private Rectangle hitbox;

    Terrain () {
        this.hitbox = new Rectangle(0, 0, 0, 0);
    }

    Terrain (int x, int y) {
        this.hitbox = new Rectangle(x, y, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
    }

    Terrain (int x, int y, int width, int height) {
        this.hitbox = new Rectangle(x, y, width, height);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

}
