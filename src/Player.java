import java.awt.Rectangle;
import java.awt.Point;

/**
 * Player
 * A class that holds a player.
 * @author Bryan Zhang
 */
public class Player implements Movable, Updatable, Reversable<Player> {
    private Rectangle hitbox;
    private double gravityAcc;
    private double xMaxVel;
    private double yMaxVel;
    private double runAcc;
    private double airMoveAcc;
    private MyVector vel;
    private MyVector acc;
    private MyArrayList<Key> keys;
    private boolean isHoldingCrate;
    private Crate heldCrate;
    private boolean holdUp;
    private boolean holdLeft;
    private boolean holdRight;
    private String direction;
    private Terrain[][] terrain;
    private MyArrayList<Crate> crates;
    private MyArrayList<MovingWall> movingWalls;
    private boolean onGround;
    private boolean isDead;
    private boolean isReverse;
    private boolean isReversing;
    private MyQueue<Player> previousStateQueue;
    private int frame;
    private double jumpStartTime;
    static final int MAX_PREVIOUS_STATES = 255;
    static double SIDE_WIDTH_RATIO = 50.0 / 120.0;
    static final double GRAVITY_RATIO = 3.6 / 120.0;
    private static final double X_MAX_VEL_RATIO = 18 / 120.0;
    static final double Y_MAX_VEL_RATIO = 45.6 / 120.0;
    static final double RUN_ACC_RATIO = 2.4 / 120.0;
    private static final double AIR_MOVE_ACC_RATIO = 0.6 / 120.0;
    private static final double JUMP_RATIO = 35 / 120.0;

    /**
     * Creates a player.
     */
    Player() {
        hitbox = new Rectangle (0, 0, (int)(MainFrame.gridScreenRatio * 0.75), MainFrame.gridScreenRatio);
        vel = new MyVector(0, 0);
        acc = new MyVector(0, gravityAcc);
        keys = new MyArrayList<Key>();
        isHoldingCrate = false;
        heldCrate = null;
        onGround = false;
        direction = "left";
        previousStateQueue = new MyQueue<Player>();
        frame = 0;
    }

    /**
     * Creates a deep copy of another player; used when saving previous states.
     * @param player Player to copy from.
     */
    private Player (Player player) {
        hitbox = new Rectangle (player.getHitbox());
        xMaxVel = player.getxMaxVel();
        yMaxVel = player.getyMaxVel();
        gravityAcc = player.getGravityAcc();
        runAcc = player.getRunAcc();
        airMoveAcc = player.getAirMoveAcc();
        vel = new MyVector(player.getVel());
        acc = new MyVector(player.getAcc());
        isHoldingCrate = false;
        heldCrate = null;
        onGround = player.isOnGround();
        direction = player.getDirection();
        terrain = player.getTerrain();
        crates = player.getCrates();
        movingWalls = player.getMovingWalls();
        holdLeft = player.isHoldLeft();
        holdRight = player.isHoldRight();
        isReverse = true;
        isReversing = true;
        previousStateQueue = player.getPreviousStateQueue();
        keys = new MyArrayList<Key>();
        for (int i = 0; i < player.getKeys().size(); i++) {
            keys.add(new Key(player.getKeys().get(i)));
        }
        frame = player.getFrame();
    }

    /**
     * Creates a shallow copy of another player object; used when rewinding time.
     * @param player Player to copy from.
     */
    public void clone (Player player) {
        hitbox = player.getHitbox();
        xMaxVel = player.getxMaxVel();
        yMaxVel = player.getyMaxVel();
        gravityAcc = player.getGravityAcc();
        runAcc = player.getRunAcc();
        airMoveAcc = player.getAirMoveAcc();
        vel = player.getVel();
        acc = player.getAcc();
        isHoldingCrate = player.isHoldingCrate();
        heldCrate = player.getHeldCrate();
        onGround = player.isOnGround();
        direction = player.getDirection();
        terrain = player.getTerrain();
        crates = player.getCrates();
        holdLeft = player.isHoldLeft();
        holdRight = player.isHoldRight();
        isReverse = player.isReverse();
        isReversing = player.isReversing();
        previousStateQueue = player.getPreviousStateQueue();
        keys = player.getKeys();
        frame = player.getFrame();

    }

