/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.closer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe chargée de fermer un resultset.
 * 
 * @author ginguene
 * 
 */
public class ResultSetCloser implements ICloser {

    private final ResultSet resultSet;

    ResultSetCloser(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public void close() throws SQLException {
        this.resultSet.close();
    }

}
