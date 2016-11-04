import java.util.List;

/**
 * Implements LinearSplit hueristic.
 * 
 * @author souto
 *
 */
public class LinearSplit implements Splitter {
  int m, M;

  /**
   * Constructor.
   * 
   * @param m indicates minimum size of a node.
   * @param M indicates the maximum size of a node.
   */
  public LinearSplit(int m, int M) {
    this.m = m;
    this.M = M;
  }

  @Override
  public List<Rectangle>[] split(List<Rectangle> children) {
    // TODO Auto-generated method stub
    return null;
  }
  
}