    /**
     * Gets the hitbox of the player.
     * @return The hitbox.
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * Sets the velocity vector of the player.
     * @param vel The velocity vector.
     */
    @Override
    public void setVel(MyVector vel) {
        this.vel = vel;
    }

    /**
     * Gets the velocity vector of the player.
     * @return The velocity vector.
     */
    @Override
    public MyVector getVel() {
        return vel;
    }

    /**
     * Gets the acceleration vector of the player.
     * @return The acceleration vector.
     */
    public MyVector getAcc() {
        return acc;
    }

    /**
     * Sets the acceleration vector of the player.
     * @param acc The acceleration vector.
     */
    public void setAcc(MyVector acc) {
        this.acc = acc;
    }

    /**
     * Sets the boolean that represents if the player is holding a crate.
     * @param holdingCrate A boolean that represents if the player is holding a crate.
     */
    public void setHoldingCrate(boolean holdingCrate) {
        isHoldingCrate = holdingCrate;
    }

    /**
     * Checks if the player is holding a crate.
     * @return A boolean that represents if the player is holding a crate.
     */
    public boolean isHoldingCrate() {
        return isHoldingCrate;
    }

    /**
     * Sets the boolean if the player is pressing up key.
     * @param holdUp A boolean that represents if the player is pressing up key.
     */
    public void setHoldUp(boolean holdUp) {
        this.holdUp = holdUp;
    }

    /**
     * Checks if the player is pressing up key.
     * @return A boolean that represents if the player is pressing up key.
     */
    public boolean isHoldUp() {
        return holdUp;
    }

    /**
     * Sets the boolean if the player is pressing left key.
     * @param holdLeft A boolean that represents if the player is pressing left key.
     */
    public void setHoldLeft(boolean holdLeft) {
        this.holdLeft = holdLeft;
    }

    /**
     * Checks if the player is pressing left key.
     * @return A boolean that represents if the player is pressing left key.
     */
    public boolean isHoldLeft() {
        return holdLeft;
    }

    /**
     * Sets the boolean if the player is pressing right key.
     * @param holdRight A boolean that represents if the player is pressing right key.
     */
    public void setHoldRight(boolean holdRight) {
        this.holdRight = holdRight;
    }

    /**
     * Checks if the player is pressing right key.
     * @return A boolean that represents if the player is pressing tight key.
     */
    public boolean isHoldRight() {
        return holdRight;
    }

    /**
     * Sets the direction the player is facing.
     * @param direction A string that represents the direction the player is facing.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Gets the direction the player is facing.
     * @return A string that represents the direction the player is facing.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the terrain the player has reference to.
     * @param terrain The 2D array of terrain.
     */
    public void setTerrain(Terrain[][] terrain) {
        this.terrain = terrain;
    }

    /**
     * Gets the terrain the player has a reference to.
     * @return The 2D array of terrain.
     */
    public Terrain[][] getTerrain() {
        return terrain;
    }

    /**
     * Sets the crates the player has reference to.
     * @param crates The ArrayList of crates.
     */
    public void setCrates(MyArrayList<Crate> crates) {
        this.crates = crates;
    }

    /**
     * Gets the crates the player has a reference to.
     * @return An ArrayList of crates.
     */
    public MyArrayList<Crate> getCrates() {
        return crates;
    }

    /**
     * Gets the moving walls the player has a reference to.
     * @return An ArrayList of moving walls.
     */
    public MyArrayList<MovingWall> getMovingWalls() {
        return movingWalls;
    }

    /**
     * Sets the moving walls the player has a reference to.
     * @param movingWalls An ArrayList of moving walls.
     */
    public void setMovingWalls(MyArrayList<MovingWall> movingWalls) {
        this.movingWalls = movingWalls;
    }

    /**
     * Gets the crate the player is holding onto.
     * @return The crate.
     */
    public Crate getHeldCrate() {
        return heldCrate;
    }

