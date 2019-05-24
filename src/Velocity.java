public class Velocity {
    private double xVel;
    private double yVel;
    private double maxVel;
    public Velocity () {
        xVel = 0;
        yVel = 0;
    }

    public Velocity (double xVel, double yVel) {
        this.xVel = xVel;
        this.yVel = yVel;
    }

    public double getxVel() {
        return xVel;
    }

    public void setxVel(double xVel) {
        this.xVel = xVel;
    }

    public double getyVel() {
        return yVel;
    }

    public void setyVel(double yVel) {
        this.yVel = yVel;
    }

    public double getMagnitude () {
        return Math.sqrt(xVel * xVel + yVel * yVel);
    }
}
