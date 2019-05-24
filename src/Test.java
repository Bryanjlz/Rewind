public class Test {
  public static void main (String[] args) {
    MyArrayList<Integer> arr = new MyArrayList<Integer>();
    arr.add(1);
    arr.add(2);
    arr.add(3);
    for (int i = 0; i < arr.size(); i++) {
      System.out.println(arr.get(i));
    }
    arr.remove(1);
    System.out.println();
    for (int i = 0; i < arr.size(); i++) {
      System.out.println(arr.get(i));
    }
    arr.remove(1);
    System.out.println();
    for (int i = 0; i < arr.size(); i++) {
      System.out.println(arr.get(i));
    }
    arr.remove(0);
    System.out.println();
    for (int i = 0; i < arr.size(); i++) {
      System.out.println(arr.get(i));
    }
  }
}