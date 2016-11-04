
public class Test {
  public static void main(String[] args) {
    A[] array =  foo();
    B b1 = (B) array[0];
    System.out.println(b1.foo());
    System.out.println(array[0].foo());
    
  }
  
  public static A[] foo() {
    B[] array = {new B(), new B()};
    return array;
  }
}
