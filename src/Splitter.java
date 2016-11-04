import java.util.List;

/**
 * Every splitter that implements an herustic must implements this.
 * 
 * @author souto
 *
 */
interface Splitter {
  /**
   * Returns two nodes in case of overflow in a Node.
   * 
   * @param children to bre splitted.
   * @return two nodes with the children.
   */
  List<Node> splitNode(List<Rectangle> children);

  /**
   * Returnw two NodesLeaf in case of overflow in a leaf.
   * 
   * @param children to be splitted.
   * @return two NodesLeaf.
   */
  List<NodeLeaf> splitNodeLeaf(List<Rectangle> children);
}
