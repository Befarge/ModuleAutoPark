package customException;
import java.sql.SQLException;

public class CheckException extends SQLException {
    public CheckException (String message) {
        super(message);
    }
}
