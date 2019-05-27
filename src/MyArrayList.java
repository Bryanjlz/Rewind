public class MyArrayList<T> {
  private Node head;
  private int size;
  public MyArrayList () {
    head = null;
  }
  public MyArrayList (MyArrayList<T> ori) {

  }

  public void add(T item) {
    Node<T> temp = head;

    if (head == null) {
      head = new Node<T>(item);
      size++;
      return;
    }

    while(temp.getNext() != null) {
      temp = temp.getNext();
    }

    temp.setNext(new Node<T> (item));
    size++;
  }

  public T get (int index) {
    Node<T> temp = head;
    for (int i = 0; i < index; i++) {
      temp = temp.getNext();
    }
    return temp.getItem();
  }

  public int size() {
    return size;
  }
  
  public void clear () {
    head = null;
    size = 0;
  }

  public int indexOf (T item) {
    if (head == null) {
      return -1;
    }
    Node<T> temp = head;
    int index = 0;
    while (!temp.getItem().equals(item) && temp.getNext() != null) {
      temp = temp.getNext();
      index++;
    }
    if (temp.getItem().equals(item)) {
      return index;
    }
    return -1;
  }

  public void remove (int index) {
    Node<T> temp = head;
    for (int i = 0; i < index - 1; i++) {
      temp = temp.getNext();
    }
    if (temp.getNext() == null) {
      head = null;
    } else {
      temp.setNext(temp.getNext().getNext());
    }
    size--;
  }

  private class Node<T> {
    private T item;
    private Node<T> next;

    public Node() {
      item = null;
      next = null;
    }

    public Node(T item) {
      this.item = item;
      next = null;
    }

    public Node(T item, Node<T> next) {
      this.item = item;
      this.next = next;
    }

    public Node<T> getNext() {
      return next;
    }

    public void setNext(Node<T> next) {
      this.next = next;
    }

    public T getItem() {
      return item;
    }

    public void setItem(T item) {
      this.item = item;
    }
  }
}
