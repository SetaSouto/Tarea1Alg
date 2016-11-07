import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an R-Tree Node.
 */
public class Node implements Rectangle {
    protected int m, M;
    private Data MBR; // Minimum Bounding Rect of the node's childrenPaths.
    protected Splitter splitter;
    protected List<String> childrenPaths; // Children of this node.
    private String path;

    /**
     * Default constructor.
     *
     * @param m        the minimum elements that has to have the node.
     * @param M        the maximum elements that has to have the node.
     * @param splitter split heuristics.
     */
    public Node(int m, int M, Splitter splitter, String path) {
        this.m = m;
        this.M = M;
        this.childrenPaths = new ArrayList<>();
        this.MBR = null;
        this.splitter = splitter;
        this.path = path;
    }

    @Override
    public Data getMBR() {
        return this.MBR;
    }

    @Override
    public List<Data> search(Data C) {
        List<Data> ret = new ArrayList<>();
        Rectangle child;
        for (String childPath : this.childrenPaths) {
            child = RTree.getObj(childPath);
            if (child.getMBR().intersect(C.getMBR())) ret.addAll(child.search(C));
        }
        return ret;
    }

    @Override
    public int accessCountSearch(Data C) {
        int count = 1;
        Rectangle child;
        for (String childPath : this.childrenPaths) {
            child = RTree.getObj(childPath);
            if (child.getMBR().intersect(C.getMBR())) count += child.accessCountSearch(C);
        }
        return count;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * Returns how many childrenPaths has this node.
     *
     * @return childrenPaths size.
     */
    public int getChildrenSize() {
        return this.childrenPaths.size();
    }

    /**
     * Creates a "cloned" node with the same parameters.
     *
     * @return a new Node object set with the same parameters.
     */
    public Node newNode(String path) {
        return new Node(this.m, this.M, this.splitter, path);
    }

    /**
     * Refresh the MBR. Called when a new child is added to this node.
     */
    protected void refreshMBR() {
        if (this.childrenPaths.isEmpty()) {
            // If there is no child, the MBR is null.
            // It couldn't be no child because we have at least m childrenPaths.
            this.MBR = null;
        } else {
            double xL, xR, yB, yT;
            xR = yT = Double.MIN_VALUE;
            xL = yB = Double.MAX_VALUE;
            Rectangle child;
            for (String childPath : this.childrenPaths) {
                child = RTree.getObj(childPath);
                Data mbr = child.getMBR();
                xL = Math.min(xL, mbr.getLeft());
                xR = Math.max(xR, mbr.getRight());
                yB = Math.min(yB, mbr.getBottom());
                yT = Math.max(yT, mbr.getTop());
            }

            // We have the maximum/minimum sides, create the MBR.
            try {
                this.MBR = new Data(xL, yT, xR, yB, this.MBR.getPath());
                RTree.save(this.MBR, this.MBR.getPath());
            } catch (GeneralException e) {
                // If xL==xR or yT==D there's no rectangle.
                this.MBR = null;
            }
        }
    }

    /**
     * Compares the actual MBR with the new rectangle and updates its parameters.
     *
     * @param C new rectangle inserted.
     */
    private void updateMBR(Rectangle C) {
        Data newMBR = C.getMBR();
        double xL = Math.min(this.MBR.getLeft(), newMBR.getLeft());
        double xR = Math.max(this.MBR.getRight(), newMBR.getRight());
        double yB = Math.min(this.MBR.getBottom(), newMBR.getBottom());
        double yT = Math.max(this.MBR.getTop(), newMBR.getTop());
        try {
            this.MBR = new Data(xL, yT, xR, yB, this.MBR.getPath());
        } catch (GeneralException e) {
            e.printStackTrace();
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

        // cond will be false when a successive node's childrenPaths is empty. This should not happen.
        boolean cond;
        try {
            cond = minNode.insert(C);
            this.updateMBR(C);
        } catch (GeneralException e) { // manage overflow
            //this.childrenPaths.remove(minNode); // Delete the node of overflow // Update: Remove not necessary
            Rectangle[] newNodes = minNode.split();
            this.addChild(newNodes[0]);
            this.addChild(newNodes[1]);
            cond = true;
        }
        return minNode != null && cond;
    }

    /**
     * Data must be inserted in the sub-tree who's area increases the least. If it is the case that
     * there are more than one, it is chosen who has the minimum area of them.
     */
    private Node findInsertChild(Data C) {
        Node minNode = null;
        double min = Double.MAX_VALUE;
        Rectangle element;
        for (String elementPath : this.childrenPaths) {
            element = RTree.getObj(elementPath);
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
     * @param newChild to be inserted.
     */
    protected void addChild(Rectangle newChild) throws GeneralException {
        this.updateMBR(newChild);
        String newChildPath = newChild.getPath();
        if (!this.childrenPaths.contains(newChildPath)) {
            this.childrenPaths.add(newChildPath);
        }
        if (this.childrenPaths.size() > this.M) {
            throw new GeneralException("Overflow");
        }
    }

    /**
     * Makes a split in case of overflow.
     *
     * @return the path to the new node created during the split.
     */
    protected Rectangle[] split() {
        String newPath = RTree.getNewPath();
        Node[] splitResult = this.splitter.split(this.childrenPaths, this.newNode(this.path), this.newNode(newPath));
        RTree.save(splitResult[0], this.path);
        RTree.save(splitResult[1], newPath);
        return splitResult;
    }

    /**
     * Determines the total number of Rectangle elements in the tree.
     *
     * @return the number of Rectangles.
     */
    public int rectangleCount() {
        int count = 1; // Itself
        Rectangle child;
        for (String childPath : this.childrenPaths) {
            child = RTree.getObj(childPath);
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
        Rectangle child;
        for (String childPath : this.childrenPaths) {
            child = RTree.getObj(childPath);
            count += ((Node) child).nodeCount();
        }
        return count;
    }

    /**
     * Determines the height of the node.
     *
     * @return the node's height.
     */
    public int height() {
        int childHeight = ((Node) RTree.getObj(this.childrenPaths.get(0))).height();
        Rectangle child;
        for (String childPath : this.childrenPaths) {
            child = RTree.getObj(childPath);
            if (((Node) child).height() != childHeight) {
                throw new Error("Children with different heights");
            }
        }
        return 1 + childHeight; // as nodes must maintain the |childrenPaths| >= m invariant, childHeight should be > 0
    }

    /**
     * Determines the total data rectangles that are in its sub-tree.
     *
     * @return the number of data rectangles in this sub-tree.
     */
    public int dataCount() {
        int count = 0; // Node does not have data.
        Rectangle child;
        for (String childPath : this.childrenPaths) {
            child = RTree.getObj(childPath);
            count += ((Node) child).dataCount();
        }
        return count;
    }
}
