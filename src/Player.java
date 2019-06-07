import java.awt.*;
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
    private int timePower;
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
    static double FACE_WIDTH_RATIO = 1;
    static double SLIDE_HEIGHT_RATIO = 0.5;
    static double BOX_DIMENSION_RATIO = 1;
    static final double GRAVITY_RATIO = 0.03;
    static final double X_MAX_VEL_RATIO = 0.15;
    static final double Y_MAX_VEL_RATIO = 0.38;
    static final double RUN_ACC_RATIO = 0.02;
    static final double AIR_MOVE_ACC_RATIO = 0.005;

    Player() {
        hitbox = new Rectangle (0, 0, (int)(MainFrame.gridScreenRatio * 0.75), MainFrame.gridScreenRatio);
        vel = new MyVector(0, 0);
        acc = new MyVector(0, gravityAcc);
        keys = new MyArrayList<Key>();
        isHoldingCrate = false;
        heldCrate = null;
        timePower = 0;
        onGround = false;
        direction = "left";
        objectQueue = new MyQueue<Player>();
        frame = 0;
    }

    /**
     * Creates a deep copy of another player; used when saving previous states.
     * @param player Player to copy.
     */
    Player(Player player) {
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
        timePower = player.getTimePower();
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
     * @param player Player to copy.
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
        timePower = player.getTimePower();
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

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    @Override
    public void setVel(MyVector vel) {
        this.vel = vel;
    }

    @Override
    public MyVector getVel() {
        return vel;
    }

    public MyVector getAcc() {
        return acc;
    }

    public void setAcc(MyVector acc) {
        this.acc = acc;
    }

    public void setHoldingCrate(boolean holdingCrate) {
        isHoldingCrate = holdingCrate;
    }

    public boolean isHoldingCrate() {
        return isHoldingCrate;
    }

    public void setTimePower(int timePower) {
        this.timePower = timePower;
    }

    public int getTimePower() {
        return timePower;
    }

    public void setHoldUp(boolean holdUp) {
        this.holdUp = holdUp;
    }

    public boolean isHoldUp() {
        return holdUp;
    }

    public void setHoldLeft(boolean holdLeft) {
        this.holdLeft = holdLeft;
    }

    public boolean isHoldLeft() {
        return holdLeft;
    }

    public void setHoldRight(boolean holdRight) {
        this.holdRight = holdRight;
    }

    public boolean isHoldRight() {
        return holdRight;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setTerrain(Terrain[][] terrain) {
        this.terrain = terrain;
    }

    public void setCrates(MyArrayList<Crate> crates) {
        this.crates = crates;
    }

    public Crate getHeldCrate() {
        return heldCrate;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean reverse) {
        isReverse = reverse;
    }

    public boolean isReversing() {
        return isReversing;
    }

    public void setReversing(boolean reversing) {
        isReversing = reversing;
    }

    @Override
    public MyQueue<Player> getObjectQueue() {
        return objectQueue;
    }

    public void setObjectQueue(MyQueue<Player> objectQueue) {
        this.objectQueue = objectQueue;
    }

    public Terrain[][] getTerrain() {
        return terrain;
    }

    public MyArrayList<Crate> getCrates() {
        return crates;
    }

    public double getxMaxVel() {
        return xMaxVel;
    }

    public double getGravityAcc() {
        return gravityAcc;
    }

    public double getyMaxVel() {
        return yMaxVel;
    }

    public double getRunAcc() {
        return runAcc;
    }

    public void setRunAcc(double runAcc) {
        this.runAcc = runAcc;
    }

    public double getAirMoveAcc() {
        return airMoveAcc;
    }

    public void setAirMoveAcc(double airMoveAcc) {
        this.airMoveAcc = airMoveAcc;
    }

    public void setKeys(MyArrayList<Key> keys) {
        this.keys = keys;
    }

    public MyArrayList<Key> getKeys() {
        return keys;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getFrame() {
        return frame;
    }

    void startLevel() {
        xMaxVel = MainFrame.gridScreenRatio * X_MAX_VEL_RATIO;
        yMaxVel = MainFrame.gridScreenRatio * Y_MAX_VEL_RATIO;
        gravityAcc = MainFrame.gridScreenRatio * GRAVITY_RATIO;
        runAcc = MainFrame.gridScreenRatio * RUN_ACC_RATIO;
        airMoveAcc = MainFrame.gridScreenRatio * AIR_MOVE_ACC_RATIO;
    }

    @Override
    public void update() {
        if (!isReverse() || !isReversing()) {
            if (isHoldUp()) {
                jump();
            }
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
            } else if (((vel.getX() < 0) && (acc.getX() < 0)) || ((vel.getX() > 0) && (acc.getX() > 0))) {
                vel.setX(0);
                acc.setX(0);
            } else if (onGround && getVel().getX() != 0) {
                if (getVel().getX() > 0) {
                    getAcc().setX(-runAcc);
                } else {
                    getAcc().setX(runAcc);
                }
            }
            int x = 0;
            int y = 0;
            int w = 0;
            int h = 0;
            if (isHoldLeft() || isHoldRight()) {
                h = MainFrame.gridScreenRatio;
                y = (int) getHitbox().getY();
                w = (int)(SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
                x = (int)(getHitbox().getX() + getHitbox().getWidth() / 2 - w / 2);
            } else {
                w = MainFrame.gridScreenRatio;
                h = MainFrame.gridScreenRatio;
                y = (int)getHitbox().getY();
                if (isHoldingCrate()) {
                    w = (int)(SIDE_WIDTH_RATIO * MainFrame.gridScreenRatio);
                }
                x = (int)(getHitbox().getX() + getHitbox().getWidth() / 2 - w / 2);
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
            int yPos = 0;
            if (getVel().getY() > 0) {
                yPos = (int) (wBox.getY() - getHitbox().getHeight());
                getVel().setY(0);
                onGround = true;
            } else {
                yPos = (int) (wBox.getY() + wBox.getHeight());
                onGround = false;
                //getVel().setY(-getVel().getY() * 0.2);
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
