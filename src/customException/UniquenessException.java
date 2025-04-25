package customException;
import java.sql.SQLException;

public class UniquenessException extends SQLException {
    public UniquenessException (String message) {
        super(message);
    }
}
