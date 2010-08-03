package der;

/**
 *
 * @author Marcius
 */
public class DerException extends RuntimeException {

    public DerException(String message) {
        super(message);
    }

    public DerException(Throwable cause) {
        super(cause);
    }

    public DerException(String message, Throwable cause) {
        super(message, cause);
    }
}
