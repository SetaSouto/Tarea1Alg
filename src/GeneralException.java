/**
 * A general exception for the homework.
 * @author souto
 *
 */
public class GeneralException extends Exception {
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * @param message to be shown by the exception.
   */
  public GeneralException(String message) {
    super(message);
  }
  
}
