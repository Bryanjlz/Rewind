public class Acceleration {
    private double xAcc;
    private double yAcc;
    Acceleration() {
        xAcc = 0;
        yAcc = 0.1;
    }

    public void setxAcc(double xAcc) {
        this.xAcc = xAcc;
    }

    public double getxAcc() {
        return xAcc;
    }

    public void setyAcc(double yAcc) {
        this.yAcc = yAcc;
    }

    public double getyAcc() {
        return yAcc;
    }
}
