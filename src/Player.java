import java.awt.*;
public class Player implements Movable, Updatable, Reversable<Player> {
    private Rectangle hitbox;
    private double gravityAcc;
    private double xMaxVel;
    private double yMaxVel;
    private double runAcc;
    private double airMoveAcc;
    private Vector vel;
    private Vector acc;
    private MyArrayList<Key> keys;
    private boolean isHoldingStone;
    private Stone heldStone;
    private int timePower;
    private boolean holdUp;
    private boolean holdDown;
    private boolean holdLeft;
    private boolean holdRight;
    private String direction;
    private Terrain[][] terrain;
    private MyArrayList<Stone> stones;
    private boolean onGround;
    private boolean isDead;
    private boolean isReverse;
    private boolean isReversing;
    private MyQueue<Player> objectQueue;
    static final double GRAVITY_RATIO = 0.03;
    static final double X_MAX_VEL_RATIO = 0.15;
    static final double Y_MAX_VEL_RATIO = 0.4;
    static final double RUN_ACC_RATIO = 0.02;
    static final double AIR_MOVE_ACC_RATIO = 0.005;

    Player() {
        hitbox = new Rectangle (0, 0, MainFrame.gridScreenRatio, MainFrame.gridScreenRatio);
        vel = new Vector(0, 0);
        acc = new Vector(0, gravityAcc);
        keys = new MyArrayList<Key>();
        isHoldingStone = false;
        heldStone = null;
        timePower = 0;
        onGround = false;
        direction = "left";
        objectQueue = new MyQueue<Player>();
    }

    /**
     * Creates a shallow copy of another player.
     * @param player Player to copy.
     */
    Player(Player player) {
        hitbox = new Rectangle (player.getHitbox());
        xMaxVel = player.getxMaxVel();
        yMaxVel = player.getyMaxVel();
        gravityAcc = player.getGravityAcc();
        runAcc = player.getRunAcc();
        airMoveAcc = player.getAirMoveAcc();
        vel = new Vector(player.getVel());
        acc = new Vector(player.getAcc());
        isHoldingStone = false;
        heldStone = null;
        timePower = player.getTimePower();
        onGround = false;
        direction = player.getDirection();
        terrain = player.getTerrain();
        stones = player.getStones();
        isReverse = true;
        isReversing = true;
        objectQueue = new MyQueue<Player>(player.getObjectQueue());
    }

