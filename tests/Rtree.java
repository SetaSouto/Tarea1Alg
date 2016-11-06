import java.util.List;

/**
 * An R-tree is a tree that handles rectangles, it is like a B-tree. It can insert and search with
 * rectangles. For more information go to: https://en.wikipedia.org/wiki/R-tree
 *
 * Created by souto on 06-11-2016.
 */
public class Rtree {
    private int m, M;
    private Splitter splitter;
    private Node root;

    /**
     * Constructor of the R-Tree.
     *
     * @param m        minimum size of a Node.
     * @param M        maximum size of a Node.
     * @param splitter contains split's heuristic to control overflow.
     */
    public Rtree(int m, int M, Splitter splitter) {
        this.m = m;
        this.M = M;
        this.splitter = splitter;
        // It starts with a lonely leaf:
        this.root = new LeafNode(this.m, this.M, this.splitter);
    }

    /**
     * Insert a new rectangle (Data) in the R-Tree.
     *
     * @param C new Data to be inserted.
     * @return true if C was inserted correctly.
     * @throws GeneralException in case of an uncontrolled overflow.
     */
    public boolean insert(Data C) throws GeneralException {
        try {
            return this.root.insert(C);
        } catch (GeneralException e) {
            // In case of an exception doing insert() change root (LeafNode) to RootNode.
            List<Rectangle> nodes = this.root.split();
            this.root = new RootNode(this.m, this.M, this.splitter);
            this.root.addChildren(nodes);
            // Now RootNode carries about the exceptions.
            return true;
        }
    }

    /**
     * Returns all rectangles that intersect with C.
     *
     * @param C rectangle to use in the search.
     * @return a list with all the data (rectangles) that intersect with C.
     */
    public List<Data> search(Data C) {
        return this.root.search(C);
    }
}
