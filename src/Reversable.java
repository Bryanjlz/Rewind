/**
 * Reversable
 * Interface that specifies an object can be reversed in time.
 * @param <T> The object that is being reversed.
 */
public interface Reversable<T> {
  /**
   * Checks if the object is being reversed.
   * @return A boolean that represents if the object is being reversed.
   */
  boolean isReverse ();

  /**
   * Sets the boolean that represents if the object is being reversed.
   * @param reverse A boolean that represents if the object is being reversed.
   */
  void setReverse (boolean reverse);

  /**
   * Gets the queue of previous states of the object.
   * @return A queue that stores the previous states of the object.
   */
  MyQueue<T> getPreviousStateQueue();
}
