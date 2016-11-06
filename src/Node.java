import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an R-Tree Node.
 */
public class Node implements Rectangle {
    protected int m, M;
    private Data MBR; // Minimum Bounding Rect of the node's children.
    protected Splitter splitter;
    protected List<Rectangle> children; // Children of this node.

    /**
     * Default constructor.
     *
     * @param m        the minimum elements that has to have the node.
     * @param M        the maximum elements that has to have the node.
     * @param splitter split heuristics.
     */
    public Node(int m, int M, Splitter splitter) {
        this.m = m;
        this.M = M;
        this.children = new ArrayList<>();
        this.MBR = null;
        this.splitter = splitter;
    }

    @Override
    public Data getMBR() {
        return this.MBR;
    }

    @Override
    public List<Data> search(Data C) {
        List<Data> ret = new ArrayList<>();
        for (Rectangle child : this.children) {
            ret.addAll(child.search(C));
        }
        return ret;
    }

    /**
     * Returns how many children has this node.
     *
     * @return children size.
     */
    public int getChildrenSize() {
        return this.children.size();
    }

    /**
     * Creates a "cloned" node with the same parameters.
     *
     * @return a new Node object set with the same parameters.
     */
    public Node newNode() {
        return new Node(this.m, this.M, this.splitter);
    }

    /**
     * Refresh the MBR. Called when a new child is added to this node.
     */
    protected void refreshMBR() {
        if (this.children.isEmpty()) {
            // If there is no child, the MBR is null.
            // It couldn't be no child because we have at least m children.
            this.MBR = null;
        } else {
            double xL, xR, yB, yT;
            xR = yT = Double.MIN_VALUE;
            xL = yB = Double.MAX_VALUE;
            for (Rectangle child : this.children) {
                Data mbr = child.getMBR();
                xL = Math.min(xL, mbr.getLeft());
                xR = Math.max(xR, mbr.getRight());
                yB = Math.min(yB, mbr.getBottom());
                yT = Math.max(yT, mbr.getTop());
            }

            // We have the maximum/minimum sides, create the MBR.
            try {
                this.MBR = new Data(xL, yT, xR, yB);
            } catch (GeneralException e) {
                // If xL==xR or yT==D there's no rectangle.
                this.MBR = null;
            }
        }
    }

    /**
     * Returns how much would increase the MBR's area if C is inserted.
     *
     * @param C data to be inserted.
     * @return how much would increase MBR's area.
     */
    public double deltaAreaQuery(Data C) {
        double xR = Math.max(this.MBR.getRight(), C.getRight());
        double xL = Math.min(this.MBR.getLeft(), C.getLeft());
        double yT = Math.max(this.MBR.getTop(), C.getTop());
        double yD = Math.min(this.MBR.getBottom(), C.getBottom());
        return ((xR - xL) * (yT - yD)) - this.MBR.getArea();
    }

    /**
     * Try to insert the rectangle C in a child of this sub-tree.
     *
     * @param C the rectangle to be inserted.
     * @return true if it is inserted in this sub-tree.
     */
    public boolean insert(Data C) throws GeneralException {
        // Data must be inserted in the sub-tree who's area increases the least.
        Node minNode = findInsertChild(C);

        // cond will be false when a successive node's children is empty. This should not happen.
        boolean cond;
        try {
            cond = minNode.insert(C);
        } catch (GeneralException e) { // manage overflow
            this.addChildren(minNode.split());
            this.children.remove(minNode);
            cond = true;
        }
        this.refreshMBR();
        return minNode != null && cond;
    }

    /**
     * Data must be inserted in the sub-tree who's area increases the least. If it is the case that
     * there are more than one, it is chosen who has the minimum area of them.
     */
    private Node findInsertChild(Data C) {
        Node minNode = null;
        double min = Double.MAX_VALUE;
        for (Rectangle element : this.children) {
            Node child = (Node) element;
            if (child.deltaAreaQuery(C) < min) {
                min = child.deltaAreaQuery(C);
                minNode = child;
            } else if (child.deltaAreaQuery(C) == min && (child.getMBR().getArea() < minNode.getMBR().getArea())) {
                minNode = child;
            }
        }
        return minNode;
    }

    /**
     * Adds a new child node. Must only be called by a splitter, as it might otherwise cause a node
     * overflow.
     *
     * @param child to be inserted.
     */
    protected void addChild(Rectangle child) {
        this.children.add(child);
        this.refreshMBR();
    }

    /**
     * Adds new nodes to this node's children.
     *
     * @param newNodes nodes to be inserted.
     * @throws GeneralException in case of this node's size is over M after insertion.
     */
    void addChildren(List<Rectangle> newNodes) throws GeneralException {
        this.children.addAll(newNodes);
        if (this.children.size() > this.M) {
            throw new GeneralException("addChildren: node overflow");
        }
        this.refreshMBR();
    }

    /**
     * Makes a split in case of overflow.
     *
     * @return a list with two nodes to replace this node.
     */
    protected List<Rectangle> split() {
        Node[] splitResult = this.splitter.split(this.children, this.newNode(), this.newNode());
        List<Rectangle> ret = new ArrayList<>();
        ret.add(splitResult[0]);
        ret.add(splitResult[1]);
        return ret;
    }

    /**
     * Determines the total number of Rectangle elements in the tree.
     *
     * @return the number of Rectangles.
     */
    public int rectangleCount() {
        int count = 1; // Itself
        for (Rectangle child : this.children) {
            count += ((Node) child).rectangleCount();
        }
        return count;
    }

    /**
     * Determines the number of Node objects in the subtree that has this node as root.
     *
     * @return the total number of nodes in the subtree.
     */
    public int nodeCount() {
        int count = 1; // starts with 1 to count itself
        for (Rectangle child : this.children) {
            count += ((Node) child).nodeCount();
        }
        return count;
    }

    /**
     * Determines the height of the node.
     *
     * @return the node's height.
     */
    public int height() throws GeneralException {
        int childHeight = ((Node) this.children.get(0)).height();
        for (Rectangle child : this.children) {
            if (((Node) child).height() != childHeight) {
                throw new GeneralException("Children with different heights");
            }
        }
        return 1 + childHeight; // as nodes must maintain the |children| >= m invariant, childHeight should be > 0
    }

    /**
     * Determines the total data rectangles that are in its sub-tree.
     *
     * @return the number of data rectangles in this sub-tree.
     */
    public int dataCount() {
        int count = 0; // Node does not have data.
        for(Rectangle child : this.children) {
            count += ((Node) child).dataCount();
        }
        return count;
    }
}
