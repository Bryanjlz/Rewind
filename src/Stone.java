import java.awt.*;

public class Stone extends Terrain implements Movable, Updatable {
    private Vector vel;
    private static double yMaxVel = MainFrame.gridScreenRatio * 0.4;
    private static double gravityAcc = MainFrame.gridScreenRatio * 0.03;
    private Vector acc;
    private boolean onGround;
    private boolean pickedUp;
    private Player player;
    private Terrain[][] terrain;
    private MyArrayList<Stone> stones;
    public Stone (int x, int y, Terrain[][] terrain, MyArrayList<Stone> stones, Player player) {
        super(x, y);
        vel = new Vector(0, 0);
        acc = new Vector(0, 0);
        onGround = false;
        pickedUp = false;
        this.terrain = terrain;
        this.stones = stones;
        this.player = player;
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

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    public void setTerrain(Terrain[][] terrain) {
        this.terrain = terrain;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void update () {
        if (isPickedUp()) {
            Rectangle pBox = player.getHitbox();
            if (player.getDirection().equals("right")) {
                getHitbox().setLocation((int)(pBox.getX() + pBox.getWidth()), (int)(pBox.getY()));
            } else {
                getHitbox().setLocation((int)(pBox.getX() - getHitbox().getWidth()), (int)(pBox.getY()));
            }
            getVel().setX(player.getVel().getX());
            getVel().setY(player.getVel().getY());
            getAcc().setX(player.getAcc().getX());
            getAcc().setY(player.getAcc().getY());
            onGround = player.isOnGround();
        } else {
            if (onGround && vel.getX() != 0 && !(Math.abs(vel.getX()) <= 1.5)) {
                if (vel.getX() > 0) {
                    acc.setX(-1.5);
                } else {
                    acc.setX(1.5);
                }
            } else if (onGround){
                acc.setX(0);
                vel.setX(0);
            }

            if (vel.getY() > yMaxVel) {
                acc.setY(0);
            } else {
                acc.setY(gravityAcc);
            }
            //System.out.println(hitBox.x + " " + hitBox.y);
            //System.out.println(vel.getMagnitude());
            vel.setX(vel.getX() + acc.getX());
            vel.setY(vel.getY() + acc.getY());
            tryMove((int) vel.getX(), (int) vel.getY());
        }

    }

    private void tryMove (int xMove, int yMove) {
        int yPos = 0;
        int xPos = 0;
        getHitbox().translate(xMove, 0);
        if (!checkWallCollisions()) {
            if (vel.getX() > 0) {
                xPos = (int)(getHitbox().getX() / MainFrame.gridScreenRatio) * MainFrame.gridScreenRatio;
            } else {
                xPos = (int)((getHitbox().getX() / MainFrame.gridScreenRatio) + 1) * MainFrame.gridScreenRatio;
            }
            vel.setX(0);
            getHitbox().setLocation(xPos, (int)getHitbox().getY());
        }
        getHitbox().translate(0, yMove);
        if (!checkWallCollisions()) {
            if (vel.getY() > 0) {
                yPos = (int)(getHitbox().getY() / MainFrame.gridScreenRatio) * MainFrame.gridScreenRatio;
                getVel().setY(0);
                onGround = true;
            } else {
                yPos = (int)((getHitbox().getY() / MainFrame.gridScreenRatio) + 1) * MainFrame.gridScreenRatio;
                onGround = false;
                vel.setY(-vel.getY() * 0.2);
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
                    okMove = ((Wall)(terrain[i][j])).collide(getHitbox());
                }
            }
        }
        for (int i = 0; i < stones.size() && okMove; i++) {
            if (stones.get(i) != this) {
                okMove = stones.get(i).collide(getHitbox());
            }
        }
        if (!isPickedUp() && getHitbox().intersects(player.getHitbox())) {
            okMove = false;
        }
        return okMove;
    }
    
    public boolean collide(Rectangle hitbox) {
        if (hitbox.intersects(getHitbox())) {
            return false;
        }
        return true;
    }
}
