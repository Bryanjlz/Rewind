import java.awt.*;
public class Player implements Movable, Updatable {
    private Rectangle hitbox;
    private double gravityAcc;
    private double xMaxVel;
    private double yMaxVel;
    private Vector vel;
    private Vector acc;
    private boolean isHoldingKey;
    private boolean isHoldingStone;
    private Stone heldStone;
    private int timePower;
    private boolean holdUp;
    private boolean holdDown;
    private boolean holdLeft;
    private boolean holdRight;
    private Terrain[][] terrain;
    private MyArrayList<Stone> stones;
    private boolean onGround;
    private String direction;

    public Player (int x, int y) {
        hitbox = new Rectangle (x, y, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        xMaxVel = MainFrame.GRID_SCREEN_RATIO * 0.2;
        yMaxVel = MainFrame.GRID_SCREEN_RATIO * 0.4;
        gravityAcc = MainFrame.GRID_SCREEN_RATIO * 0.03;
        vel = new Vector(0, 0);
        acc = new Vector(0, gravityAcc);
        isHoldingKey = false;
        isHoldingStone = false;
        heldStone = null;
        timePower = 0;
        onGround = false;
        direction = "left";
    }

    public void holdStone () {
        isHoldingStone = true;
        stones.get(0).setPickedUp(true);
        heldStone = stones.get(0);
        stones.get(0).setPlayer(this);
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
            acc.setY(gravityAcc);
        }
        //System.out.println(hitbox.x + " " + hitbox.y);
        //System.out.println(vel.getMagnitude());
        vel.setX(vel.getX() + acc.getX());
        vel.setY(vel.getY() + acc.getY());
        tryMove((int)vel.getX(), (int)vel.getY());
    }
    
    public void interact() {
        if (direction.equals("left")) {
            if (!isHoldingStone()) {
                boolean foundStone = false;
                Point p = new Point((int)(getHitbox().getX() - MainFrame.GRID_SCREEN_RATIO * 0.2), (int)getHitbox().getY());
                for (int i = 0; i < stones.size() && !foundStone; i++) {
                    if (stones.get(i).getHitbox().contains(p)) {
                        foundStone = true;
                        isHoldingStone = true;
                        stones.get(i).setPickedUp(true);
                        heldStone = stones.get(i);
                        stones.get(i).setPlayer(this);
                    }
                }
            } else {
                heldStone.setPickedUp(false);
                heldStone = null;
                isHoldingStone = false;
            }
        }
    }
    
    private void jump() {
        if (onGround) {
            vel.setY(-yMaxVel);
        }
    }

    private void tryMove (int xMove, int yMove) {
        int yPos = 0;
        int xPos = 0;
        getHitbox().translate(xMove, 0);
        if (isHoldingStone()) {
            heldStone.getHitbox().translate(xMove, 0);
        }
        if (!checkWallCollisions()) {
            if (getVel().getX() > 0) {
                xPos = (int)(getHitbox().getX() / MainFrame.GRID_SCREEN_RATIO) * MainFrame.GRID_SCREEN_RATIO;
            } else {
                xPos = (int)((getHitbox().getX() / MainFrame.GRID_SCREEN_RATIO) + 1) * MainFrame.GRID_SCREEN_RATIO;
            }
            getVel().setX(0);
            getHitbox().setLocation(xPos, (int)getHitbox().getY());
        }
        getHitbox().translate(0, yMove);
        if (!checkWallCollisions()) {
            if (getVel().getY() > 0) {
                yPos = (int)(getHitbox().getY() / MainFrame.GRID_SCREEN_RATIO) * MainFrame.GRID_SCREEN_RATIO;
                getVel().setY(0);
                onGround = true;
            } else {
                yPos = (int)((getHitbox().getY() / MainFrame.GRID_SCREEN_RATIO) + 1) * MainFrame.GRID_SCREEN_RATIO;
                onGround = false;
                getVel().setY(-getVel().getY() * 0.2);
            }
            getHitbox().setLocation((int)getHitbox().getX(), yPos);
        } else {
            onGround = false;
        }
    }

    private boolean checkWallCollisions () {
        boolean okMove = true;
        for (int i = 0; i < terrain.length && okMove; i++) {
            for (int j = 0; j < terrain[0].length && okMove; j++) {
                if (terrain[i][j] instanceof Wall) {
                    if(isHoldingStone()) {
                        okMove = ((Wall)(terrain[i][j])).collide(heldStone.getHitbox()) && ((Wall)(terrain[i][j])).collide(getHitbox());
                    } else {
                        okMove = ((Wall)(terrain[i][j])).collide(getHitbox());
                    }
                }
            }
        }
        for (int i = 0; i < stones.size() && okMove; i++) {
            if (stones.get(i) != heldStone) {
                okMove = stones.get(i).collide(getHitbox());
                /*if(isHoldingStone()) {
                    okMove = stones.get(i).collide(heldStone.getHitbox());
                }*/
            }
        }
        return okMove;
    }
}
