/**
 * MyQueue
 * A queue to hold objects.
 * @param <T> The type of element held in this collection.
 * @author Bryan Zhang
 */
public class MyQueue<T> {
  private Node<T> head;
  private Node<T> tail;
  private int size;

  /**
   * Constructor for an empty queue.
   */
  MyQueue() {
    head = null;
    tail = null;
  }

  /**
   * Copy constructor to create a deep copy of another queue.
   * @param queue The queue to copy from.
   */
  MyQueue(MyQueue<T> queue) {
    Node<T> oriTemp = queue.getHead();
    // Copy items in the queue to this queue
    if (oriTemp != null) {
      head = new Node<T>(oriTemp.getItem());
      Node<T> copyTemp = head;
      while (oriTemp.getNext() != null) {
        copyTemp.setNext(new Node<T>(oriTemp.getNext().getItem(), copyTemp));
        oriTemp = oriTemp.getNext();
        copyTemp = copyTemp.getNext();
      }
      size = queue.size();
      // Set tail to last item
      tail = copyTemp;
    }
  }

  /**
   * Adds an item to the end of the queue.
   * @param item The item to add.
   */
  public void add (T item) {
    if (head == null) {
      head = new Node<T>(item);
      tail = head;
    } else {
      tail.setNext(new Node<T>(item, tail));
      tail = tail.getNext();
      tail.getPrev().setNext(tail);
    }
    size++;
  }

  /**
   * Get the head item of the queue.
   * @return The item at the head of the queue.
   */
  public T poll () {
    T item = head.getItem();
    head = head.getNext();
    if (head == null) {
      tail = null;
    } else {
      head.setPrev(null);
    }
    size--;
    return item;
  }

  /**
   * Gets the tail item of the queue.
   * @return The item at the tail of the queue.
   */
  public T pollLast () {
    T item = tail.getItem();
    tail = tail.getPrev();
    if (tail == null) {
      head = null;
    } else {
      tail.setNext(null);
    }
    size--;
    return item;
  }

  /**
   * Checks if the queue is empty or not.
   * @return A boolean that represents if the queue is empty or not.
   */
  public boolean isEmpty () {
    if (head == null) {
      return true;
    }
    return false;
  }

  public int size () {
    return size;
  }

  /**
   * Gets the head node of the queue.
   * @return The head node of the queue.
   */
  private Node<T> getHead () {
    return head;
  }

  /**
   * Gets the tail item of the queue.
   * @return The item at the tail of the queue.
   */
  public T getLast () {
    return tail.getItem();
  }

  /**
   * Node
   * Node inner class used to store items and make up the queue.
   * @param <T> The type of element held in this collection.
   */
  private class Node<T> {
    private T item;
    private Node<T> next;
    private Node<T> prev;

    /**
     * Constructor to create a node with the specified item.
     * @param item The item.
     */
    private Node(T item) {
      this.item = item;
      next = null;
      prev = null;
    }

    /**
     * Constructor to create a node with the specified item and the previous node.
     * @param item Item to store in node.
     * @param prev Previous node.
     */
    private Node(T item, Node<T> prev) {
      this.item = item;
      this.prev = prev;
      this.next = null;
    }

    /**
     * Gets the next node.
     * @return The next node.
     */
    private Node<T> getNext() {
      return next;
    }

    /**
     * Sets the next node.
     * @param next The next node.
     */
    private void setNext(Node<T> next) {
      this.next = next;
    }

    /**
     * Gets the previous node.
     * @return The previous node.
     */
    private Node<T> getPrev() {
      return prev;
    }

    /**
     * Sets the previous node.
     * @param prev The previous node.
     */
    private void setPrev(Node<T> prev) {
      this.prev = prev;
    }

    /**
     * Gets the item of this node.
     * @return The item.
     */
    private T getItem() {
      return item;
    }

  }
}
