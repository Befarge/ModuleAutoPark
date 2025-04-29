package customException;

public class  CheckLicensePlateException extends CheckException {
    public CheckLicensePlateException (String message) {
        super(message);
    }
}