    /**
     * Clones another Player object to this object to not have to re-reference everything that requires player
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
        isHoldingStone = player.isHoldingStone();
        heldStone = player.getHeldStone();
        timePower = player.getTimePower();
        onGround = false;
        direction = player.getDirection();
        terrain = player.getTerrain();
        stones = player.getStones();
        isReverse = player.isReverse();
        isReversing = player.isReversing();
        objectQueue = player.getObjectQueue();

    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    @Override
    public void setVel(Vector vel) {
        this.vel = vel;
    }

    @Override
    public Vector getVel() {
        return vel;
    }

    public Vector getAcc() {
        return acc;
    }

    public void setAcc(Vector acc) {
        this.acc = acc;
    }

    public void setHoldingStone(boolean holdingStone) {
        isHoldingStone = holdingStone;
    }

    public boolean isHoldingStone() {
        return isHoldingStone;
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

    public void setHoldDown(boolean holdDown) {
        this.holdDown = holdDown;
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

    public void setStones(MyArrayList<Stone> stones) {
        this.stones = stones;
    }

    public Stone getHeldStone() {
        return heldStone;
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

    public MyArrayList<Stone> getStones() {
        return stones;
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
            } else if (((vel.getX()) < 0 && (acc.getX() < 0)) || ((vel.getX()) > 0 && (acc.getX() > 0))) {
                vel.setX(0);
                acc.setX(0);
            } else if (onGround && getVel().getX() != 0) {
                if (getVel().getX() > 0) {
                    getAcc().setX(-runAcc);
                } else {
                    getAcc().setX(runAcc);
                }
            }

            if (vel.getY() > yMaxVel) {
                acc.setY(0);
            } else {
                acc.setY(gravityAcc);
            }
            //System.out.println(hitbox.x + " " + hitbox.y);
            //System.out.println(vel.getMagnitude());
            vel.setX(vel.getX() + acc.getX());
            vel.setY(vel.getY() + acc.getY());
            tryMove((int) vel.getX(), (int) vel.getY());
            checkInScreen();
            if (getObjectQueue().isEmpty() || !equals(getObjectQueue().getLast())) {
                getObjectQueue().add(new Player(this));
            }
        } else {
            heldStone.setPickedUp(false);
            heldStone = null;
        }
    }
    
    void interact() {
        if (direction.equals("left")) {
            if (!isHoldingStone()) {
                Point p = new Point((int)(getHitbox().getX() - MainFrame.gridScreenRatio * 0.2), (int)(getHitbox().getY() + getHitbox().getHeight() / 2));
                pickUpStone(p);
            } else {
                placeDownStone();
            }
        } else {
            if (!isHoldingStone()) {
                Point p = new Point((int)(getHitbox().getX() + getHitbox().getWidth() + MainFrame.gridScreenRatio * 0.2), (int)getHitbox().getY());
                pickUpStone(p);
            } else {
                placeDownStone();
            }
        }
    }

    private void pickUpStone (Point p) {
        boolean foundStone = false;
        for (int i = 0; i < stones.size() && !foundStone; i++) {
            if (stones.get(i).getHitbox().contains(p)) {
                foundStone = true;
                isHoldingStone = true;
                stones.get(i).setPickedUp(true);
                heldStone = stones.get(i);
                stones.get(i).setPlayer(this);
            }
        }
    }

    public void placeDownStone () {
        heldStone.setPickedUp(false);
        isHoldingStone = false;
        heldStone.getVel().setX(vel.getX());
        if (getVel().getY() < 0) {
            heldStone.getVel().setY(-MainFrame.gridScreenRatio * 0.3);
        } else {
            heldStone.getVel().setY(0);
        }
        heldStone = null;
    }
    
    private void jump() {
        if (onGround) {
            vel.setY(-yMaxVel);
        }
    }

    private void tryMove (int xMove, int yMove) {
        getHitbox().translate(xMove, 0);
        if (isHoldingStone()) {
            heldStone.getHitbox().translate(xMove, 0);
        }
        checkWallCollisions(true);
        getHitbox().translate(0, yMove);
        if (isHoldingStone()) {
            heldStone.getHitbox().translate(0, yMove);
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
                } else if (terrain[i][j] instanceof Key) {
                    keys.add((Key)terrain[i][j]);
                    ((Key)terrain[i][j]).setPickedUp(true);
                }
            }
        }
    }

    private boolean checkWallCollisions (boolean tryX) {
        boolean okMove = true;
        for (int i = 0; i < terrain.length && okMove; i++) {
            for (int j = 0; j < terrain[0].length && okMove; j++) {
                if (terrain[i][j] instanceof Wall) {
                    if(isHoldingStone()) {
                        okMove = ((Wall)(terrain[i][j])).collide(heldStone.getHitbox()) && ((Wall)(terrain[i][j])).collide(getHitbox());
                    } else {
                        okMove = ((Wall)(terrain[i][j])).collide(getHitbox());
                    }
                    if (!okMove) {
                        wallCollide(terrain[i][j].getHitbox(), tryX);
                    }
                }
            }
        }
        for (int i = 0; i < stones.size() && okMove; i++) {
            if (stones.get(i) != heldStone) {
                if(isHoldingStone()) {
                    okMove = stones.get(i).collide(heldStone.getHitbox()) && stones.get(i).collide(getHitbox());
                } else {
                    okMove = stones.get(i).collide(getHitbox());
                }
                if (!okMove) {
                    wallCollide(stones.get(i).getHitbox(),tryX);
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
                    if (isHoldingStone()) {
                        xPos -= heldStone.getHitbox().getWidth();
                    }
                } else {
                    xPos = (int) (wBox.getX() + wBox.getWidth());
                }
            } else {
                if (wBox.getX() < getHitbox().getX()) {
                    xPos = (int) (wBox.getX() + wBox.getWidth());
                    if (isHoldingStone()) {
                        xPos += heldStone.getHitbox().getWidth();
                    }
                } else {
                    xPos = (int) (wBox.getX() - getHitbox().getWidth());
                }
            }
            if (isHoldingStone()) {
                heldStone.getHitbox().translate((int) (xPos - getHitbox().getX()), 0);
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
            if (isHoldingStone()) {
                heldStone.getHitbox().translate(0, (int) (yPos - getHitbox().getY()));
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
