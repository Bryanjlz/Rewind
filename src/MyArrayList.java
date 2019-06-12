/**
 * MyArrayList
 * An array list to hold objects.
 * @param <T> The type of element held in this collection.
 * @author Bryan Zhang
 */
public class MyArrayList<T> {
  private Node<T> head;
  private int size;

  /**
   * Constructor for an empty ArrayList
   */
  public MyArrayList () {
    head = null;
  }

  /**
   * Adds an item to the end of the ArrayList
   * @param item The item to add.
   */
  public void add(T item) {
    Node<T> temp = head;

    // If head is null, add item to head
    if (head == null) {
      head = new Node<T>(item);
      size++;
      return;
    }

    // Get to the last element
    while(temp.getNext() != null) {
      temp = temp.getNext();
    }

    // Add item to last element and increase size by one
    temp.setNext(new Node<T> (item));
    size++;
  }

  /**
   * Gets an item at an index.
   * @param index The index to get item from.
   * @return The item.
   */
  public T get (int index) {

    // Get to the node with specific index.
    Node<T> temp = head;
    for (int i = 0; i < index; i++) {
      temp = temp.getNext();
    }

    return temp.getItem();
  }

  /**
   * Gets the number of elements in the ArrayList.
   * @return The number of elements in the ArrayList.
   */
  public int size() {
    return size;
  }

  /**
   * Sets an item to the node at the specified index.
   * @param index The index of the node to put item at.
   * @param item The item.
   */
  public void set (int index, T item) {

    // Get to the node at specified index
    Node<T> temp = head;
    for (int i = 0; i < index; i++) {
      temp = temp.getNext();
    }
    temp.setItem(item);
  }

  /**
   * Node
   * Inner class node, used to store the items and form ArrayList.
   * @param <T> The type of element held in the collection.
   */
  private class Node<T> {
    private T item;
    private Node<T> next;

    /**
     * Creates a node with an item.
     * @param item The item.
     */
    private Node(T item) {
      this.item = item;
      next = null;
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
     * Gets the item of this node.
     * @return The item.
     */
    private T getItem() {
      return item;
    }

    /**
     * Sets the item of this node.
     * @param item The item.
     */
    private void setItem(T item) {
      this.item = item;
    }
  }
}
