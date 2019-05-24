import java.awt.*;
public class Player implements Movable, Updatable {
    private Rectangle hitBox;
    private int xMaxVel;
    private int yMaxVel;
    private Velocity vel;
    private Acceleration acc;
    private boolean isHoldingKey;
    private boolean isHoldingStone;
    private int timePower;
    private boolean holdUp;
    private boolean holdDown;
    private boolean holdLeft;
    private boolean holdRight;

    public Player () {
        hitBox = new Rectangle (0, 0, 100, 100);
        xMaxVel = 20;
        yMaxVel = 25;
        vel = new Velocity();
        acc = new Acceleration();
        isHoldingKey = false;
        isHoldingStone = false;
        timePower = 0;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void setVel(Velocity vel) {
        this.vel = vel;
    }

    @Override
    public Velocity getVel() {
        return vel;
    }

    public Acceleration getAcc() {
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

    public void setHoldDown(boolean holdDown) {
        this.holdDown = holdDown;
    }

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

    @Override
    public void update() {
        if (isHoldUp()) {
            jump();
        }
        if (isHoldLeft()) {
            if (vel.getxVel() < xMaxVel) {
                acc.setxAcc(-0.5);
            }
        } else if (isHoldRight()) {
            if (vel.getxVel() < xMaxVel) {
                acc.setxAcc(0.5);
            }
        } else if (((vel.getxVel()) < 0 && (acc.getxAcc() < 0)) || ((vel.getxVel()) > 0 && (acc.getxAcc() > 0))) {
            vel.setxVel(0);
            acc.setxAcc(0);
        }

        if (vel.getyVel() > yMaxVel) {
            acc.setyAcc(0);
        } else {
            acc.setyAcc(1);
        }
        //System.out.println(hitBox.x + " " + hitBox.y);
        //System.out.println(vel.getMagnitude());
        vel.setxVel(vel.getxVel() + acc.getxAcc());
        vel.setyVel(vel.getyVel() + acc.getyAcc());
        hitBox.translate((int)vel.getxVel(), (int)vel.getyVel());
    }

    public void jump() {
        if (hitBox.getY() > 500) {
            vel.setyVel(-yMaxVel);
        }
    }

    public void checkCollision() {

    }
}
