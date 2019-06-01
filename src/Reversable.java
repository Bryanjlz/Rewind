public interface Reversable<T> {
  boolean isReverse ();
  void setReverse (boolean reverse);
  MyQueue<T> getObjectQueue();
}
