package customException;

public class UnsuccessfulValidationException extends Exception {
    public UnsuccessfulValidationException (String message) {
        super(message);
    }
}
