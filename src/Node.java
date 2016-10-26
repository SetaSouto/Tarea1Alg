import java.util.ArrayList;
import java.util.List;

/**
 * Node is the core of a Rtree.
 * 
 * @author souto
 *
 */
public abstract class Node implements INode {
  int m, M;
  Rectangle MBR; // Minimum Bounding Rect of the node's children.
  List<INode> children; // Children of this node.

  /**
   * Constructor.
   * 
   * @param m the minimum elements that has to have the node.
   * @param M the maximum elements that has to have the node.
   */
  public Node(int m, int M) {
    this.m = m;
    this.M = M;
    this.children = new ArrayList();
  }

  @Override
  public Rectangle getMBR() {
    return MBR;
  }

  @Override
  public List<Rectangle> search(Rectangle C) {
    // TODO
    return null;
  }

  /**
   * Try to insert the rectangle C in a leaf of this sub-tree.
   * 
   * @param C the rectangle to be inserted.
   * @return true if it is inserted in this sub-tree.
   */
  public boolean insert(Rectangle C) {
    // TODO
    return false;
  }
}
