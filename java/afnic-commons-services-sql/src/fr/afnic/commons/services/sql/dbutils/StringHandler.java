/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.ResultSetHandler;

public class StringHandler implements ResultSetHandler<String> {

    @Override
    public String handle(final ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

}
