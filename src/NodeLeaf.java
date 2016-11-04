import java.util.List;

public class NodeLeaf extends Node {
  
  /**
   * Leaf of the R-Tree, has the data in its children.
   * @param m minimum number of children.
   * @param M maximum number of children.
   * @param splitter contains split heuristic.
   */
  public NodeLeaf(int m, int M, Splitter splitter) {
    super(m, M, splitter);
  }

  @Override
  public boolean insert(Data C) throws GeneralException {
    this.children.add(C);

    if (this.children.size() > this.M) throw new GeneralException("Leaf overflow");

    return true;
  }
  
  /**
   * Makes a split in case of overflow.
   *
   * @return a list with two nodes to replace this node.
   */
  protected List<NodeLeaf> split() {
    return this.splitter.splitNodeLeaf(this.children);
  }

}
