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
   * Returns an array with the top-left corner (x,y).
   * 
   * @return an array (x,y).
   */
  public double[] getTopLeftCorner() {
    double[] ret = new double[2];
    ret[0] = this.xL;
    ret[1] = this.yT;
    return ret;
  }

  /**
   * Returns an array with the bottom-right corner (x,y).
   * 
   * @return an array (x,y).
   */
  public double[] getBottomRightCorner() {
    double[] ret = new double[2];
    ret[0] = this.xR;
    ret[1] = this.yD;
    return ret;
  }
  /**
   * Returns the area of the rectangle.
   * @return the area of the rectangle.
   */
  public double getArea() {
    double width = this.xR - this.xL;
    double height = this.yT - this.yD;
    return (width * height);
  }
}
