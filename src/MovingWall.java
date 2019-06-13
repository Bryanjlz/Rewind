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
    private static final double SPEED_RATIO = 7 / 120.0;

    /**
     * Constructor to create a moving wall with specified location and ID.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param id The ID.
     * @param player Reference to player for collisions.
     * @param crates Reference to crates for collosions.
     */
    public MovingWall (int x, int y, int id, Player player, MyArrayList<Crate> crates) {
        super(x, y);
        vel = new MyVector(0 ,0);
        this.id = id;
        this.player = player;
        this.crates = crates;
        currentPointIndex = 0;
    }

    /**
     * Gets the velocity vector of the moving wall.
     * @return The velocity vector.
     */
    @Override
    public MyVector getVel() {
        return vel;
    }

    /**
     * Sets the velocity vector of the moving wall.
     * @param vel The velocity vector.
     */
    @Override
    public void setVel(MyVector vel) {
        this.vel = vel;
    }

    /**
     * Sets the path the moving wall walks on.
     * @param path An array of Points that indicate path.
     */
    public void setPath(Point[] path) {
        this.path = path;
    }

    /**
     * Sets the button the wall has reference to.
     * @param button The button.
     */
    public void setButton(Button button) {
        this.button = button;
    }

    /**
     * Gets the ID of this wall.
     * @return The ID of the wall.
     */
    public int getId() {
        return id;
    }

    /**
     * Updates moving wall
     */
    @Override
    public void update() {
        // Set which point to go to depending on whether the button is being pressed
        if (button.isPressed()) {
            currentPointIndex = 1;

        } else {
            currentPointIndex = 0;
        }

        // Update velocity and move
        updateVel();
        tryMove((int)vel.getX(), (int)vel.getY());
    }

    /**
     * Updates the velocity
     */
    private void updateVel () {
        Point next = path[currentPointIndex];
        if (getHitbox().getX() < next.getX() * MainFrame.gridScreenRatio) {
            vel.setX(SPEED_RATIO * MainFrame.gridScreenRatio);
            vel.setY(0);
        } else if (getHitbox().getX() > next.getX() * MainFrame.gridScreenRatio) {
            vel.setX(-SPEED_RATIO * MainFrame.gridScreenRatio);
            vel.setY(0);
        } else if (getHitbox().getY() < next.getY() * MainFrame.gridScreenRatio) {
            vel.setY(SPEED_RATIO * MainFrame.gridScreenRatio);
            vel.setX(0);
        } else if (getHitbox().getY() > next.getY() * MainFrame.gridScreenRatio){
            vel.setY(-SPEED_RATIO * MainFrame.gridScreenRatio);
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

        // Check collision with player and crate if player is holding a crate
        if (player.isHoldingCrate() && getHitbox().intersects(player.getHeldCrate().getHitbox())) {
            collided = true;

            // Combine player and crate hitbox into one
            int x = (int) (player.getHitbox().getX());
            if (player.getDirection() == "left") {
                x = (int) (player.getHitbox().getX() - MainFrame.gridScreenRatio);
            }
            int y = (int) player.getHitbox().getY();
            int w = (int) (player.getHitbox().getWidth() + MainFrame.gridScreenRatio);
            int h = MainFrame.gridScreenRatio;
            Rectangle combinedHitbox = new Rectangle(x, y, w, h);

            // Translate original hitboxes
            Point translation = moveCollider(xMove, yMove, combinedHitbox);
            player.getHitbox().translate((int)translation.getX(), (int)translation.getY());
            player.getHeldCrate().getHitbox().translate((int)translation.getX(), (int)translation.getY());

        // Check collision with player
        } else if (getHitbox().intersects(player.getHitbox())) {
            collided = true;
            moveCollider(xMove, yMove, player.getHitbox());
        }
        return collided;
    }

    /**
     * Moves the specified hitbox to edge of this wall.
     * @param xMove Used to help determine which edge.
     * @param yMove Used to help determine which edge.
     * @param hitbox The hitbox that wall is colliding with.
     * @return
     */
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
        return new Point((int)(x + MainFrame.gridScreenRatio - player.getHitbox().getX()), (int)(y - player.getHitbox().getY()));
    }

    /**
     * Checks collision.
     * @param hitbox The hitbox to check collision with.
     * @return A boolean that represents if the wall is colliding with the hitbox.
     */
    public boolean collide (Rectangle hitbox) {
        return getHitbox().intersects(hitbox);
    }
}
