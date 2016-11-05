/**
 * Implementation of an R-Tree leaf node.
 */
public class LeafNode extends Node {

    /**
     * Default constructor.
     *
     * @param m        minimum number of children.
     * @param M        maximum number of children.
     * @param splitter contains split heuristic.
     */
    public LeafNode(int m, int M, Splitter splitter) {
        super(m, M, splitter);
    }

    @Override
    public Node newNode() {
        return new LeafNode(this.m, this.M, this.splitter);
    }

    @Override
    public boolean insert(Data C) throws GeneralException {
        this.children.add(C);

        if (this.children.size() > this.M)
            throw new GeneralException("Leaf overflow");

        this.refreshMBR();
        return true;
    }
}
