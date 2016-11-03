import java.util.ArrayList;
import java.util.List;

public class NodeLeaf implements INode {
  
  private int m, M;
  private Rectangle MBR;
  private List<Rectangle> children;

  public NodeLeaf(int m, int M) {
    this.m = m;
    this.M = M;
    this.children = new ArrayList<Rectangle>();
    this.MBR = null;
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
      for (Rectangle child : this.children) {
        Rectangle mbr = child;
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
  public Rectangle getMBR() {
    return this.MBR;
  }

  @Override
  public List<Rectangle> search(Rectangle C) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double deltaAreaQuery(Rectangle C) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean insert(Rectangle C) {
    // TODO Auto-generated method stub
    return false;
  }

}
