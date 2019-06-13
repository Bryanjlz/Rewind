import java.awt.Rectangle;

/**
 * Crate
 * Class that is a crate
 * @author Bryan Zhang
 */
public class Crate extends Terrain implements Movable, Updatable, Reversable<Crate> {
    private MyVector vel;
    private  double yMaxVel;
    private  double gravityAcc;
    private double frictionAcc;
    private MyVector acc;
    private boolean onGround;
    private boolean pickedUp;
    private Player player;
    private Terrain[][] terrain;
    private MyArrayList<Crate> crates;
    private MyArrayList<MovingWall> movingWalls;
    private boolean reverse;
    private MyQueue<Crate> previousStateQueue;
    private static double FRICTION_ACC_RATIO = 2.04 / 120.0;

    /**
     * Creates a crate object.
     * @param x The x coordinate of the crate.
     * @param y The y coordinate of the crate.
     * @param terrain The 2D array of terrain to check collisions.
     * @param crates The ArrayList of crates to check collisions.
     * @param player The player to check collisions.
     */
    public Crate(int x, int y, Terrain[][] terrain, MyArrayList<Crate> crates, MyArrayList<MovingWall> movingWalls, Player player) {
        super(x, y);
        vel = new MyVector(0, 0);
        acc = new MyVector(0, 0);
        gravityAcc = MainFrame.gridScreenRatio * Player.GRAVITY_RATIO;
        yMaxVel = MainFrame.gridScreenRatio * Player.Y_MAX_VEL_RATIO;
        frictionAcc = MainFrame.gridScreenRatio * FRICTION_ACC_RATIO;
        onGround = false;
        pickedUp = false;
        reverse = false;
        previousStateQueue = new MyQueue<Crate>();
        this.terrain = terrain;
        this.crates = crates;
        this.movingWalls = movingWalls;
        this.player = player;
    }

    /**
     * Creates a deep copy of another crate.
     * @param crate The crate to copy from.
     */
    Crate (Crate crate) {
        super((int)crate.getHitbox().getX(), (int)crate.getHitbox().getY());
        vel = new MyVector (crate.getVel());
        acc = new MyVector (crate.getAcc());
        gravityAcc = MainFrame.gridScreenRatio * Player.GRAVITY_RATIO;
        yMaxVel = MainFrame.gridScreenRatio * Player.Y_MAX_VEL_RATIO;
        frictionAcc = MainFrame.gridScreenRatio * Player.RUN_ACC_RATIO;
        terrain = crate.getTerrain();
        crates = crate.getCrates();
        movingWalls = crate.getMovingWalls();
        player = crate.getPlayer();
        onGround = crate.isOnGround();
        reverse = true;
        pickedUp = false;
        onGround = false;
        previousStateQueue = crate.getPreviousStateQueue();
    }

    /**
     * Gets the velocity vector of the crate.
     * @return The velocity vector.
     */
    @Override
    public MyVector getVel() {
        return vel;
    }

    /**
     * Sets the velocity vector of the crate.
     * @param vel The velocity vector.
     */
    @Override
    public void setVel(MyVector vel) {
        this.vel = vel;
    }

    /**
     * Gets the acceleration vector of the crate.
     * @return The acceleration vector.
     */
    public MyVector getAcc() {
        return acc;
    }

    /**
     * Checks if the crate is on the ground.
     * @return A boolean that represents if the crate is on the ground.
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Checks if the crate is being picked up.
     * @return A boolean that represents if the crate is being picked up.
     */
    public boolean isPickedUp() {
        return pickedUp;
    }

    /**
     * Sets the boolean for if the crate is being picked up.
     * @param pickedUp A boolean that represents if the crate is being picked up.
     */
    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    /**
     * Checks if the crate is being reversed.
     * @return A boolean that represents if the crate is being reversed.
     */
    @Override
    public boolean isReverse() {
        return reverse;
    }

    /**
     * Sets the boolean for if the crate is being reversed.
     * @param reverse A boolean that represents if the crate is being reversed.
     */
    @Override
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    /**
     * Gets the object queue for time rewinding.
     * @return The object queue.
     */
    @Override
    public MyQueue<Crate> getPreviousStateQueue() {
        return previousStateQueue;
    }

    /**
     * Gets the terrain the crate has reference to.
     * @return The terrain 2D array.
     */
    public Terrain[][] getTerrain() {
        return terrain;
    }

    /**
     * Gets the crates this crate has reference to.
     * @return The array list of crates.
     */
    public MyArrayList<Crate> getCrates() {
        return crates;
    }

    public MyArrayList<MovingWall> getMovingWalls() {
        return movingWalls;
    }

