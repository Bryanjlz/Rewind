import java.awt.*;

public class Crate extends Terrain implements Movable, Updatable, Reversable<Crate> {
    private MyVector vel;
    private  double yMaxVel;
    private  double gravityAcc;
    private double frictionAcc;
    private MyVector acc;
    private boolean onGround;
    private boolean pickedUp;
    private Player player;
    private Terrain[][] terrain;
    private MyArrayList<Crate> crates;
    private boolean reverse;
    private MyQueue<Crate> objectQueue;
    private static double FRICTION_ACC_RATIO = 0.017;

    public Crate(int x, int y, Terrain[][] terrain, MyArrayList<Crate> crates, Player player) {
        super(x, y);
        vel = new MyVector(0, 0);
        acc = new MyVector(0, 0);
        gravityAcc = MainFrame.gridScreenRatio * Player.GRAVITY_RATIO;
        yMaxVel = MainFrame.gridScreenRatio * Player.Y_MAX_VEL_RATIO;
        frictionAcc = MainFrame.gridScreenRatio * FRICTION_ACC_RATIO;
        onGround = false;
        pickedUp = false;
        reverse = false;
        objectQueue = new MyQueue<Crate>();
        this.terrain = terrain;
        this.crates = crates;
        this.player = player;
    }

    Crate (Crate crate) {
        super((int)crate.getHitbox().getX(), (int)crate.getHitbox().getY());
        vel = new MyVector (crate.getVel());
        acc = new MyVector (crate.getAcc());
        gravityAcc = MainFrame.gridScreenRatio * Player.GRAVITY_RATIO;
        yMaxVel = MainFrame.gridScreenRatio * Player.Y_MAX_VEL_RATIO;
        frictionAcc = MainFrame.gridScreenRatio * Player.RUN_ACC_RATIO;
        terrain = crate.getTerrain();
        crates = crate.getCrates();
        player = crate.getPlayer();
        onGround = crate.isOnGround();
        reverse = true;
        pickedUp = false;
        onGround = false;
        objectQueue = new MyQueue<Crate>(crate.getObjectQueue());
    }

    @Override
    public MyVector getVel() {
        return vel;
    }

    @Override
    public void setVel(MyVector vel) {
        this.vel = vel;
    }

    public MyVector getAcc() {
        return acc;
    }

    public void setAcc(MyVector acc) {
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

    @Override
    public boolean isReverse() {
        return reverse;
    }

    @Override
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public MyQueue<Crate> getObjectQueue() {
        return objectQueue;
    }

    public void setTerrain(Terrain[][] terrain) {
        this.terrain = terrain;
    }

    public Terrain[][] getTerrain() {
        return terrain;
    }

    public MyArrayList<Crate> getCrates() {
        return crates;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void update() {
        if (!isReverse() && (getObjectQueue().isEmpty() || !equals(getObjectQueue().getLast()))) {
            objectQueue.add(new Crate(this));
        }
        if (isPickedUp()) {
            Rectangle pBox = player.getHitbox();
            if (player.getDirection().equals("right")) {
                getHitbox().setLocation((int) (pBox.getX() + pBox.getWidth()), (int) (pBox.getY() + pBox.getHeight() - getHitbox().getHeight()));
            } else {
                getHitbox().setLocation((int) (pBox.getX() - getHitbox().getWidth()), (int) (pBox.getY() + pBox.getHeight() - getHitbox().getHeight()));
            }
            getVel().setX(player.getVel().getX());
            getVel().setY(player.getVel().getY());
        } else {
            if (!isReverse()) {
                tryMove((int) vel.getX(), (int) vel.getY());
                if (onGround) {
                    applyDrag(frictionAcc);
                }
                if (vel.getY() > yMaxVel) {
                    acc.setY(0);
                } else {
                    acc.setY(gravityAcc);
                }
                vel.setX(vel.getX() + acc.getX());
                vel.setY(vel.getY() + acc.getY());
            }
        }

    }

    private void applyDrag (double drag) {
        if (Math.abs(vel.getX()) <= frictionAcc) {
            vel.setX(0);
            acc.setX(0);
        }
        if (acc.getX() > vel.getX()) {
            acc.setX(drag);
        }
        if (acc.getX() < vel.getX()) {
            acc.setX(-drag);
        }
    }

    private void tryMove(int xMove, int yMove) {
        int yPos = 0;
        int xPos = 0;
        getHitbox().translate(xMove, 0);
        if (!checkWallCollisions()) {
            if (vel.getX() > 0) {
                xPos = (int) (getHitbox().getX() / MainFrame.gridScreenRatio) * MainFrame.gridScreenRatio;
            } else {
                xPos = (int) ((getHitbox().getX() / MainFrame.gridScreenRatio) + 1) * MainFrame.gridScreenRatio;
            }
            getVel().setX(0);
            getHitbox().setLocation(xPos, (int) getHitbox().getY());
        }
        getHitbox().translate(0, yMove);
        if (!checkWallCollisions()) {
            if (vel.getY() > 0) {
                yPos = (int) (getHitbox().getY() / MainFrame.gridScreenRatio) * MainFrame.gridScreenRatio;
                getVel().setY(0);
                onGround = true;
            } else {
                yPos = (int) ((getHitbox().getY() / MainFrame.gridScreenRatio) + 1) * MainFrame.gridScreenRatio;
                onGround = false;
                vel.setY(-vel.getY() * 0.2);
            }
            getHitbox().setLocation((int) getHitbox().getX(), yPos);
        } else {
            onGround = false;
        }
    }

    private boolean checkWallCollisions() {
        boolean okMove = true;
        for (int i = 0; i < terrain.length && okMove; i++) {
            for (int j = 0; j < terrain[0].length && okMove; j++) {
                if ((terrain[i][j] instanceof Wall && !(terrain[i][j] instanceof Door)) || (terrain[i][j] instanceof Door && !((Door)terrain[i][j]).isUnlocked())) {
                    okMove = !((Wall) (terrain[i][j])).collide(getHitbox());
                }
            }
        }
        for (int i = 0; i < crates.size() && okMove; i++) {
            if (crates.get(i) != this) {
                okMove = !crates.get(i).collide(getHitbox());
            }
        }
        if (!isPickedUp() && getHitbox().intersects(player.getHitbox())) {
            okMove = false;
        }
        return okMove;
    }

    public boolean collide(Rectangle hitbox) {
        if (hitbox.intersects(getHitbox())) {
            return true;
        }
        return false;
    }

    private boolean equals (Crate crate) {
        if (!crate.getHitbox().equals(getHitbox())) {
            return false;
        }
        if (!crate.getVel().equals(getVel())) {
            return false;
        }
        if (!crate.getAcc().equals(getAcc())) {
            return false;
        }
        return true;
    }
}
