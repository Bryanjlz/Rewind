import java.awt.*;

public class Stone extends Terrain implements Movable {
    private Vector vel;
    private boolean onGround;
    public Stone () {
        super();
        vel = new Vector();
        onGround = false;
    }

    @Override
    public Vector getVel() {
        return vel;
    }

    @Override
    public void setVel(Vector vel) {
        this.vel = vel;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void collide(Player player) {
        //TODO: collision
    }
}
