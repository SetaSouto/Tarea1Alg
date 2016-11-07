import java.util.List;

/**
 * Implements LinearSplit heuristic.
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
        int initChildrenSize = children.size(); // To check errors.

        Rectangle[] startingPair = this.mostSeparated(children);

        // Delete the pair from children:
        children.remove(startingPair[0]);
        children.remove(startingPair[1]);

        // How many rectangles are:
        int countRest = children.size();

        // Add r1 to n1 add r2 to n2:
        n1.addChild(startingPair[0]);
        n2.addChild(startingPair[1]);

        // Iterate over the children
        for (Rectangle child : children) {
            if (n1.deltaAreaQuery(child.getMBR()) < n2.deltaAreaQuery(child.getMBR())) {
                // n1's MBR must increase less than n2's MBR
                if (countRest <= (m - n2.getChildrenSize())) {
                    // There are just the elements that n2 needs to keep the invariant of have at least m
                    // children
                    n2.addChild(child);
                } else {
                    n1.addChild(child);
                }
            } else {
                if (countRest <= (m - n1.getChildrenSize())) {
                    n1.addChild(child);
                } else {
                    n2.addChild(child);
                }
            }
            // Delete the rectangle recently added:
            countRest--;
        }

        if (n1.getChildrenSize() < m || n2.getChildrenSize() < m) {
            throw new Error("Invariant m broken.");
        }
        if ((n1.getChildrenSize() + n2.getChildrenSize()) != initChildrenSize) {
            throw new Error("There are more children than in the beginning.");
        }

        return new Node[]{n1, n2};
    }

    private Rectangle[] mostSeparated(List<Rectangle> children) {
        Rectangle[] pairX = this.getPairX(children);
        Rectangle[] pairY = this.getPairY(children);
        // (max xL - min xR)/(range X):
        double distXNorm =
                (pairX[0].getMBR().getLeft() - pairX[1].getMBR().getRight()) / (this.getRangeX(children));
        // (max yB - min yT)/(range Y):
        double distYNorm =
                (pairY[0].getMBR().getBottom() - pairY[1].getMBR().getTop()) / (this.getRangeY(children));

        return distXNorm > distYNorm ? pairX : pairY;
    }

    /**
     * Returns two rectangles that are furthest away of each other in X.
     *
     * @param children list with the rectangles.
     * @return two rectangles, first is with the max xL, second is with the minimum xR
     */
    Rectangle[] getPairX(List<Rectangle> children) {
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
        return new Rectangle[]{r1, r2};
    }

    /**
     * Return two rectangles that are furthest away of each other in Y.
     *
     * @param children list with rectangles.
     * @return two rectangles, first is with the maximum yB, second is with the minimum yT
     */
    Rectangle[] getPairY(List<Rectangle> children) {
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
        return new Rectangle[]{r1, r2};
    }

    /**
     * Returns the range in X.
     *
     * @param children list with the rectangles.
     * @return range in X.
     */
    double getRangeX(List<Rectangle> children) {
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
    double getRangeY(List<Rectangle> children) {
        double max_yT = Double.MIN_VALUE;
        double min_yB = Double.MAX_VALUE;
        for (Rectangle child : children) {
            max_yT = Math.max(child.getMBR().getTop(), max_yT);
            min_yB = Math.min(child.getMBR().getBottom(), min_yB);
        }
        return max_yT - min_yB;
    }
}
