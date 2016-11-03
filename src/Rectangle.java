import java.util.ArrayList;
import java.util.List;

/**
 * Rectangle is defined by two pairs (x,y).
 * 
 * @author souto
 *
 */
public class Rectangle {
  private double xL, xR, yT, yD; // xLeft, xRight, yTop, yDown.

  /**
   * Constructor.
   * 
   * @param x1 the x of the top-left corner.
   * @param y1 the y of the top-left corner.
   * @param x2 the x of the bottom-right corner.
   * @param y2 the y of the bottom-right corner.
   * @throws GeneralException
   */
  public Rectangle(double x1, double y1, double x2, double y2) throws GeneralException {
    if (x1 == x2 || y1 == y2) {
      throw new GeneralException("Rectangle with no area.");
    }
    if (x1 < x2) {
      // In this case x1 is at the left of x2, is correct.
      this.xL = x1;
      this.xR = x2;
    } else {
      this.xL = x2;
      this.xR = x1;
    }
    if (y1 > y2) {
      // In this case y1 is over y2, is correct.
      this.yT = y1;
      this.yD = y2;
    } else {
      this.yT = y2;
      this.yD = y1;
    }
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
    double width = this.xR - this.xL;
    double height = this.yT - this.yD;
    return (width * height);
  }

  /**
   * Returns true if C intersects with this rectangle.
   * 
   * @param C the rectangle to check.
   * @return true if C intersects with this rectangle.
   */
  public boolean intersect(Rectangle C) {
    // If someone of the following conditions is true, they are not intersected.
    return !(cond1(C) || cond2(C) || cond3(C) || cond4(C));
  }

  /**
   * Returns true if C's right side is at the left of the left side of this Rectangle.
   * 
   * @param C the rectangle to be compared.
   * @return true if C's right side is at the left of the left side of this Rectangle.
   */
  private boolean cond1(Rectangle C) {
    double cright = C.getRight();
    return (cright < this.xL);
  }

  /**
   * Returns true if C's left side is at the right of the right side of this rectangle.
   * 
   * @param C
   * @return
   */
  private boolean cond2(Rectangle C) {
    double cleft = C.getLeft();
    return (cleft > this.xR);
  }

  /**
   * Returns true if C's bottom side is over the top side of this.
   * 
   * @param C
   * @return
   */
  private boolean cond3(Rectangle C) {
    double cbottom = C.getBottom();
    return (cbottom > this.yT);
  }

  /**
   * Returns true if C's top side is below of this bottom side.
   * 
   * @param C
   * @return
   */
  public boolean cond4(Rectangle C) {
    double ctop = C.getTop();
    return (ctop < this.yD);
  }

  /**
   * Returns a list with this rectangle included if intersects with C.
   * 
   * @param C rectangle to check intersection.
   * @return a list with this rectangle included if intersects with C.
   */
  public List<Rectangle> search(Rectangle C) {
    List<Rectangle> ret = new ArrayList<Rectangle>();
    if (this.intersect(C)) {
      ret.add(this);
    }
    return ret;
  }
}
