import java.util.ArrayList;
import java.util.List;

/**
 * Interface of a LinearSplitNode. Can be an intern LinearSplitNode (LinearSplitNode Class) or a
 * leaf (Data Class).
 *
 * @author souto
 */
public class Node implements Rectangle {
    protected int m, M;
    private Data MBR; // Minimum Bounding Rect of the node's children.
    protected Splitter splitter;
    protected List<Rectangle> children; // Children of this node.

    /**
     * Constructor.
     *
     * @param m        the minimum elements that has to have the node.
     * @param M        the maximum elements that has to have the node.
     * @param splitter herustic to make the split.
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

    @Override
    public List<Data> search(Data C) {
        List<Data> ret = new ArrayList<>();
        for (Rectangle child : this.children) {
            ret.addAll(child.search(C));
        }
        return ret;
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
        Node minNode = null;
        double min = Double.MAX_VALUE;
        // Find insert point.
        for (Rectangle element : this.children) {
            Node child = (Node) element;
            if (child.deltaAreaQuery(C) < min) {
                min = child.deltaAreaQuery(C);
                minNode = child;
            }
        }
        // cond will be false when an successive node's children is empty. This should not happen.
        boolean cond;
        try {
            cond = minNode.insert(C);
        } catch (GeneralException e) { // manage overflow
            this.insertChildren(minNode.split());
            this.children.remove(minNode);
            cond = true;
        }
        this.refreshMBR();
        return min != Double.MAX_VALUE && cond;
    }

    /**
     * Insert new nodes to this node's children.
     *
     * @param newNodes nodes to be inserted.
     * @throws GeneralException in case of this node's size is over M after insertion.
     */
    private void insertChildren(List<? extends Node> newNodes) throws GeneralException {
        this.children.addAll(newNodes);
        if (this.children.size() > this.M) {
            throw new GeneralException("Node overflow");
        }
    }

    /**
     * Insert a child in this node.
     *
     * @param child to be inserted.
     * @throws GeneralException in case of overflow.
     */
    public void insertChild(Rectangle child) {
        this.children.add(child);
        this.refreshMBR();
        // Only called by Splitter, making a split is impossible to broke M invariant.
        // if (this.children.size() > this.M) {
        // throw new GeneralException("Node overflow");
        // }
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
     * Makes a split in case of overflow.
     *
     * @return a list with two nodes to replace this node.
     */
    private List<Node> split() {
        Node[] splitResult = this.splitter.split(this.children, new Node(this.m, this.M, this.splitter),
                new Node(this.m, this.M, this.splitter));
        List<Node> ret = new ArrayList<Node>();
        ret.add(splitResult[0]);
        ret.add(splitResult[1]);
        return ret;
    }

}
