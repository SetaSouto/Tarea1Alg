import java.util.List;

/**
 * Interface of a Node. Can be an intern Node (Node Class) or a leaf (Data Class).
 * 
 * @author souto
 *
 */
public abstract class AbstractNode implements Rectangle {

  /**
   * Returns the change in the area if C is inserted in this sub-tree.
   * 
   * @param C the rectangle to be inserted.
   * @return the change of tha area of this node's MBR.
   */
  public abstract double deltaAreaQuery(Data C);
  
  /**
   * Try to insert the rectangle C in a child of this sub-tree.
   * 
   * @param C the rectangle to be inserted.
   * @return true if it is inserted in this sub-tree.
   */
  public abstract boolean insert(Data C);


}
