import java.awt.*;

public class Stone extends Terrain implements Movable, Updatable, Reversable<Stone> {
    private Vector vel;
    private  double yMaxVel;
    private  double gravityAcc;
    private double dragAcc;
    private double frictionAcc;
    private Vector acc;
    private boolean onGround;
    private boolean pickedUp;
    private Player player;
    private Terrain[][] terrain;
    private MyArrayList<Stone> stones;
    private boolean reverse;
    private MyQueue<Stone> objectQueue;

    public Stone(int x, int y, Terrain[][] terrain, MyArrayList<Stone> stones, Player player) {
        super(x, y);
        vel = new Vector(0, 0);
        acc = new Vector(0, 0);
        gravityAcc = MainFrame.gridScreenRatio * Player.GRAVITY_RATIO;
        yMaxVel = MainFrame.gridScreenRatio * Player.Y_MAX_VEL_RATIO;
        dragAcc = MainFrame.gridScreenRatio * Player.AIR_MOVE_ACC_RATIO;
        frictionAcc = MainFrame.gridScreenRatio * Player.RUN_ACC_RATIO;
        onGround = false;
        pickedUp = false;
        reverse = false;
        objectQueue = new MyQueue<Stone>();
        this.terrain = terrain;
        this.stones = stones;
        this.player = player;
    }

    Stone (Stone stone) {
        super((int)stone.getHitbox().getX(), (int)stone.getHitbox().getY());
        vel = new Vector (stone.getVel());
        acc = new Vector (stone.getAcc());
        gravityAcc = MainFrame.gridScreenRatio * Player.GRAVITY_RATIO;
        yMaxVel = MainFrame.gridScreenRatio * Player.Y_MAX_VEL_RATIO;
        dragAcc = MainFrame.gridScreenRatio * Player.AIR_MOVE_ACC_RATIO;
        frictionAcc = MainFrame.gridScreenRatio * Player.RUN_ACC_RATIO;
        terrain = stone.getTerrain();
        stones = stone.getStones();
        player = stone.getPlayer();
        onGround = stone.isOnGround();
        reverse = true;
        pickedUp = false;
        objectQueue = new MyQueue<Stone>(stone.getObjectQueue());
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

    @Override
    public boolean isReverse() {
        return reverse;
    }

    @Override
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public MyQueue<Stone> getObjectQueue() {
        return objectQueue;
    }

    public void setTerrain(Terrain[][] terrain) {
        this.terrain = terrain;
    }

    public Terrain[][] getTerrain() {
        return terrain;
    }

    public MyArrayList<Stone> getStones() {
        return stones;
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
            objectQueue.add(new Stone(this));
        }
        if (isPickedUp()) {
            Rectangle pBox = player.getHitbox();
            if (player.getDirection().equals("right")) {
                getHitbox().setLocation((int) (pBox.getX() + pBox.getWidth()), (int) (pBox.getY()));
            } else {
                getHitbox().setLocation((int) (pBox.getX() - getHitbox().getWidth()), (int) (pBox.getY()));
            }
            getVel().setX(player.getVel().getX());
            getVel().setY(player.getVel().getY());
            onGround = player.isOnGround();
        } else {
            if (!isReverse()) {
                if (onGround) {
                    applyDrag(frictionAcc);
                } else {
                    applyDrag(dragAcc);
                }
                acc.setY(gravityAcc);
                vel.setX(vel.getX() + acc.getX());
                vel.setY(vel.getY() + acc.getY());
                tryMove((int) vel.getX(), (int) vel.getY());
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
                if (terrain[i][j] instanceof Wall) {
                    okMove = ((Wall) (terrain[i][j])).collide(getHitbox());
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

    @Override
    public void reverse() {

    }

    private boolean equals (Stone stone) {
        if (!stone.getHitbox().equals(getHitbox())) {
            return false;
        }
        if (!stone.getVel().equals(getVel())) {
            return false;
        }
        if (!stone.getAcc().equals(getAcc())) {
            return false;
        }
        return true;
    }
}
