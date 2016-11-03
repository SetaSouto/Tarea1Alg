import java.util.List;

/**
 * Created by fcocl_000 on 2016-11-03.
 */
public interface Rectangle {
  /**
   * Returns the Minimum Bounding Data that covers the rectangles that are in the sub-tree of
   * this node.
   *
   * @return the Minimum Bounding Data.
   */
  public Data getMBR();

  /**
   * Returns all the rectangles that are intersected with C.
   *
   * @param C the rectangle to be used to check intersections.
   * @return rectangles thar are insersected with C.
   */
  public List<Data> search(Data C);
}
