import java.awt.Rectangle;

/**
 *
 */
public class MovingWall extends Wall implements Movable, Updatable {
    private MyVector vel;
    public MovingWall (int x, int y) {
        super(x, y);
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
    public void update() {

    }

}
