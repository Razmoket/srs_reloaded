package fr.afnic.utils.sql.pool;

import java.sql.SQLException;

public class ClosedConnection extends TestConnection {

    @Override
    public boolean isClosed() throws SQLException {
        return true;
    }

}
