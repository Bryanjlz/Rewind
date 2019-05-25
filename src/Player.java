import java.awt.*;
public class Player implements Movable, Updatable {
    private Rectangle hitBox;
    private int xMaxVel;
    private int yMaxVel;
    private Vector vel;
    private Vector acc;
    private boolean isHoldingKey;
    private boolean isHoldingStone;
    private int timePower;
    private boolean holdUp;
    private boolean holdDown;
    private boolean holdLeft;
    private boolean holdRight;
    private Terrain[][] terrain;
    private boolean onGround;

    public Player () {
        hitBox = new Rectangle (0, 0, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        xMaxVel = 20;
        yMaxVel = 25;
        vel = new Vector(0, 0);
        acc = new Vector(0, 0.1);
        isHoldingKey = false;
        isHoldingStone = false;
        timePower = 0;
        onGround = false;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public Rectangle getHitBox() {
        return hitBox;
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

    public void setHoldingKey(boolean holdingKey) {
        isHoldingKey = holdingKey;
    }

    public boolean isHoldingKey() {
        return isHoldingKey;
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

    public void setHoldDown(boolean holdDown) { this.holdDown = holdDown; }

    public boolean isHoldDown() {
        return holdDown;
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

    public Terrain[][] getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain[][] terrain) {
        this.terrain = terrain;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void update() {
        if (isHoldUp()) {
            jump();
        }
        if (isHoldLeft()) {
            if (vel.getX() < xMaxVel) {
                acc.setX(-0.3);
            }
        } else if (isHoldRight()) {
            if (vel.getX() < xMaxVel) {
                acc.setX(0.3);
            }
        } else if (((vel.getX()) < 0 && (acc.getX() < 0)) || ((vel.getX()) > 0 && (acc.getX() > 0)) || ((Math.abs(acc.getX()) == 1) && (Math.abs(vel.getX()) == 1))) {
            vel.setX(0);
            acc.setX(0);
        }

        if (vel.getY() > yMaxVel) {
            acc.setY(0);
        } else {
            acc.setY(1);
        }

        if (isOnGround() && !isHoldUp()) {
            acc.setY(0);
        } else {
            acc.setY(1);
        }

        //System.out.println(hitBox.x + " " + hitBox.y);
        //System.out.println(vel.getMagnitude());
        vel.setX(vel.getX() + acc.getX());
        vel.setY(vel.getY() + acc.getY());
        hitBox.translate((int)vel.getX(), (int)vel.getY());
        checkCollision();
    }

    public void jump() {
        if (isOnGround()) {
            vel.setY(-yMaxVel);
        }
    }

    public void checkCollision() {
        setOnGround(false);
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if (terrain[i][j] != null) {
                    terrain[i][j].collide(this);
                }
            }
        }
    }
}
