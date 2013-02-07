package fr.afnic.commons.services.sql.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.ResultSetProcessor;
import fr.afnic.commons.services.sql.converter.SqlConverterFacade;
import fr.afnic.commons.services.sql.query.SimpleQuery;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;

public class QueryStatementManagement {
    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(QueryStatementManagement.class);

    private ISqlConnectionFactory sqlConnectionFactory = null;

    private Connection connection = null;

    public QueryStatementManagement(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public QueryStatementManagement() {
    }

    public PreparedStatement initializeStatement(Connection connection, String sqlQuery) throws ServiceException {
        try {
            this.connection = connection;
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            return preparedStatement;
        } catch (SQLException e) {
            QueryStatementManagement.LOGGER.error("initializeStatement(" + sqlQuery + ") failed ", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public PreparedStatement initializeStatement(String sqlQuery) throws ServiceException {

        try {
            if (this.connection != null) {
                throw new IllegalArgumentException("Cannot call initializeStatement twice because it override the connection which cannot be closed later.");
            }
            this.connection = this.sqlConnectionFactory.createConnection();
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            return preparedStatement;
        } catch (SQLException e) {
            QueryStatementManagement.LOGGER.error("initializeStatement(" + sqlQuery + ") failed ", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void executeQuery(final ResultSetProcessor<?> processor, final SimpleQuery query) throws SQLException, ServiceException {
        this.executeQuery(processor, query.toString(), query.getParameters());
    }

    public static String getCharValue(boolean bool) {
        return bool ? "1" : "0";
    }

    public static boolean getBooleanValue(String character) {
        if ("0".equals(character)) {
            return false;
        } else if ("1".equals(character)) {
            return true;
        } else {
            throw new IllegalArgumentException("A boolean can only have two values.");
        }
    }

    public int executeStatement(PreparedStatement preparedStatement) throws ServiceException {
        int ret = 0;
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ret = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            return ret;
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        } finally {
            this.closeConnection();
        }
    }

    public String executeStatementAsString(PreparedStatement preparedStatement) throws ServiceException {
        String ret = "";
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ret = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
            return ret;
        } catch (SQLException e) {
            QueryStatementManagement.LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            this.closeConnection();
        }
    }

    public List<String> executeStatementAsStringList(PreparedStatement preparedStatement) throws ServiceException {
        List<String> list = new ArrayList<String>();
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
            resultSet.close();
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            QueryStatementManagement.LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            this.closeConnection();
        }
    }

    public List<Integer> executeStatementAsIntegerList(PreparedStatement preparedStatement) throws ServiceException {
        List<Integer> list = new ArrayList<Integer>();
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getInt(1));
            }
            resultSet.close();
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            QueryStatementManagement.LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            this.closeConnection();
        }
    }

    public java.util.Date executeStatementAsDate(PreparedStatement preparedStatement) throws ServiceException {
        java.util.Date ret = null;
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getDate(1) != null) {
                    ret = new java.util.Date(resultSet.getDate(1).getTime());
                }
            }
            resultSet.close();
            preparedStatement.close();
            return ret;
        } catch (SQLException e) {
            QueryStatementManagement.LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            this.closeConnection();
        }
    }

    public int executeStatementAsInt(PreparedStatement preparedStatement) throws ServiceException {
        int ret = -1;
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ret = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            return ret;
        } catch (SQLException e) {
            QueryStatementManagement.LOGGER.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        } finally {
            this.closeConnection();
        }
    }

    public <OBJECT extends Object> OBJECT executeStatement(Class<OBJECT> returnKind, PreparedStatement preparedStatement, UserId userId, TldServiceFacade tld) throws ServiceException {

        List<OBJECT> executeStatementList = this.executeStatementList(returnKind, preparedStatement, userId, tld);
        if (executeStatementList.isEmpty()) {
            return null;
        }
        return executeStatementList.get(0);
    }

    public <OBJECT extends Object> List<OBJECT> executeStatementList(Class<OBJECT> returnKind, PreparedStatement preparedStatement, UserId userId, TldServiceFacade tld) throws ServiceException {

        ArrayList<OBJECT> ret = new ArrayList<OBJECT>();
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (returnKind != null) {
                while (resultSet.next()) {
                    //Enclenche l'ouverture de connection pour les sous opérations d'une qualification par exemple
                    ret.add(SqlConverterFacade.convert(resultSet, returnKind, userId, tld));
                }

            }
            resultSet.close();
            preparedStatement.close();
            return ret;
        } catch (Exception e) {
            throw new ServiceException("executeStatementList() failed with connection " + this.getConnectionInfo(), e);
        } finally {
            this.closeConnection();
        }
    }

    private String getConnectionInfo() {
        if (this.connection == null) {
            return "[vide]";
        } else {
            return this.connection.toString();
        }
    }

    public void closeConnection() throws ServiceException {
        this.sqlConnectionFactory.closeConnection(this.connection);
    }

    /**
     * Fonction lançant une requete sql avec des parametre, puis appelant un resultSetProcessor pour traiter le resultSet obtenu. Une fois le
     * traitement effectué, la connection est fermé
     * 
     * @param processor
     * @param sql
     * @param params
     * @throws SQLException
     */
    public void executeQuery(final ResultSetProcessor<?> processor, final String sql, final Object... params) throws SQLException, ServiceException {
        final Connection connection = this.sqlConnectionFactory.createConnection();

        SQLException sqlException = null;
        RequestFailedException serviceException = null;
        ResultSet resultSet = null;

        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            initPreparedStatementParameters(preparedStatement, params);

            resultSet = preparedStatement.executeQuery();
            processor.populateResultFromResultSet(resultSet);
            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            sqlException = e;
        } catch (final RequestFailedException e) {
            serviceException = e;
        } finally {
            this.sqlConnectionFactory.closeConnection(connection);
        }

        if (sqlException != null) throw sqlException;
        if (serviceException != null) throw serviceException;

    }

    /**
     * Fonction lançant une requete sql avec des parametre, puis appelant un resultSetProcessor pour traiter le resultSet obtenu. Une fois le
     * traitement effectué, la connection est fermé
     * 
     * @param processor
     * @param sql
     * @param params
     * @throws SQLException
     */
    public void executeQuery(final ResultSetProcessor<?> processor, final String sql, final List<Object> params) throws SQLException, ServiceException {
        final Connection connection = this.sqlConnectionFactory.createConnection();

        SQLException sqlException = null;
        RequestFailedException serviceException = null;
        ResultSet resultSet = null;

        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            initPreparedStatementParameters(preparedStatement, params);

            resultSet = preparedStatement.executeQuery();
            processor.populateResultFromResultSet(resultSet);
            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            sqlException = e;
        } catch (final RequestFailedException e) {
            serviceException = e;
        } finally {
            this.sqlConnectionFactory.closeConnection(connection);
        }

        if (sqlException != null) throw sqlException;
        if (serviceException != null) throw serviceException;

    }

    /**
     * Fonction lançant une requete sql d'update avec des parametre. Ne permet pas de trai
     * 
     * @param processor
     * @param sql
     * @param params
     * @throws SQLException
     * @throws ConnectionException
     */
    public void executeUpdate(final String sql, final Object... params) throws SQLException, ConnectionException {
        final Connection connection = this.sqlConnectionFactory.createConnection();

        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            initPreparedStatementParameters(preparedStatement, params);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } finally {
            this.sqlConnectionFactory.closeConnection(connection);
        }

    }

    /**
     * Fonction lançant une requete sql d'update avec des parametre. Remonte la valeur de séquence utilisée
     * 
     * @param processor
     * @param sql
     * @param params
     * @throws SQLException
     * @throws ConnectionException
     */
    public int executeUpdateWithSequence(final String sql, final Object... params) throws SQLException, ConnectionException {
        final Connection connection = this.sqlConnectionFactory.createConnection();

        SQLException sqlException = null;
        int id = -1;

        try {
            final OraclePreparedStatement preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sql);
            initPreparedStatementParameters(preparedStatement, params);
            preparedStatement.registerReturnParameter(params.length + 1, OracleTypes.INTEGER);

            int count = preparedStatement.executeUpdate();
            if (count > 0)
            {
                ResultSet rset = preparedStatement.getReturnResultSet(); //rest is not null and not empty
                while (rset.next())
                {
                    id = rset.getInt(1);
                }
            }

            preparedStatement.close();

        } catch (final SQLException e) {
            sqlException = e;
        } finally {
            this.sqlConnectionFactory.closeConnection(connection);
        }

        if (sqlException != null) throw sqlException;
        return id;
    }

    public static void initPreparedStatementParameters(final PreparedStatement preparedStatement, final List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            final Object param = params.get(i);
            final int index = i + 1;
            initPreparedStatementParameter(preparedStatement, index, param);
        }
    }

    public static void initPreparedStatementParameters(final PreparedStatement preparedStatement, final Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            final Object param = params[i];
            final int index = i + 1;
            initPreparedStatementParameter(preparedStatement, index, param);
        }
    }

    private static void initPreparedStatementParameter(final PreparedStatement preparedStatement, int index, Object value) throws SQLException {
        if (value != null) {
            if (value instanceof Integer) {
                preparedStatement.setInt(index, ((Integer) value).intValue());
            } else if (value instanceof String) {
                preparedStatement.setString(index, (String) value);
            } else if (value instanceof Timestamp) {
                preparedStatement.setTimestamp(index, (Timestamp) value);
            } else if (value instanceof Date) {
                preparedStatement.setDate(index, (Date) value);
            } else if (value instanceof java.util.Date) {
                preparedStatement.setDate(index, new Date(((java.util.Date) value).getTime()));
            } else {
                preparedStatement.setString(index, value.toString());
            }
        }

    }

    public Connection getConnection() {
        return this.connection;
    }

}
