import java.util.ArrayList;
import java.util.List;

/**
 * Interface of a LinearSplitNode. Can be an intern LinearSplitNode (LinearSplitNode Class) or a leaf (Data Class).
 * 
 * @author souto
 *
 */
public class Node implements Rectangle {
  protected int m, M;
  private Data MBR; // Minimum Bounding Rect of the node's children.
  private Splitter splitter;
  protected List<Rectangle> children; // Children of this node.

  /**
   * Constructor.
   *
   * @param m the minimum elements that has to have the node.
   * @param M the maximum elements that has to have the node.
   */
  public Node(int m, int M, Splitter splitter) {
    this.m = m;
    this.M = M;
    this.children = new ArrayList<>();
    this.MBR = null;
    this.splitter = splitter;
  }

  @Override
  public Data getMBR() {
    return this.MBR;
  }

  /**
   * Refresh the MBR. Called when a new child is added to this node.
   */
  protected void refreshMBR() {
    if (this.children.isEmpty()) {
      // If there is no child, the MBR is null.
      // It couldn't be no child because we have at least m children.
      this.MBR = null;
    }
    else {
      double xL, xR, yB, yT;
      xR = yT = Double.MAX_VALUE;
      xL = yB = Double.MIN_VALUE;
      for (Rectangle child : this.children) {
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
    for (Rectangle child : this.children) {
      ret.addAll(child.search(C));
    }
    return ret;
  }

  protected double deltaAreaQuery(Data C) {
    double xR = Math.max(this.MBR.getRight(), C.getRight());
    double xL = Math.min(this.MBR.getLeft(), C.getLeft());
    double yT = Math.max(this.MBR.getTop(), C.getTop());
    double yD = Math.min(this.MBR.getBottom(), C.getBottom());
    return ((xR - xL) * (yT - yD)) - this.MBR.getArea();
  }

  /**
   * Try to insert the rectangle C in a child of this sub-tree.
   *
   * @param C the rectangle to be inserted.
   * @return true if it is inserted in this sub-tree.
   */
  public boolean insert(Data C) throws GeneralException {
    // Data must be inserted in the sub-tree who's area increases the least.
    Node minNode = null;
    double min = Double.MAX_VALUE;
    // Find insert point.
    for(Rectangle element : this.children) {
      Node child = (Node) element;
      if (child.deltaAreaQuery(C) < min) {
        min = child.deltaAreaQuery(C);
        minNode = child;
      }
    }
    this.refreshMBR();
    // cond will be false when an successive node's children is empty. This should not happen.
    boolean cond;
    try {
      cond = minNode.insert(C);
    } catch(GeneralException e) {             // manage overflow
      this.insertChildren(minNode.split());
      this.children.remove(minNode);
      cond = true;
    }
    return min != Double.MAX_VALUE && cond;
  }

  private void insertChildren(List<Node> newNodes) throws GeneralException {
    this.children.addAll(newNodes);
    if (this.children.size() > this.M) {
      throw new GeneralException("Node overflow");
    }
  }

  protected List<Node> split() {
    return this.splitter.split(this.children);
  }

}
