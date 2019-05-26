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

    public Player (int x, int y) {
        hitBox = new Rectangle (x, y, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        xMaxVel = 20;
        yMaxVel = 30;
        vel = new Vector(0, 0);
        acc = new Vector(0, 3);
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

    public Terrain[][] getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain[][] terrain) {
        this.terrain = terrain;
    }

    @Override
    public void update() {
        if (isHoldUp()) {
            jump();
        }
        if (isHoldLeft()) {
            if (vel.getX() < xMaxVel) {
                if (onGround && (acc.getX() < vel.getX())) {
                    acc.setX(-1.5);
                } else {
                    acc.setX(-0.5);
                }
            }
        } else if (isHoldRight()) {
            if (vel.getX() < xMaxVel) {
                if (onGround && (acc.getX() > vel.getX())) {
                    acc.setX(1.5);
                } else {
                    acc.setX(0.5);
                }
            }
        } else if (((vel.getX()) < 0 && (acc.getX() < 0)) || ((vel.getX()) > 0 && (acc.getX() > 0))) {
            vel.setX(0);
            acc.setX(0);
        } else if (onGround && getVel().getX() != 0) {
            if (getVel().getX() > 0) {
                getAcc().setX(-1.5);
            } else {
                getAcc().setX(1.5);
            }
        }

        if (vel.getY() > yMaxVel) {
            acc.setY(0);
        } else {
            acc.setY(3);
        }
        //System.out.println(hitBox.x + " " + hitBox.y);
        //System.out.println(vel.getMagnitude());
        vel.setX(vel.getX() + acc.getX());
        vel.setY(vel.getY() + acc.getY());
        tryMove((int)vel.getX(), (int)vel.getY());
    }

    public void jump() {
        if (onGround) {
            vel.setY(-yMaxVel);
        }
    }

    private void tryMove (int xMove, int yMove) {
        int yPos = 0;
        int xPos = 0;
        getHitBox().translate(xMove, 0);
        if (!checkWallCollisions()) {
            if (getVel().getX() > 0) {
                xPos = (int)(getHitBox().getX() / MainFrame.GRID_SCREEN_RATIO) * MainFrame.GRID_SCREEN_RATIO;
            } else {
                xPos = (int)((getHitBox().getX() / MainFrame.GRID_SCREEN_RATIO) + 1) * MainFrame.GRID_SCREEN_RATIO;
            }
            getVel().setX(0);
            getHitBox().setLocation(xPos, (int)getHitBox().getY());
        }
        getHitBox().translate(0, yMove);
        if (!checkWallCollisions()) {
            if (getVel().getY() > 0) {
                yPos = (int)(getHitBox().getY() / MainFrame.GRID_SCREEN_RATIO) * MainFrame.GRID_SCREEN_RATIO;
                onGround = true;
            } else {
                yPos = (int)((getHitBox().getY() / MainFrame.GRID_SCREEN_RATIO) + 1) * MainFrame.GRID_SCREEN_RATIO;
                onGround = false;
                getVel().setY(-getVel().getY() * 0.2);
            }
            getHitBox().setLocation((int)getHitBox().getX(), yPos);
        } else {
            onGround = false;
        }
    }

    private boolean checkWallCollisions () {
        boolean okMove = true;
        for (int i = 0; i < terrain.length && okMove; i++) {
            for (int j = 0; j < terrain[0].length && okMove; j++) {
                if (terrain[i][j] instanceof Wall) {
                    okMove = ((Wall)(terrain[i][j])).collide(getHitBox());
                }
            }
        }
        return okMove;
    }
}
