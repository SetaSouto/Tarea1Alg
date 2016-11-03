import java.util.ArrayList;
import java.util.List;

/**
 * Node is the core of a Rtree.
 * 
 * @author souto
 *
 */
public class Node extends AbstractNode {
  private int m, M;
  private Data MBR; // Minimum Bounding Rect of the node's children.
  private List<AbstractNode> children; // Children of this node.

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
  public Data getMBR() {
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
      for (AbstractNode child : this.children) {
        Data mbr = child.getMBR();
        xL = Math.min(xL, mbr.getLeft());
        xR = Math.max(xR, mbr.getRight());
        yB = Math.min(yB, mbr.getBottom());
        yT = Math.max(yT, mbr.getTop());
      }

      // We have the maximum/minimum sides, create the MBR.
      try {
        this.MBR = new Data(xL, yT, xR, yB);
      } catch (GeneralException e) {
        // If xL==xR or yT==D there's no rectangle.
        this.MBR = null;
      }
    }
  }

  @Override
  public List<Data> search(Data C) {
    List<Data> ret = new ArrayList<>();
    for (AbstractNode child : this.children) {
      ret.addAll(child.search(C));
    }
    return ret;
  }

  @Override
  public double deltaAreaQuery(Data C) {
    double xR = Math.max(this.MBR.getRight(), C.getRight());
    double xL = Math.min(this.MBR.getLeft(), C.getLeft());
    double yT = Math.max(this.MBR.getTop(), C.getTop());
    double yD = Math.min(this.MBR.getBottom(), C.getBottom());
    return ((xR - xL) * (yT - yD)) - this.MBR.getArea();
  }

  @Override
  public boolean insert(Data C) {
    AbstractNode minNode = null;
    double min = Double.MAX_VALUE;
    for(AbstractNode child : this.children) {
      if (child.deltaAreaQuery(C) < min) {
        minNode = child;
      }
    }
    this.refreshMBR();
    return min==Double.MAX_VALUE ? false : minNode.insert(C);
  }
}
