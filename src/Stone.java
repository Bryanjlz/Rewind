import java.awt.*;

public class Stone extends Terrain implements Movable {
    private Velocity vel;
    private boolean onGround;
    public Stone () {
        super();
        vel = new Velocity();
        onGround = false;
    }

    @Override
    public Velocity getVel() {
        return vel;
    }

    @Override
    public void setVel(Velocity vel) {
        this.vel = vel;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void collide() {
        //TODO: collision
    }
}
