import java.util.List;

interface Rectangle {
  /**
   * Returns the Minimum Bounding Data that covers the rectangles that are in the sub-tree of
   * this node.
   *
   * @return the Minimum Bounding Data.
   */
  Data getMBR();

  /**
   * Returns all the rectangles that are intersected with C.
   *
   * @param C the rectangle to be used to check intersections.
   * @return rectangles thar are intersected with C.
   */
  List<Data> search(Data C);
}
