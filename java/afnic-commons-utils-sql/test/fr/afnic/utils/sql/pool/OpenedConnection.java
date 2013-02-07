package fr.afnic.utils.sql.pool;

import java.sql.SQLException;

public class OpenedConnection extends TestConnection {

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

}
