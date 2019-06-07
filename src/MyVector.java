public class MyVector {
    private double x;
    private double y;

    MyVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    MyVector (MyVector vector) {
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

    public boolean equals (MyVector vector) {
        if (x == vector.getX() && y == vector.getY()) {
            return true;
        }
        return false;
    }
}
