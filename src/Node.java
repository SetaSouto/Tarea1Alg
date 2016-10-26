/**
 * Node is the core of a Rtree.
 * 
 * @author souto
 *
 */
public class Node {
  int m, M;

  /**
   * Constructor.
   * 
   * @param m the minimum elements that has to have the node.
   * @param M the maximum elements that has to have the node.
   */
  public Node(int m, int M) {
    this.m = m;
    this.M = M;
  }
}
