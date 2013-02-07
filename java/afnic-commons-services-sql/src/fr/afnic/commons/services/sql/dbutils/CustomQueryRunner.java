/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.dbutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import fr.afnic.utils.closer.CloseException;
import fr.afnic.utils.closer.Closer;

/**
 * QueryRunner qui ne fait pas Mapping lors du query si il y a plus de maxResultCount résultats trouvés.
 * 
 * Après tests: gain de temps ridicule par rapport au fait d'utiliser le QueryRunner de base:<br/>
 * Pour 1000 résultats d'un seachTicket, l'utilisation d'un QueryRunner prend 23 sec, celle d'un CustomQueryRunner prend 22 sec.
 * 
 * @author ginguene
 * 
 */
public class CustomQueryRunner extends QueryRunner {

    private int maxResultCount = -1;

    public CustomQueryRunner(final int maxResultCount) {
        this.maxResultCount = maxResultCount;
    }

    @Override
    public <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rsh, final Object... params) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet wrapResultset = null;
        ResultSet resultset = null;
        T result = null;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);

            resultset = stmt.executeQuery();
            if (this.maxResultCount > 0) {
                int resultCount = 0;
                while (resultset.next()) {
                    resultCount++;
                }

                if (resultCount > this.maxResultCount) {
                    throw new RuntimeException("bou");
                } else {
                    resultset.first();
                }
            }

            wrapResultset = this.wrap(stmt.executeQuery());

            result = rsh.handle(wrapResultset);

        } catch (final SQLException e) {
            e.printStackTrace();
            this.rethrow(e, sql, params);
        } finally {
            try {
                Closer.close(stmt, wrapResultset, resultset);
            } catch (final CloseException e) {
                e.printStackTrace();
            }

        }
        return result;
    }
}
