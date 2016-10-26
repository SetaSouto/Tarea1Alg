import java.util.List;

/**
 * Interface of a Node. Can be an intern Node (Node Class) or a leaf (Rectangle Class).
 * 
 * @author souto
 *
 */
public interface INode {
  /**
   * Returns the Minimum Bounding Rectangle that covers the rectangles that are in the sub-tree of
   * this node.
   * 
   * @return the Minimum Bounding Rectangle.
   */
  public Rectangle getMBR();

  /**
   * Returns all the rectangles that are intersected with C.
   * 
   * @param C the rectangle to be used to check intersections.
   * @return rectangles thar are insersected with C.
   */
  public List<Rectangle> search(Rectangle C);
}
