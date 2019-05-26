import java.awt.*;

public class Stone extends Terrain implements Movable, Updatable {
    private Vector vel;
    private static int yMaxVel = 25;
    private static int xMaxVel = 20;
    private Vector acc;
    private boolean onGround;
    private Terrain[][] terrain;
    public Stone (int x, int y, Terrain[][] terrain) {
        super(x, y);
        vel = new Vector(0, 0);
        onGround = false;
        this.terrain = terrain;
    }

    @Override
    public Vector getVel() {
        return vel;
    }

    @Override
    public void setVel(Vector vel) {
        this.vel = vel;
    }

    public Vector getAcc() {
        return acc;
    }

    public void setAcc(Vector acc) {
        this.acc = acc;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setTerrain(Terrain[][] terrain) {
        this.terrain = terrain;
    }

    @Override
    public void update () {
        if (((vel.getX()) < 0 && (acc.getX() < 0)) || ((vel.getX()) > 0 && (acc.getX() > 0))) {
            vel.setX(0);
            acc.setX(0);
        } else if (onGround && vel.getX() != 0) {
            if (vel.getX() > 0) {
                acc.setX(-1.5);
            } else {
                acc.setX(1.5);
            }
        }

        if (vel.getY() > yMaxVel) {
            acc.setY(0);
        } else {
            acc.setY(1);
        }
        //System.out.println(hitBox.x + " " + hitBox.y);
        //System.out.println(vel.getMagnitude());
        vel.setX(vel.getX() + acc.getX());
        vel.setY(vel.getY() + acc.getY());
        tryMove((int)vel.getX(), (int)vel.getY());
    }

    private void tryMove (int xMove, int yMove) {
        int yPos = 0;
        int xPos = 0;
        getHitBox().translate(xMove, 0);
        if (!checkWallCollisions()) {
            if (vel.getX() > 0) {
                xPos = (int)(getHitBox().getX() / MainFrame.GRID_SCREEN_RATIO) * MainFrame.GRID_SCREEN_RATIO;
            } else {
                xPos = (int)((getHitBox().getX() / MainFrame.GRID_SCREEN_RATIO) + 1) * MainFrame.GRID_SCREEN_RATIO;
            }
            vel.setX(0);
            getHitBox().setLocation(xPos, (int)getHitBox().getY());
        }
        getHitBox().translate(0, yMove);
        if (!checkWallCollisions()) {
            if (vel.getY() > 0) {
                yPos = (int)(getHitBox().getY() / MainFrame.GRID_SCREEN_RATIO) * MainFrame.GRID_SCREEN_RATIO;
                onGround = true;
            } else {
                yPos = (int)((getHitBox().getY() / MainFrame.GRID_SCREEN_RATIO) + 1) * MainFrame.GRID_SCREEN_RATIO;
                onGround = false;
                vel.setY(-vel.getY() * 0.2);
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
    
    public void collide() {
        
    }
}
