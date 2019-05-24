import java.awt.*;

public class MovingWall extends Terrain implements Movable {
    private Velocity vel;
    public MovingWall () {
        super();
        vel = new Velocity();
    }

    @Override
    public Velocity getVel() {
        return vel;
    }

    @Override
    public void setVel(Velocity vel) {
        this.vel = vel;
    }

    @Override
    public Rectangle getHitBox() {
        return super.getHitBox();
    }

    @Override
    public void setHitBox(Rectangle hitBox) {
        super.setHitBox(hitBox);
    }

    @Override
    public void collide() {
        //TODO: collision
    }
}