    /**
     * Checks if the player is on the ground.
     * @return A boolean that represents if the player is on the ground.
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Checks if the player is dead.
     * @return A boolean that represents if the player is dead.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Sets the boolean that represents if the player is dead.
     * @param dead A boolean that represents if the player is dead.
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }

    /**
     * Checks if the player is being reversed.
     * @return A boolean that represents if the player is being reversed.
     */
    public boolean isReverse() {
        return isReverse;
    }

    /**
     * Sets the boolean that represents if the player is being reversed.
     * @param reverse A boolean that represents if the player is being reversed.
     */
    public void setReverse(boolean reverse) {
        isReverse = reverse;
    }

    /**
     * Checks if the player is reversing something.
     * @return A boolean that represents if the player is reversing something.
     */
    public boolean isReversing() {
        return isReversing;
    }

    /**
     * Sets the boolean that represents if the player is reversing something.
     * @param reversing A boolean that represents if the player is reversing something.
     */
    public void setReversing(boolean reversing) {
        isReversing = reversing;
    }

    /**
     * Gets the object queue that stores previous states of the player.
     * @return The queue that stores previous states of the player.
     */
    @Override
    public MyQueue<Player> getPreviousStateQueue() {
        return previousStateQueue;
    }

    /**
     * Sets the queue that stores previous states of the player.
     * @param previousStateQueue A queue that stores previous states of the player.
     */
    public void setPreviousStateQueue(MyQueue<Player> previousStateQueue) {
        this.previousStateQueue = previousStateQueue;
    }

    /**
     * Gets the max x velocity of the player.
     * @return An int that represents the max x velocity of the player.
     */
    public double getxMaxVel() {
        return xMaxVel;
    }

    /**
     * Gets the gravity acceleration.
     * @return A double that represents the gravity acceleration.
     */
    public double getGravityAcc() {
        return gravityAcc;
    }

    /**
     * Gets the max y velocity.
     * @return A double that represents the max y velocity.
     */
    public double getyMaxVel() {
        return yMaxVel;
    }

    /**
     * Gets the run acceleration.
     * @return A double that represents the run acceleration.
     */
    public double getRunAcc() {
        return runAcc;
    }

    /**
     * Gets the acceleration of the player when in the air.
     * @return A double that represents the acceleration of the player in the air.
     */
    public double getAirMoveAcc() {
        return airMoveAcc;
    }

    public void setJumpStartTime(double jumpStartTime) {
        this.jumpStartTime = jumpStartTime;
    }

    /**
     * Sets the ArrayList of keys that player holds.
     * @param keys The ArrayList of keys.
     */
    public void setKeys(MyArrayList<Key> keys) {
        this.keys = keys;
    }

    /**
     * Gets the ArrayList of keys that player holds.
     * @return The ArrayList of keys.
     */
    public MyArrayList<Key> getKeys() {
        return keys;
    }

    /**
     * Sets the animation frame of the player.
     * @param frame The animation frame number of the player.
     */
    public void setFrame(int frame) {
        this.frame = frame;
    }

    /**
     * Gets the animation frame number of the player.
     * @return The animation frame number of the player.
     */
    public int getFrame() {
        return frame;
    }

    /**
     * Make sure all values are relative to the grid to screen ratio.
     */
    void startLevel() {
        xMaxVel = MainFrame.gridScreenRatio * X_MAX_VEL_RATIO;
        yMaxVel = MainFrame.gridScreenRatio * Y_MAX_VEL_RATIO;
        gravityAcc = MainFrame.gridScreenRatio * GRAVITY_RATIO;
        runAcc = MainFrame.gridScreenRatio * RUN_ACC_RATIO;
        airMoveAcc = MainFrame.gridScreenRatio * AIR_MOVE_ACC_RATIO;
    }

