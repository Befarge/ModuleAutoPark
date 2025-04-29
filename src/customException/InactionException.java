package customException;
import java.sql.SQLException;

public class InactionException extends SQLException {
    public InactionException (String message) {
        super(message);
    }
}
