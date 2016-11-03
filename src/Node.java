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
    this.children = new ArrayList<>();
    this.MBR = null;
  }

  @Override
  public Rectangle getMBR() {
    return this.MBR;
  }

  /**
   * Refresh the MBR. Called when a new child is added to this node.
   */
  private void refreshMBR() {
    if (this.children.isEmpty()) {
      // If there is no child, the MBR is null.
      // It couldn't be no child because we have at least m children.
      this.MBR = null;
    }
    else {
      double xL, xR, yB, yT;
      xR = yT = Double.MAX_VALUE;
      xL = yB = Double.MIN_VALUE;
      for (INode child : this.children) {
        Rectangle mbr = child.getMBR();
        xL = Math.min(xL, mbr.getLeft());
        xR = Math.max(xR, mbr.getRight());
        yB = Math.min(yB, mbr.getBottom());
        yT = Math.max(yT, mbr.getTop());
      }

      // We have the maximum/minimum sides, create the MBR.
      try {
        this.MBR = new Rectangle(xL, yT, xR, yB);
      } catch (GeneralException e) {
        // If xL==xR or yT==D there's no rectangle.
        this.MBR = null;
      }
    }
  }

  @Override
  public List<Rectangle> search(Rectangle C) {
    List<Rectangle> ret = new ArrayList<Rectangle>();
    for (int i = 0; i < this.children.size(); i++) {
      ret.addAll(this.children.get(i).search(C));
    }
    return ret;
  }

  /**
   * Try to insert the rectangle C in a leaf of this sub-tree.
   * 
   * @param C the rectangle to be inserted.
   * @return true if it is inserted in this sub-tree.
   */
  public abstract boolean insert(Rectangle C);
}