    /**
     * Gets the player.
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player this crate has a reference to.
     * @param player The player.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Updates the crate object.
     */
    @Override
    public void update() {
        // Save state of current crate for possible rewind later
        if (!(isReverse()) && ((getPreviousStateQueue().isEmpty()) || !(equals(getPreviousStateQueue().getLast())))) {
            previousStateQueue.add(new Crate(this));
            if (previousStateQueue.size() > Player.MAX_PREVIOUS_STATES) {
                previousStateQueue.poll();
            }
        }

        // If crate is picked up, follow player
        if (isPickedUp()) {
            Rectangle pBox = player.getHitbox();

            // Adjust the hitbox of the crate depending on the direction the player is facing
            if (player.getDirection().equals("right")) {
                getHitbox().setLocation((int) (pBox.getX() + pBox.getWidth()), (int) (pBox.getY() + pBox.getHeight() - getHitbox().getHeight()));
            } else {
                getHitbox().setLocation((int) (pBox.getX() - getHitbox().getWidth()), (int) (pBox.getY() + pBox.getHeight() - getHitbox().getHeight()));
            }

            // Set player velocity to crate's velocity
            getVel().setX(player.getVel().getX());
            getVel().setY(player.getVel().getY());
        } else {
            // Apply drags and movement when crate isn't being reversed
            if (!isReverse()) {
                tryMove((int) vel.getX(), (int) vel.getY());
                checkTerrainCollisions();

                // Apply friction drag
                if (onGround) {
                    applyDrag(frictionAcc);
                }

                // Apply gravity acceleration only if player y velocity isn't greater than terminal velocity
                if (vel.getY() > yMaxVel) {
                    acc.setY(0);
                } else {
                    acc.setY(gravityAcc);
                }

                // Update velocity
                vel.setX(vel.getX() + acc.getX());
                vel.setY(vel.getY() + acc.getY());
            }
        }

    }

    /**
     * Change acceleration to opposite direction of crate velocity to slow down.
     * @param drag The acceleration of the drag.
     */
    private void applyDrag (double drag) {
        // Set velocity and acceleration of x to 0 if velocity is small
        if (Math.abs(vel.getX()) <= frictionAcc) {
            vel.setX(0);
            acc.setX(0);
        }

        if (acc.getX() > vel.getX()) {
            acc.setX(drag);
        }
        if (acc.getX() < vel.getX()) {
            acc.setX(-drag);
        }
    }

    /**
     * Tries to move through the x and y translation.
     * @param xMove The x translation.
     * @param yMove The y translation.
     */
    private void tryMove(int xMove, int yMove) {
        int yPos;
        int xPos;

        // Try to move x translation
        getHitbox().translate(xMove, 0);

        // Check if it collides with any walls, crates, or player
        if (checkWallCollisions()) {
            // Move crate to edge of the wall
            if (vel.getX() > 0) {
                xPos = (int) (getHitbox().getX() / MainFrame.gridScreenRatio) * MainFrame.gridScreenRatio;
            } else {
                xPos = (int) ((getHitbox().getX() / MainFrame.gridScreenRatio) + 1) * MainFrame.gridScreenRatio;
            }
            getHitbox().setLocation(xPos, (int) getHitbox().getY());

            // Set x velocity to 0
            getVel().setX(0);
        }

        // Try to move y translation
        getHitbox().translate(0, yMove);

        // Check if it collides with any walls, crates, or player
        if (checkWallCollisions()) {
            // Move crate to edge of the wall
            if (vel.getY() > 0) {
                yPos = (int) (getHitbox().getY() / MainFrame.gridScreenRatio) * MainFrame.gridScreenRatio;
                getVel().setY(0);
                //set on ground variable to true since it's colliding with a wall below
                onGround = true;
            } else {
                yPos = (int) ((getHitbox().getY() / MainFrame.gridScreenRatio) + 1) * MainFrame.gridScreenRatio;
                onGround = false;
                vel.setY(-vel.getY() * 0.2);
            }
            getHitbox().setLocation((int) getHitbox().getX(), yPos);
        } else {
            onGround = false;
        }
    }

    private void checkTerrainCollisions() {
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if (terrain[i][j] instanceof Button) {
                    ((Button)terrain[i][j]).collide(getHitbox());
                }
            }
        }
    }

    /**
     * Checks for collision with walls, crates, and player.
     * @return A boolean that represents if this crate is colliding with a wall, crate, or player.
     */
    private boolean checkWallCollisions() {
        boolean collided = false;

        // Checks collision with walls
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {

                // Only check collision if the terrain is a wall and not a door, or is an unlocked door
                boolean lockedDoor = (terrain[i][j] instanceof Door) && !((Door)terrain[i][j]).isUnlocked();
                boolean wall = (terrain[i][j] instanceof Wall) && !(terrain[i][j] instanceof Door);
                if (wall || lockedDoor) {
                    collided = ((Wall) (terrain[i][j])).collide(getHitbox());
                    if (collided) {
                        return true;
                    }
                }
            }
        }

        // Check collision with crates
        for (int i = 0; i < crates.size(); i++) {

            // Make sure crate doesn't check collision with itself
            if (crates.get(i) != this) {
                collided = crates.get(i).collide(getHitbox());
                if (collided) {
                    return true;
                }
            }
        }

        //Check collision with moving walls
        for (int i = 0; i < movingWalls.size(); i++) {
            collided = movingWalls.get(i).collide(getHitbox());
            if (collided) {
                return true;
            }
        }

        // Check collision with player
        if (!isPickedUp() && getHitbox().intersects(player.getHitbox())) {
            collided = true;
        }
        return collided;
    }

    /**
     * Check for collision of the crate and a given hitbox.
     * @param hitbox The hitbox of the object.
     * @return A boolean that represents if the crate is colliding with the object.
     */
    public boolean collide(Rectangle hitbox) {
        return hitbox.intersects(getHitbox());
    }

    /**
     * Checks if this crate is the same as another crate.
     * @param crate The other crate to compare to.
     * @return A boolean that represents if the crate is the same as the other.
     */
    private boolean equals (Crate crate) {
        if (!crate.getHitbox().equals(getHitbox())) {
            return false;
        }
        if (!crate.getVel().equals(getVel())) {
            return false;
        }
        if (!crate.getAcc().equals(getAcc())) {
            return false;
        }
        return true;
    }
}