    /**
     * Updates the player.
     */
    @Override
    public void update() {
        // Stop updating player if time is being reversed
        if (!isReversing()) {
            // Jump if press up
            if (isHoldUp()) {
                jump();
            }

            // Move left
            if (isHoldLeft()) {
                if (vel.getX() > -xMaxVel) {
                    if (onGround && (acc.getX() < vel.getX())) {
                        acc.setX(-runAcc);
                    } else {
                        acc.setX(-airMoveAcc);
                    }
                } else {
                    acc.setX(0);
                }

             // Move right
            } else if (isHoldRight()) {
                if (vel.getX() < xMaxVel) {
                    if (onGround && (acc.getX() > vel.getX())) {
                        acc.setX(runAcc);
                    } else {
                        acc.setX(airMoveAcc);
                    }
                } else {
                    acc.setX(0);
                }

             // If no key are being pressed and velocity and acceleration are same direction, set acceleration to 0
            } else if (((vel.getX() < 0) && (acc.getX() < 0)) || ((vel.getX() > 0) && (acc.getX() > 0))) {
                vel.setX(0);
                acc.setX(0);

             // Apply drags if on ground
            } else if (onGround && getVel().getX() != 0) {
                if (getVel().getX() > 0) {
                    getAcc().setX(-runAcc);
                } else {
                    getAcc().setX(runAcc);
                }
            }

            // Change the size and location of the hitbox of the player depending on its current action
            int x;
            int y;
            int w;
            int h;
            if (!isOnGround() || isHoldLeft() || isHoldRight()) {
                // Jump or run hitbox
                h = MainFrame.gridScreenRatio;
                y = (int) getHitbox().getY();
                w = (int)(SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
                x = (int) (getHitbox().getX() + getHitbox().getWidth() / 2 - w / 2);

            } else {
                // Sliding and standing hitboz
                w = MainFrame.gridScreenRatio;
                h = MainFrame.gridScreenRatio;
                y = (int)getHitbox().getY();

                // If holding crate with sliding or standing hitbox
                if (isHoldingCrate()) {
                    w = (int)(SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
                    x = (int) (getHitbox().getX() + (getHitbox().getWidth() / 2) - (w / 2));
                } else {
                    x = (int) (getHitbox().getX() + (getHitbox().getWidth() / 2) - (w / 2));
                }
            }

            // Change hitbox
            getHitbox().setLocation(x, y);
            getHitbox().setSize(w, h);

            // Add to animation frame number
            frame++;

            // Gravity and maximum y velocity check
            if (vel.getY() > yMaxVel) {
                acc.setY(0);
            } else {
                acc.setY(gravityAcc);
            }

            // Update velocities
            vel.setX(vel.getX() + acc.getX());
            vel.setY(vel.getY() + acc.getY());

            // Try move
            tryMove((int) vel.getX(), (int) vel.getY());

            // Check if player is in the screen
            checkInScreen();

            // Add current state to queue
            if (getPreviousStateQueue().isEmpty() || !equals(getPreviousStateQueue().getLast())) {
                getPreviousStateQueue().add(new Player(this));
                if (getPreviousStateQueue().size() > MAX_PREVIOUS_STATES) {
                    getPreviousStateQueue().poll();
                }
            }
        } else {

            // If rewinding, drop crate
            heldCrate.setPickedUp(false);
            heldCrate = null;
        }
    }

    /**
     * Interact with keys or crates in front of player
     */
    void interact() {
        if (direction.equals("left")) {
            if (!isHoldingCrate()) {
                Point p = new Point((int)(getHitbox().getX() - MainFrame.gridScreenRatio * 0.2), (int)(getHitbox().getY() + getHitbox().getHeight() / 2));
                unlockDoor(p);
                pickUpCrate(p);
            } else {
                placeDownCrate();
            }
        } else {
            if (!isHoldingCrate()) {
                Point p = new Point((int)(getHitbox().getX() + getHitbox().getWidth() + MainFrame.gridScreenRatio * 0.2), (int)getHitbox().getY());
                unlockDoor(p);
                pickUpCrate(p);
            } else {
                placeDownCrate();
            }
        }
    }

    /**
     * Unlock a door if player has a matching key and is in range.
     * @param p A point that represents where the player can reach.
     */
    private void unlockDoor (Point p) {
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if (terrain[i][j] instanceof Door && terrain[i][j].getHitbox().contains(p) && keys.size() != 0) {
                    Door door = (Door)terrain[i][j];
                    for (int k = 0; k < keys.size(); k++) {
                        if (keys.get(k).isPickedUp() && keys.get(k).getId() == door.getId()) {
                            door.setUnlocked(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Pick up a crate if the player is in range.
     * @param p A point that represents where the player can reach.
     */
    private void pickUpCrate (Point p) {
        boolean foundCrate = false;
        for (int i = 0; i < crates.size() && !foundCrate; i++) {
            if (crates.get(i).getHitbox().contains(p)) {
                foundCrate = true;
                isHoldingCrate = true;
                crates.get(i).setPickedUp(true);
                heldCrate = crates.get(i);
                crates.get(i).setPlayer(this);
            }
        }
    }

    /**
     * Place down the crate the player is holding.
     */
    public void placeDownCrate () {
        heldCrate.setPickedUp(false);
        isHoldingCrate = false;
        heldCrate.getVel().setX(vel.getX());
        if (getVel().getY() < 0) {
            heldCrate.getVel().setY(-MainFrame.gridScreenRatio * 0.3);
        } else {
            heldCrate.getVel().setY(0);
        }
        heldCrate = null;
    }

    /**
     * Changes the y velocity of the player to emulate a jump.
     */
    private void jump() {
        if (System.nanoTime() / 1000000000.0 - jumpStartTime < 0.14) {
            vel.setY(-JUMP_RATIO * MainFrame.gridScreenRatio);
        }
    }

    /**
     * Tries to move the player through the x and y translations.
     * @param xMove The x translation.
     * @param yMove The y translation.
     */
    private void tryMove (int xMove, int yMove) {
        // Try the x translation first
        getHitbox().translate(xMove, 0);
        // If holding a crate, move the crate as well
        if (isHoldingCrate()) {
            heldCrate.getHitbox().translate(xMove, 0);
        }
        checkWallCollisions(true);

        // Try the y translation
        getHitbox().translate(0, yMove);
        // If holding a crate, move the crate as well
        if (isHoldingCrate()) {
            heldCrate.getHitbox().translate(0, yMove);
        }
        if (!checkWallCollisions(false)) {
            onGround = false;
        }

        // Check collision with other objects (key, exit)
        checkTerrainCollisions();
    }

    /**
     * Check collision with exit and key.
     */
    private void checkTerrainCollisions() {
        // Check collision with exits
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if (terrain[i][j] instanceof Exit) {
                    ((Exit)terrain[i][j]).collide(getHitbox());
                } else if (terrain[i][j] instanceof Button) {
                    ((Button)terrain[i][j]).collide(getHitbox());
                    if (isHoldingCrate()) {
                        ((Button) terrain[i][j]).collide(heldCrate.getHitbox());
                    }
                }
            }
        }

        // Check collision with keys
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).collide(getHitbox())) {
                keys.get(i).setPickedUp(true);
            }
        }
    }

    /**
     * Checks collision with objects you can't move through (crates, walls, and locked doors)
     * @param tryX If the player is moving through x translation or y translation.
     * @return A boolean that represents if the player is colliding with anything.
     */
    public boolean checkWallCollisions (boolean tryX) {
        boolean collided;

        // Check collision with walls hat aren't doors and locked doors
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if ((terrain[i][j] instanceof Wall && !(terrain[i][j] instanceof Door))
                        || (terrain[i][j] instanceof Door && !((Door)terrain[i][j]).isUnlocked())) {

                    // If player is holding crate, also check crate collision
                    if(isHoldingCrate()) {
                        collided = ((Wall)(terrain[i][j])).collide(heldCrate.getHitbox())
                                || ((Wall)(terrain[i][j])).collide(getHitbox());
                    } else {
                        collided = ((Wall)(terrain[i][j])).collide(getHitbox());
                        if (collided) {
                            int x = 1;
                        }
                    }
                    if (collided) {
                        wallCollide(terrain[i][j].getHitbox(), tryX);
                        return true;
                    }
                }
            }
        }

        // Check collision with crates
        for (int i = 0; i < crates.size(); i++) {
            if (crates.get(i) != heldCrate) {

                // If holding crate, also check crate collision
                if(isHoldingCrate()) {
                    collided = crates.get(i).collide(heldCrate.getHitbox()) || crates.get(i).collide(getHitbox());
                } else {
                    collided = crates.get(i).collide(getHitbox());
                }
                if (collided) {
                    wallCollide(crates.get(i).getHitbox(),tryX);
                    return true;
                }
            }
        }

        // Check collision with moving walls
        for (int i = 0; i < movingWalls.size(); i++) {
            if (isHoldingCrate()) {
                collided = movingWalls.get(i).collide(heldCrate.getHitbox()) || movingWalls.get(i).collide(getHitbox());
            } else {
                collided = movingWalls.get(i).collide(getHitbox());
            }
            if (collided) {
                wallCollide(movingWalls.get(i).getHitbox(), tryX);
                return true;
            }
        }

        return false;
    }

