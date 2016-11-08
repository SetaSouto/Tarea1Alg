import java.util.ArrayList;
import java.util.List;

/**
 * Data is defined by two pairs (x,y).
 *
 * @author souto
 */
public class Data implements Rectangle, java.io.Serializable {
    private double xL, xR, yT, yD; // xLeft, xRight, yTop, yDown.
    private String path;

    /**
     * Constructor.
     *
     * @param x1 the x of the top-left corner.
     * @param y1 the y of the top-left corner.
     * @param x2 the x of the bottom-right corner.
     * @param y2 the y of the bottom-right corner.
     */
    public Data(double x1, double y1, double x2, double y2, String path) throws GeneralException {
        if (x1 == x2 || y1 == y2) {
            throw new GeneralException("Data with no area.");
        }
        this.xL = Math.min(x1, x2);
        this.xR = Math.max(x1, x2);
        this.yD = Math.min(y1, y2);
        this.yT = Math.max(y1, y2);
        this.path = path;
    }

    @Override
    public Data getMBR() {
        return this;
    }

    @Override
    public List<Data> search(Data C) {
        List<Data> ret = new ArrayList<>();
        if (this.intersect(C)) {
            ret.add(this);
        }
        return ret;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * Returns the value of the left side.
     *
     * @return xL.
     */
    public double getLeft() {
        return this.xL;
    }

    /**
     * Returns the value of the right side;
     *
     * @return the value of the right side;
     */
    public double getRight() {
        return this.xR;
    }

    /**
     * Returns the value of the top side.
     *
     * @return the value of the top side.
     */
    public double getTop() {
        return this.yT;
    }

    /**
     * Returns the value of the bottom side.
     *
     * @return the value of the bottom side.
     */
    public double getBottom() {
        return this.yD;
    }

    /**
     * Returns the area of the rectangle.
     *
     * @return the area of the rectangle.
     */
    public double getArea() {
        return ((this.xR - this.xL) * (this.yT - this.yD));
    }

    /**
     * Returns true if C intersects with this rectangle.
     *
     * @param C the rectangle to check.
     * @return true if C intersects with this rectangle.
     */
    public boolean intersect(Data C) {
        // If someone of the following conditions is true, they are not intersected.
        return !(cond1(C) || cond2(C) || cond3(C) || cond4(C));
    }

    /**
     * Returns true if C's right side is at the left of the left side of this Data.
     *
     * @param C the rectangle to be compared.
     * @return true if C's right side is at the left of the left side of this Data.
     */
    private boolean cond1(Data C) {
        return (C.getRight() < this.xL);
    }

    /**
     * Returns true if C's left side is at the right of the right side of this rectangle.
     */
    private boolean cond2(Data C) {
        return (C.getLeft() > this.xR);
    }

    /**
     * Returns true if C's bottom side is over the top side of this.
     */
    private boolean cond3(Data C) {
        return (C.getBottom() > this.yT);
    }

    /**
     * Returns true if C's top side is below of this bottom side.
     */
    private boolean cond4(Data C) {
        return (C.getTop() < this.yD);
    }

    /**
     * Return true if this is equal to C (dimensions are tha same and they are in the same
     * position).
     */
    public boolean equals(Data C) {
        return (this.getLeft() == C.getLeft() && this.getRight() == C.getRight() &&
                this.getTop() == C.getTop() && this.getBottom() == C.getBottom());
    }
}
