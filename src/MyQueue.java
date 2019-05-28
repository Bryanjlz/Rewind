public class MyQueue<T> {
  private Node<T> head;
  private Node<T> tail;
  MyQueue() {
    head = null;
    tail = null;
  }

  MyQueue(MyQueue<T> queue) {
    Node<T> oriTemp = queue.getHead();
    if (oriTemp != null) {
      head = new Node<T>(oriTemp.getItem());
      Node<T> copyTemp = head;
      while (oriTemp.getNext() != null) {
        copyTemp.setNext(new Node<T>(oriTemp.getNext().getItem(), copyTemp));
        oriTemp = oriTemp.getNext();
        copyTemp = copyTemp.getNext();
      }
      tail = copyTemp;
    }
  }

  public void add (T item) {
    if (head == null) {
      head = new Node(item);
      tail = head;
    } else {
      tail.setNext(new Node(item, tail));
      tail = tail.getNext();
      tail.getPrev().setNext(tail);
    }
  }

  public T poll () {
    T item = head.getItem();
    head = head.getNext();
    if (head == null) {
      tail = null;
    } else {
      head.setPrev(null);
    }
    return item;
  }

  public T pollLast () {
    T item = tail.getItem();
    tail = tail.getPrev();
    if (tail == null) {
      head = null;
    } else {
      tail.setNext(null);
    }
    return item;
  }

  public boolean isEmpty () {
    if (head == null) {
      return true;
    }
    return false;
  }

  public Node<T> getHead () {
    return head;
  }

  private class Node<T> {
    private T item;
    private Node<T> next;
    private Node<T> prev;

    public Node() {
      item = null;
      next = null;
      prev = null;
    }

    public Node(T item) {
      this.item = item;
      next = null;
      prev = null;
    }

    public Node(T item, Node<T> prev) {
      this.item = item;
      this.prev = prev;
      this.next = null;
    }

    public Node(T item, Node<T> prev,Node<T> next) {
      this.item = item;
      this.next = next;
      this.prev = prev;
    }

    public Node<T> getNext() {
      return next;
    }

    public void setNext(Node<T> next) {
      this.next = next;
    }

    public Node<T> getPrev() {
      return prev;
    }

    public void setPrev(Node<T> prev) {
      this.prev = prev;
    }

    public T getItem() {
      return item;
    }

    public void setItem(T item) {
      this.item = item;
    }
  }
}
