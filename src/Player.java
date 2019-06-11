import java.awt.Rectangle;
import java.awt.Point;

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
    private boolean onGround;
    private boolean isDead;
    private boolean isReverse;
    private boolean isReversing;
    private MyQueue<Player> objectQueue;
    private int frame;
    static double SIDE_WIDTH_RATIO = 50.0 / 85.0;
    static final double GRAVITY_RATIO = 0.03;
    static final double X_MAX_VEL_RATIO = 0.15;
    static final double Y_MAX_VEL_RATIO = 0.38;
    static final double RUN_ACC_RATIO = 0.02;
    static final double AIR_MOVE_ACC_RATIO = 0.005;

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
        objectQueue = new MyQueue<Player>();
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
        onGround = false;
        direction = player.getDirection();
        terrain = player.getTerrain();
        crates = player.getCrates();
        isReverse = true;
        isReversing = true;
        objectQueue = new MyQueue<Player>(player.getObjectQueue());
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
        onGround = false;
        direction = player.getDirection();
        terrain = player.getTerrain();
        crates = player.getCrates();
        isReverse = player.isReverse();
        isReversing = player.isReversing();
        objectQueue = player.getObjectQueue();
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
    public MyQueue<Player> getObjectQueue() {
        return objectQueue;
    }

    /**
     * Sets the queue that stores previous states of the player.
     * @param objectQueue A queue that stores previous states of the player.
     */
    public void setObjectQueue(MyQueue<Player> objectQueue) {
        this.objectQueue = objectQueue;
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
            if (!isOnGround()) {
                // Jump hitbox
                h = MainFrame.gridScreenRatio;
                y = (int) getHitbox().getY();
                w = MainFrame.gridScreenRatio;
                x = (int)(getHitbox().getX() + getHitbox().getWidth() / 2 - w / 2);
            }else if (isHoldLeft() || isHoldRight()) {
                // Running hitbox
                h = MainFrame.gridScreenRatio;
                y = (int) getHitbox().getY();
                w = (int)(SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
                x = (int)(getHitbox().getX() + getHitbox().getWidth() / 2 - w / 2);
            } else {
                // Sliding and standing hitboz
                w = MainFrame.gridScreenRatio;
                h = MainFrame.gridScreenRatio;
                y = (int)getHitbox().getY();

                // If holding crate with sliding or standing hitbox
                if (isHoldingCrate()) {
                    w = (int)(SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
                    x = (int)(getHitbox().getX());
                } else {
                    x = (int) (getHitbox().getX() + getHitbox().getWidth() / 2 - w / 2);
                }
            }
            getHitbox().setLocation(x, y);
            getHitbox().setSize(w, h);
            frame++;
            if (vel.getY() > yMaxVel) {
                acc.setY(0);
            } else {
                acc.setY(gravityAcc);
            }

            vel.setX(vel.getX() + acc.getX());
            vel.setY(vel.getY() + acc.getY());
            tryMove((int) vel.getX(), (int) vel.getY());
            checkInScreen();
            if (getObjectQueue().isEmpty() || !equals(getObjectQueue().getLast())) {
                getObjectQueue().add(new Player(this));
            }
        } else {
            heldCrate.setPickedUp(false);
            heldCrate = null;
        }
    }
    
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

    private void unlockDoor (Point p) {
        boolean foundDoor = false;
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
    
    private void jump() {
        if (onGround) {
            vel.setY(-yMaxVel);
        }
    }

    private void tryMove (int xMove, int yMove) {
        getHitbox().translate(xMove, 0);
        if (isHoldingCrate()) {
            heldCrate.getHitbox().translate(xMove, 0);
        }
        checkWallCollisions(true);
        getHitbox().translate(0, yMove);
        if (isHoldingCrate()) {
            heldCrate.getHitbox().translate(0, yMove);
        }
        if (checkWallCollisions(false)) {
            onGround = false;
        }
        checkTerrainCollisions();
    }

    private void checkTerrainCollisions() {
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if (terrain[i][j] instanceof Exit) {
                    ((Exit)terrain[i][j]).collide(getHitbox());
                }
            }
        }
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).getHitbox().intersects(getHitbox())) {
                keys.get(i).setPickedUp(true);
            }
        }
    }

    private boolean checkWallCollisions (boolean tryX) {
        boolean okMove = true;
        for (int i = 0; i < terrain.length && okMove; i++) {
            for (int j = 0; j < terrain[0].length && okMove; j++) {
                if ((terrain[i][j] instanceof Wall && !(terrain[i][j] instanceof Door)) || (terrain[i][j] instanceof Door && !((Door)terrain[i][j]).isUnlocked())) {
                    if(isHoldingCrate()) {
                        okMove = !((Wall)(terrain[i][j])).collide(heldCrate.getHitbox()) && !((Wall)(terrain[i][j])).collide(getHitbox());
                    } else {
                        okMove = !((Wall)(terrain[i][j])).collide(getHitbox());
                    }
                    if (!okMove) {
                        wallCollide(terrain[i][j].getHitbox(), tryX);
                    }
                }
            }
        }
        for (int i = 0; i < crates.size() && okMove; i++) {
            if (crates.get(i) != heldCrate) {
                if(isHoldingCrate()) {
                    okMove = !crates.get(i).collide(heldCrate.getHitbox()) && !crates.get(i).collide(getHitbox());
                } else {
                    okMove = !crates.get(i).collide(getHitbox());
                }
                if (!okMove) {
                    wallCollide(crates.get(i).getHitbox(),tryX);
                }
            }
        }
        return okMove;
    }

    private void wallCollide (Rectangle wBox, boolean tryX) {
        if (tryX) {
            int xPos = 0;
            if (getDirection().equals("right")) {
                if (wBox.getX() > getHitbox().getX()) {
                    xPos = (int) (wBox.getX() - getHitbox().getWidth());
                    if (isHoldingCrate()) {
                        xPos -= heldCrate.getHitbox().getWidth();
                    }
                } else {
                    xPos = (int) (wBox.getX() + wBox.getWidth());
                }
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
            if (isHoldingCrate()) {
                heldCrate.getHitbox().translate((int) (xPos - getHitbox().getX()), 0);
            }
            getVel().setX(0);
            getHitbox().setLocation(xPos, (int) getHitbox().getY());
        } else {
            int yPos;
            if (getVel().getY() > 0) {
                yPos = (int) (wBox.getY() - getHitbox().getHeight());
                getVel().setY(0);
                onGround = true;
            } else {
                yPos = (int) (wBox.getY() + wBox.getHeight());
                onGround = false;
                getVel().setY(0);
            }
            if (isHoldingCrate()) {
                heldCrate.getHitbox().translate(0, (int) (yPos - getHitbox().getY()));
            }
            getHitbox().setLocation((int) getHitbox().getX(), yPos);
        }
    }

    private void checkInScreen () {
        int xPos = (int)getHitbox().getX();
        int yPos = (int)getHitbox().getY();
        if (xPos > MainFrame.WIDTH || xPos < -getHitbox().getWidth() || yPos > MainFrame.HEIGHT || yPos < -getHitbox().getHeight()) {
            setDead(true);
        }
    }


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