    /**
     * Collide with a certain hitbox, use boolen tryX to know whether the player is moving through x or y translation.
     * @param wBox The hitbox of the wall to collide with.
     * @param tryX A boolean that represents if the player is trying x translation.
     */
    private void wallCollide (Rectangle wBox, boolean tryX) {
        // The player is moving through x translation
        if (tryX) {
            int xPos;

            // Player is looking right
            if (getDirection().equals("right")) {
                if (wBox.getX() > getHitbox().getX()) {
                    xPos = (int) (wBox.getX() - getHitbox().getWidth());
                    if (isHoldingCrate()) {
                        xPos -= heldCrate.getHitbox().getWidth();
                    }
                } else {
                    xPos = (int) (wBox.getX() + wBox.getWidth());
                }

             // Player is looking left
            } else {
                if (wBox.getX() < getHitbox().getX()) {
                    xPos = (int) (wBox.getX() + wBox.getWidth());
                    if (isHoldingCrate()) {
                        xPos += heldCrate.getHitbox().getWidth();
                    }
                } else {
                    xPos = (int) (wBox.getX() - getHitbox().getWidth());
                }
            }

            // Also move crate hitbox if player is holding one
            if (isHoldingCrate()) {
                heldCrate.getHitbox().translate((int) (xPos - getHitbox().getX()), 0);
            }

            // Move player to the selected location at edge of wall, and set their x velocity to 0
            getVel().setX(0);
            getHitbox().setLocation(xPos, (int) getHitbox().getY());

        //Move through y translation
        } else {
            int yPos;

            // If is moving down and collided with wall
            if (getVel().getY() > 0) {
                yPos = (int) (wBox.getY() - getHitbox().getHeight());
                getVel().setY(0);
                onGround = true;

            // If moving up and collided with a wall
            } else {
                yPos = (int) (wBox.getY() + wBox.getHeight());
                onGround = false;
                getVel().setY(0);
            }

            // Translate held crate if player is holding one
            if (isHoldingCrate()) {
                heldCrate.getHitbox().translate(0, (int) (yPos - getHitbox().getY()));
            }

            // Translate player
            getHitbox().setLocation((int) getHitbox().getX(), yPos);
        }
    }

    /**
     * checks if the player is in the visible grid, and kills the player if it isn't.
     */
    private void checkInScreen () {
        int xPos = (int)getHitbox().getX();
        int yPos = (int)getHitbox().getY();
        if (xPos > MainFrame.WIDTH || xPos < 0 || yPos > MainFrame.HEIGHT) {
            setDead(true);
        }
    }

    /**
     * Checks if two players have the same position, velocity, and acceleration.
     * @param player The other player.
     * @return A boolean that represents if the two have the same position, velocity, and acceleration.
     */
    private boolean equals(Player player) {
        if (!getHitbox().equals(player.getHitbox())) {
            return false;
        }
        if (!getVel().equals(player.getVel())) {
            return false;
        }
        if (!getAcc().equals(player.getAcc())) {
            return false;
        }
        return true;
    }
}
