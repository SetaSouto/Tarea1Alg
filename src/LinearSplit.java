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

        if (startingPair[0] == null || startingPair[1] == null) {
            throw new Error("Starting pair null.");
        }
        if (startingPair[0].equals(startingPair[1])) {
            throw new Error("Starting rectangles are the same.");
        }

        // Delete the pair from children:
        children.remove(startingPair[0]);
        children.remove(startingPair[1]);

        if (!(children.size() == (initChildrenSize - 2))) {
            throw new Error("The starting pair wasn't removed.");
        }

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
        if ((n1.getChildrenSize() + n2.getChildrenSize()) > initChildrenSize) {
            throw new Error("There are more children than in the beginning.");
        } else if ((n1.getChildrenSize() + n2.getChildrenSize()) < initChildrenSize) {
            throw new Error("There are less children than in the beginning.");
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
        Rectangle r1 = getMaxLeft(children);
        Rectangle r2 = getMinRight(children);
        if (r1.equals(r2)) {                // If they are the same
            children.remove(r1);            // Remove r1 from children
            r2 = getMinRight(children);     // Look for r2 in the list without r1
            children.add(r1);               // Add r1 again
        }
        return new Rectangle[]{r1, r2};
    }

    /**
     * Determines the rectangle with the left side most at the right.
     *
     * @param candidates list with all the rectangles.
     * @return a rectangle with its left side is the most at the right.
     */
    private Rectangle getMaxLeft(List<Rectangle> candidates) {
        double max_xL = Double.MIN_VALUE;
        Rectangle ret = null;
        for (Rectangle candidate : candidates) {
            // If its left side if at the righ of max_xL
            if (candidate.getMBR().getLeft() > max_xL) {
                max_xL = candidate.getMBR().getLeft();
                ret = candidate;
            }
        }
        return ret;
    }

    /**
     * Determines the rectangle with the right side most at the left.
     *
     * @param candidates list with all the rectangles.
     * @return a rectangle with its right side most at the left.
     */
    private Rectangle getMinRight(List<Rectangle> candidates) {
        double min_xR = Double.MAX_VALUE;
        Rectangle ret = null;
        for (Rectangle candidate : candidates) {
            // If its right side is at the left of the min_xR
            if (candidate.getMBR().getRight() < min_xR) {
                min_xR = candidate.getMBR().getRight();
                ret = candidate;
            }
        }
        return ret;
    }

    /**
     * Return two rectangles that are furthest away of each other in Y.
     *
     * @param children list with rectangles.
     * @return two rectangles, first is with the maximum yB, second is with the minimum yT
     */
    Rectangle[] getPairY(List<Rectangle> children) {
        Rectangle r1 = getMaxBottom(children);
        Rectangle r2 = getMinTop(children);
        if (r1.equals(r2)) {            // They cannot be the same
            children.remove(r1);        // Remove r1 from children
            r2 = getMinTop(children);   // Look for r2 in children without r1
            children.add(r1);           // Add r1 again
        }
        return new Rectangle[]{r1, r2};
    }

    /**
     * Determines the rectangle with its bottom most up.
     *
     * @param candidates all the rectangles.
     * @return the rectangle with its bottom side most up.
     */
    private Rectangle getMaxBottom(List<Rectangle> candidates) {
        double max_yB = Double.MIN_VALUE;
        Rectangle ret = null;
        for (Rectangle candidate : candidates) {
            if (candidate.getMBR().getBottom() > max_yB) {  // If its right side is at the right of the current max
                max_yB = candidate.getMBR().getBottom();
                ret = candidate;
            }
        }
        return ret;
    }

    /**
     * Determines the rectangle with its top side most down.
     *
     * @param candidates all the rectangles.
     * @return the rectangle with its top side most down.
     */
    private Rectangle getMinTop(List<Rectangle> candidates) {
        double min_yT = Double.MAX_VALUE;
        Rectangle ret = null;
        for (Rectangle candidate : candidates) {
            if (candidate.getMBR().getTop() < min_yT) {
                min_yT = candidate.getMBR().getTop();
                ret = candidate;
            }
        }
        return ret;
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
