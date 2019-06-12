import java.awt.Rectangle;

public class MovingWall extends Terrain implements Movable {
    private MyVector vel;
    public MovingWall () {
        super();
        vel = new MyVector(0 ,0);
    }

    @Override
    public MyVector getVel() {
        return vel;
    }

    @Override
    public void setVel(MyVector vel) {
        this.vel = vel;
    }

    @Override
    public Rectangle getHitbox() {
        return super.getHitbox();
    }

    @Override
    public void setHitbox(Rectangle hitBox) {
        super.setHitbox(hitBox);
    }

    public void collide() {
        //TODO: collision
    }
}
