package customException;

public class CheckPhoneException extends CheckException {
    public CheckPhoneException(String message) {
        super(message);
    }
}