import java.util.ArrayList;
import java.util.List;

public class NodeLeaf extends Node {

  /**
   * Leaf of the R-Tree, has the data in its children.
   * 
   * @param m minimum number of children.
   * @param M maximum number of children.
   * @param splitter contains split heuristic.
   */
  public NodeLeaf(int m, int M, Splitter splitter) {
    super(m, M, splitter);
  }

  /**
   * Leaf of the R-Tree, has the data in its children.
   * 
   * @param m minimum number of children.
   * @param M maximum number of children.
   * @param splitter contains split heuristic.
   * @param children initial children of the leaf.
   */
  public NodeLeaf(int m, int M, Splitter splitter, List<Rectangle> children) {
    this(m, M, splitter);
    this.children.addAll(children);
  }

  @Override
  public boolean insert(Data C) throws GeneralException {
    this.children.add(C);

    if (this.children.size() > this.M)
      throw new GeneralException("Leaf overflow");

    this.refreshMBR();
    return true;
  }

  /**
   * Makes a split in case of overflow.
   *
   * @return a list with two nodes to replace this node.
   */
  protected List<NodeLeaf> split() {
    List<Rectangle>[] splitResult = this.splitter.split(this.children);
    List<NodeLeaf> ret = new ArrayList<NodeLeaf>();
    ret.add(new NodeLeaf(this.m, this.M, this.splitter, splitResult[0]));
    ret.add(new NodeLeaf(this.m, this.M, this.splitter, splitResult[1]));
    return ret;
  }

}
