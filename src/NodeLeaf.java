public class NodeLeaf extends Node {

  public NodeLeaf(int m, int M, Splitter splitter) {
    super(m, M, splitter);
  }

  @Override
  public boolean insert(Data C) throws GeneralException {
    this.children.add(C);

    if (this.children.size() > this.M) throw new GeneralException("Leaf overflow");

    return true;
  }

}
