/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.closer;

import java.sql.SQLException;
import java.sql.Statement;

public class StatementCloser implements ICloser {

    private final Statement statement;

    StatementCloser(final Statement statement) {
        this.statement = statement;
    }

    @Override
    public void close() throws SQLException {
        this.statement.close();
    }

}
