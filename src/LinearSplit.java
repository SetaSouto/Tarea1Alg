import java.util.List;

/**
 * Implements LinearSplit hueristic.
 *
 * @author souto
 */
public class LinearSplit implements Splitter {
    int m, M;

    /**
     * Constructor.
     *
     * @param m indicates minimum size of a node.
     * @param M indicates the maximum size of a node.
     */
    public LinearSplit(int m, int M) {
        this.m = m;
        this.M = M;
    }

    @Override
    public Node[] split(List<Rectangle> children, Node n1, Node n2) {
        Rectangle[] pairX = this.getPairX(children);
        Rectangle[] pairY = this.getPairY(children);
        // (max xL - min xR)/(range X):
        double distXNorm =
                (pairX[0].getMBR().getLeft() - pairX[1].getMBR().getRight()) / (this.getRangeX(children));
        // (max yB - min yT)/(range Y):
        double distYNorm =
                (pairY[0].getMBR().getBottom() - pairY[1].getMBR().getTop()) / (this.getRangeY(children));

        // Select the pair with the biggest distance.
        Rectangle r1;
        Rectangle r2;
        if (distXNorm > distYNorm) {
            r1 = pairX[0];
            r2 = pairY[1];
        } else {
            r1 = pairY[0];
            r2 = pairY[1];
        }

        // Delete the pair from children:
        children.remove(r1);
        children.remove(r2);

        // How many rectangles are:
        int countRest = children.size();

        // Add r1 to n1 add r2 to n2:
        n1.insertChild(r1);
        n2.insertChild(r2);

        // Iterate over the children
        for (Rectangle child : children) {
            if (n1.deltaAreaQuery(child.getMBR()) < n2.deltaAreaQuery(child.getMBR())) {
                // n1's MBR must increase less than n2's MBR
                if (countRest <= (m - n2.getChildrenSize())) {
                    // There are just the elements that n2 needs to keep the invariant of have at least m
                    // children
                    n2.insertChild(child);
                } else {
                    n1.insertChild(child);
                }
            } else {
                if (countRest <= (m - n1.getChildrenSize())) {
                    n1.insertChild(child);
                } else {
                    n2.insertChild(child);
                }
            }
            // Delete the rectangle recently added:
            countRest--;
        }

        if (n1.getChildrenSize() < m || n2.getChildrenSize() < m) {
            System.out.println("ERROR: Invariant m broken.");
            ;
        }

        Node[] ret = {n1, n2};
        return ret;
    }

    /**
     * Returns two rectangles that are fardest away of each other in X.
     *
     * @param children list with the rectangles.
     * @return two rectangles, first is with the max xL, second is with the minimum xR
     */
    private Rectangle[] getPairX(List<Rectangle> children) {
        double max_xL = Double.MIN_VALUE;
        double min_xR = Double.MAX_VALUE;
        Rectangle r1 = null;
        Rectangle r2 = null;
        for (Rectangle child : children) {
            // If its left side if at the right of max_xL
            if (child.getMBR().getLeft() > max_xL) {
                max_xL = child.getMBR().getLeft();
                r1 = child;
            }
            // If its right side is at the left of min_xR
            if (child.getMBR().getRight() < min_xR) {
                min_xR = child.getMBR().getRight();
                r2 = child;
            }
        }
        Rectangle[] ret = {r1, r2};
        return ret;
    }

    /**
     * Return two rectangles that are fardest away of each other in Y.
     *
     * @param children list with rectangles.
     * @return two rectangles, first is with the maximum yB, second is with the minimum yT
     */
    private Rectangle[] getPairY(List<Rectangle> children) {
        double max_yB = Double.MIN_VALUE;
        double min_yT = Double.MAX_VALUE;
        Rectangle r1 = null;
        Rectangle r2 = null;
        for (Rectangle child : children) {
            // If its bottom side is over of max_yB
            if (child.getMBR().getBottom() > max_yB) {
                max_yB = child.getMBR().getBottom();
                r1 = child;
            }
            // If its top side is under the of min_yT
            if (child.getMBR().getRight() < min_yT) {
                min_yT = child.getMBR().getRight();
                r2 = child;
            }
        }
        Rectangle[] ret = {r1, r2};
        return ret;
    }

    /**
     * Returns the range in X.
     *
     * @param children list with the rectangles.
     * @return range in X.
     */
    private double getRangeX(List<Rectangle> children) {
        double max_xR = Double.MIN_VALUE;
        double min_xL = Double.MAX_VALUE;
        for (Rectangle child : children) {
            max_xR = Math.max(child.getMBR().getRight(), max_xR);
            min_xL = Math.min(child.getMBR().getLeft(), min_xL);
        }
        return max_xR - min_xL;
    }

    /**
     * Returns the range in Y.
     *
     * @param children list with rectangles.
     * @return range in Y.
     */
    private double getRangeY(List<Rectangle> children) {
        double max_yT = Double.MIN_VALUE;
        double min_yB = Double.MAX_VALUE;
        for (Rectangle child : children) {
            max_yT = Math.max(child.getMBR().getTop(), max_yT);
            min_yB = Math.min(child.getMBR().getBottom(), min_yB);
        }
        return max_yT - min_yB;
    }

}
