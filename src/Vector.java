public class Vector {
    private double x;
    private double y;

    Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Vector (Vector vector) {
        x = vector.getX();
        y = vector.getY();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
