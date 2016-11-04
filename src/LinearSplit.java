import java.util.List;

/**
 * Implements LinearSplit hueristic.
 * 
 * @author souto
 *
 */
public class LinearSplit implements Splitter {
  int m;

  /**
   * Constructor.
   * 
   * @param m indicates minimum size of a node.
   */
  public LinearSplit(int m) {
    this.m = m;
  }

  @Override
  public List<Node> split(List<Rectangle> children) {
    
  }
}
