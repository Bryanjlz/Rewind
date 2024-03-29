/**
 * Movable
 * Interface that specifies an object can move (has velocity)
 * @author Bryan Zhang
 * @since June 13/2019
 */
public interface Movable {

    /**
     * Gets the velocity vector of the object.
     * @return The velocity vector.
     */
    MyVector getVel();

    /**
     * Sets the velocity vector of the object.
     * @param vel The velocity vector.
     */
    void setVel(MyVector vel);
}
