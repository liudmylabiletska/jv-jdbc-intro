package mate.academy.model;

import java.sql.SQLException;

public class DataProcessingException extends Throwable {
    public DataProcessingException(String failedToEstablishDatabaseConnection, SQLException e) {
    }
}
