import java.awt.Rectangle;
import java.awt.Point;

/**
 * MovingWall
 * Class that represents moving walls.
 * @author Bryan Zhang
 */
public class MovingWall extends Wall implements Movable, Updatable {
    private MyVector vel;
    private Point[] path;
    private int id;
    private int currentPointIndex;
    private Button button;
    private Player player;
    private MyArrayList<Crate> crates;
    private static final int SPEED_RATIO = 1;
    public MovingWall (int x, int y, int id, Player player, MyArrayList<Crate> crates) {
        super(x, y);
        vel = new MyVector(0 ,0);
        this.id = id;
        this.player = player;
        this.crates = crates;
        currentPointIndex = 0;
    }

    @Override
    public MyVector getVel() {
        return vel;
    }

    @Override
    public void setVel(MyVector vel) {
        this.vel = vel;
    }

    public void setPath(Point[] path) {
        this.path = path;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getId() {
        return id;
    }

    @Override
    public void update() {
        if (button.isPressed()) {
            currentPointIndex = 1;

        } else {
            currentPointIndex = 0;
        }
        updateVel();
        tryMove((int)vel.getX(), (int)vel.getY());
    }

    private void updateVel () {
        Point next = path[currentPointIndex];
        if (getHitbox().getX() < next.getX() * MainFrame.gridScreenRatio) {
            vel.setX(5);
            vel.setY(0);
        } else if (getHitbox().getX() > next.getX() * MainFrame.gridScreenRatio) {
            vel.setX(-5);
            vel.setY(0);
        } else if (getHitbox().getY() < next.getY() * MainFrame.gridScreenRatio) {
            vel.setY(5);
            vel.setX(0);
        } else if (getHitbox().getY() > next.getY() * MainFrame.gridScreenRatio){
            vel.setY(-5);
            vel.setX(0);
        } else {
            vel.setX(0);
            vel.setY(0);
        }
    }

    /**
     * Tries to move through the x and y translation.
     * @param xMove The x translation.
     * @param yMove The y translation.
     */
    private void tryMove(int xMove, int yMove) {
        // Try to move x translation
        getHitbox().translate(xMove, 0);
        checkWallCollisions(xMove, 0);

        // Try to move y translation
        getHitbox().translate(0, yMove);
        checkWallCollisions(0, yMove);
    }

    /**
     * Checks for collision with crates and player.
     * @return A boolean that represents if this crate is colliding with a crate or player.
     */
    private boolean checkWallCollisions(int xMove, int yMove) {
        boolean collided = false;

        // Check collision with crates
        for (int i = 0; i < crates.size(); i++) {
            Crate crate = crates.get(i);
            collided = crate.collide(getHitbox());
            if (collided) {
                if (!player.isHoldingCrate() || (crate != player.getHeldCrate())) {
                    moveCollider(xMove, yMove, crate.getHitbox());
                }

            }
        }

        if (player.isHoldingCrate() && getHitbox().intersects(player.getHeldCrate().getHitbox())) {
            collided = true;
            int x = (int) (player.getHitbox().getX());
            if (player.getDirection() == "left") {
                x = (int) (player.getHitbox().getX() - MainFrame.gridScreenRatio);
            }
            int y = (int) player.getHitbox().getY();
            int w = (int) (player.getHitbox().getWidth() + MainFrame.gridScreenRatio);
            int h = MainFrame.gridScreenRatio;
            Rectangle combinedHitbox = new Rectangle(x, y, w, h);
            Point movedLocation = moveCollider(xMove, yMove, combinedHitbox);
            player.getHitbox().setLocation(movedLocation);

        // Check collision with player
        } else if (getHitbox().intersects(player.getHitbox())) {
            collided = true;
            moveCollider(xMove, yMove, player.getHitbox());
        }


        return collided;
    }

    private Point moveCollider (int xMove, int yMove, Rectangle hitbox) {
        int x = (int)hitbox.getX();
        int y = (int)hitbox.getY();
        if (xMove > 0) {
            x = (int)(getHitbox().getX() + getHitbox().getWidth());
        } else if (xMove < 0) {
            x = (int)(getHitbox().getX() - hitbox.getWidth());
        } else if (yMove > 0) {
            y = (int)(getHitbox().getY() + getHitbox().getWidth());
        } else if (yMove < 0) {
            y = (int)(getHitbox().getY() - hitbox.getWidth());
        }
        hitbox.setLocation(x, y);
        return new Point(x, y);
    }

    public boolean collide (Rectangle hitbox) {
        return getHitbox().intersects(hitbox);
    }
}
