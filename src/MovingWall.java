import java.awt.*;

public class MovingWall extends Terrain implements Movable {
    private Vector vel;
    public MovingWall () {
        super();
        vel = new Vector(0 ,0);
    }

    @Override
    public Vector getVel() {
        return vel;
    }

    @Override
    public void setVel(Vector vel) {
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

    public void collide() {
        //TODO: collision
    }
}
