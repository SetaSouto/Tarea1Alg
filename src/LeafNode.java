/**
 * Implementation of an R-Tree leaf node.
 */
public class LeafNode extends Node {

    /**
     * Default constructor.
     *
     * @param m        minimum number of childrenPaths.
     * @param M        maximum number of childrenPaths.
     * @param splitter contains split heuristic.
     */
    public LeafNode(int m, int M, Splitter splitter, String path) {
        super(m, M, splitter, path);
    }

    @Override
    public Node newNode(String path) {
        return new LeafNode(this.m, this.M, this.splitter, path);
    }

    @Override
    public boolean insert(Data C) throws GeneralException {
        String cPath = C.getPath();
        RTree.save(C, cPath);
        this.childrenPaths.add(cPath);
        this.updateMBR(C);
        if (this.childrenPaths.size() > this.M)
            throw new GeneralException("Leaf overflow");
        return true;
    }

    @Override
    public int accessCountSearch(Data C) {
        return 1;
    }

    @Override
    public int rectangleCount() {
        return this.getChildrenSize() + 1;
    }

    @Override
    public int dataCount() {
        return this.getChildrenSize();
    }

    @Override
    public int nodeCount() {
        return 1;
    }

    @Override
    public int height() {
        return 0;
    }
}
