/**
 * MyVector
 * Class that represent vectors.
 * @author Bryan Zhang
 */
public class MyVector {
    private double x;
    private double y;

    /**
     * Constructor for a vector with the specified x and y components.
     * @param x The x component of the vector.
     * @param y The y component of the vector.
     */
    MyVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor to create a deep copy of another vector.
     * @param vector The vector to copy from.
     */
    MyVector (MyVector vector) {
        x = vector.getX();
        y = vector.getY();
    }

    /**
     * Gets the x component.
     * @return The x component.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x component.
     * @param x The x component.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y component.
     * @return The y component.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y component.
     * @param y The y component.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Check if two vectors are equal or not.
     * @param vector The vector to compare current vector to.
     * @return A boolean that represents if the vectors are equal or not.
     */
    public boolean equals (MyVector vector) {
        return x == vector.getX() && y == vector.getY();
    }
}
