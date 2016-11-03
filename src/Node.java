import java.util.ArrayList;
import java.util.List;

/**
 * Node is the core of a Rtree.
 * 
 * @author souto
 *
 */
public abstract class Node implements INode {
  private int m, M;
  private Rectangle MBR; // Minimum Bounding Rect of the node's children.
  private List<INode> children; // Children of this node.

  /**
   * Constructor.
   * 
   * @param m the minimum elements that has to have the node.
   * @param M the maximum elements that has to have the node.
   */
  public Node(int m, int M) {
    this.m = m;
    this.M = M;
    this.children = new ArrayList<INode>();
    this.MBR = null;
  }

  @Override
  public Rectangle getMBR() {
    return this.MBR;
  }

  /**
   * Refresh the MBR, it is call when a new child is added to this node.
   */
  private void refreshMBR() {
    if (this.children.isEmpty()) {
      // If there is no child, the MBR is null.
      // It couldn't be no child because we have at least m children.
      this.MBR = null;
    } else {
      INode first = null; // First INode to visit.
      INode actual; // Actual node visiting.
      Rectangle r; // MBR of the actual node.
      double xL = 0, xR = 0, yT = 0, yD = 0;
      for (int i = 0; i < this.children.size(); i++) {
        if (first == null) {
          // There's no first yet.
          first = this.children.get(i);
          r = first.getMBR();
          // Set the corners.
          xL = r.getLeft();
          xR = r.getRight();
          yT = r.getTop();
          yD = r.getBottom();
        } else {
          actual = this.children.get(i);
          r = actual.getMBR();
          // Refresh the corners if needed.
          if (r.getLeft() < xL) {
            xL = r.getLeft();
          }
          if (r.getRight() > xR) {
            xR = r.getRight();
          }
          if (r.getTop() > yT) {
            yT = r.getTop();
          }
          if (r.getBottom() < yD) {
            yD = r.getBottom();
          }
        }
      }
      // We have the maximum/minimum sides, create the MBR.
      try {
        this.MBR = new Rectangle(xL, yT, xR, yD);
      } catch (GeneralException e) {
        // If xL==xR or yT==D there's no rectangle.
        this.MBR = null;
      }
    }
  }

  @Override
  public List<Rectangle> search(Rectangle C) {
    List<Rectangle> ret = new ArrayList<Rectangle>();
    for (INode child : this.children) {
      ret.addAll(child.search(C));
    }
    return ret;
  }

  /**
   * Returns the change in the area if C is inserted in this sub-tree.
   * 
   * @param C the rectangle to be inserted.
   * @return the change of tha area of this node's MBR.
   */
  public double deltaAreaQuery(Rectangle C) {
    double xR = Math.max(this.MBR.getRight(), C.getRight());
    double xL = Math.min(this.MBR.getLeft(), C.getLeft());
    double yT = Math.max(this.MBR.getTop(), C.getTop());
    double yD = Math.min(this.MBR.getBottom(), C.getBottom());
    return ((xR-xL)*(yT-yD)) - this.MBR.getArea();
  }

  /**
   * Try to insert the rectangle C in a leaf of this sub-tree.
   * 
   * @param C the rectangle to be inserted.
   * @return true if it is inserted in this sub-tree.
   */
  public abstract boolean insert(Rectangle C);
}
