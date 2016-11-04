import java.util.List;

/**
 * Every splitter that implements an herustic must implements this.
 * 
 * @author souto
 *
 */
interface Splitter {
  /**
   * Returns an array with the children splitted.
   * 
   * @param children to bre splitted.
   * @return two List of Nodes.
   */
  Node[] split(List<Rectangle> children, Node n1, Node n2);
}
