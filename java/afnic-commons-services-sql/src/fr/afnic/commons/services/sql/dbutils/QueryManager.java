/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.dbutils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.sql.query.ParameterizedQuery;
import fr.afnic.utils.sql.ISqlConnectionFactory;

/**
 * Simplifie l'utilisation de DB-utils.
 * 
 * TODO devrait peut-eter etre un QueryRunner customisé...
 * 
 * @author ginguene
 * 
 */
public class QueryManager {

    private final ISqlConnectionFactory sqlConnectionFactory;

    public QueryManager(final ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    /**
     * Execute une requete et map le résultat dans un objet grace au handler passé en parametre.
     * 
     * @param <T>
     * @param sql
     * @param handler
     * @param params
     * @return
     * @throws ConnectionException
     * @throws SQLException
     * @throws InvalidFormatException
     */
    public <T> T executeQuery(final ParameterizedQuery query, final ResultSetHandler<T> handler) throws ConnectionException, SQLException, InvalidFormatException {
        return this.executeQuery(query.toSql(), handler, query.getParameters());
    }

    /**
     * Execute une requete et map le résultat dans un objet grace au handler passé en parametre.
     * 
     * @param <T>
     * @param sql
     * @param handler
     * @param params
     * @return
     * @throws ConnectionException
     * @throws SQLException
     */
    public <T> T executeQuery(final String sql, final ResultSetHandler<T> handler, final Object... params) throws ConnectionException, SQLException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        T result = new QueryRunner().query(connection, sql, handler, params);
        connection.close();
        return result;

    }

    /**
     * Réalise une requete d'update
     * 
     * @param sql
     * @param params
     * @return
     * @throws ConnectionException
     * @throws SQLException
     */
    public int executeUpdate(final String sql, final Object... params) throws ConnectionException, SQLException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        int result = new QueryRunner().update(connection, sql, params);
        connection.close();
        return result;

    }

}
