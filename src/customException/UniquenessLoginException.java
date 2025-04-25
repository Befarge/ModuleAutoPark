package customException;

public class UniquenessLoginException extends UniquenessException {
    public UniquenessLoginException(String message) {
        super(message);
    }
}
