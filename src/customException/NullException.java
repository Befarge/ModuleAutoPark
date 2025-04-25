package customException;
import java.sql.SQLException;

public class NullException extends SQLException {
    public NullException(String message) {
        super(message);
    }
}