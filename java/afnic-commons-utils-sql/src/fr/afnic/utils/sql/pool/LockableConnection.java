/*
 * $Id: JdbcConnection.java,v 1.4 2010/04/09 10:47:31 ginguene Exp $ $Revision: 1.4 $ $Author: ginguene $
 */

package fr.afnic.utils.sql.pool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Classe gérant une connection jdbc faite pour faire partie d'un pool
 * 
 * @see fr.afnic.commons.dao.sql.jdbcdbcConnectionPool
 * 
 * @author ginguene
 * 
 */
public class LockableConnection implements Connection {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(LockableConnection.class);

    /**Séquence d'id pour les connection*/
    private static int LAST_ID = 1;

    /**id de la connection*/
    private int id = 0;

    private final Connection connection;

    /**Indique si la connection est déja en cours d'utilisation*/
    private boolean isLocked;

    /**Nombre maximal d'utilisation d'une connection*/
    private final int maxUseCount;

    /**Temps maximal d'utilisation d'une connection en ms*/
    private final long maxUseTime;

    /**time stamp de création de la connection*/
    private final long createTimestamp;

    private long lockTimestamp;

    private LockableConnectionException lockTrace;

    public long getCreateTimestamp() {
        return this.createTimestamp;
    }

    /**Nombre de fois que la connection a été utilisé*/
    private int useCount = 0;

    public static int getLastId() {
        return LAST_ID;
    }

    public LockableConnection(final Connection conn, int maxUseCount, long maxUseTime) {
        this.connection = conn;
        this.isLocked = false;
        this.maxUseCount = maxUseCount;
        this.maxUseTime = maxUseTime;
        this.createTimestamp = System.currentTimeMillis();
        this.id = LAST_ID++;

    }

    public int getId() {
        return this.id;
    }

    public synchronized boolean lock() {
        if (this.isObsolete()) {
            return false;
        }

        if (this.isLocked) {
            return false;
        } else {
            this.setLocked(true);
            this.lockTimestamp = System.currentTimeMillis();
            try {
                throw new LockableConnectionException(this);
            } catch (LockableConnectionException e) {
                this.lockTrace = e;
            }
            return true;
        }
    }

    protected synchronized void unlock() {
        this.useCount++;
        this.setLocked(false);
    }

    private synchronized void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * Indique qu'une connection est obsolete depuis trop longtemps et qu'elle est toujours vérouillée
     * 
     */
    public synchronized boolean isLockedSinceTooManyTime() {
        try {
            this.getMetaData();
            boolean ret = this.lockTimestamp < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5) && this.isLocked;
            if (ret) {
                //Erreur en warn pour ne pas etre monitorée
                LOGGER.warn("Connection is locked since to many time", this.lockTrace);
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean isObsolete() {
        return !this.isNotObsolete();
    }

    public boolean isNotObsolete() {
        return this.createTimestamp > this.getMinCreateTimestamp()
               && this.getUseCount() < this.maxUseCount;
    }

    private long getMinCreateTimestamp() {
        return System.currentTimeMillis() - this.maxUseTime;
    }

    public synchronized boolean isLocked() {
        return this.isLocked;
    }

    @Override
    public void close() throws SQLException {
        this.unlock();
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public int getUseCount() {
        return this.useCount;
    }

    protected Connection getConnection() {
        return this.connection;
    }

    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        return this.connection.prepareCall(sql);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    @Override
    public String nativeSQL(final String sql) throws SQLException {
        return this.connection.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        this.connection.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.connection.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        this.connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        this.connection.rollback();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.connection.getMetaData();
    }

    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        this.connection.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.connection.isReadOnly();
    }

    @Override
    public void setCatalog(final String catalog) throws SQLException {
        this.connection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return this.connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
        this.connection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.connection.clearWarnings();
    }

    @Override
    public Array createArrayOf(final String arg0, final Object[] arg1) throws SQLException {
        return this.connection.createArrayOf(arg0, arg1);
    }

    @Override
    public Blob createBlob() throws SQLException {
        return this.connection.createBlob();
    }

    @Override
    public Clob createClob() throws SQLException {
        return this.connection.createClob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return this.connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return this.connection.createSQLXML();
    }

    @Override
    public Statement createStatement(final int arg0, final int arg1) throws SQLException {
        return this.connection.createStatement(arg0, arg1);
    }

    @Override
    public Statement createStatement(final int arg0, final int arg1, final int arg2) throws SQLException {
        return this.connection.createStatement(arg0, arg1, arg2);
    }

    @Override
    public Struct createStruct(final String arg0, final Object[] arg1) throws SQLException {
        return this.connection.createStruct(arg0, arg1);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return this.connection.getClientInfo();
    }

    @Override
    public String getClientInfo(final String arg0) throws SQLException {
        return this.connection.getClientInfo(arg0);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.connection.getHoldability();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.connection.getTypeMap();
    }

    @Override
    public boolean isValid(final int arg0) throws SQLException {
        return this.connection.isValid(arg0);
    }

    @Override
    public CallableStatement prepareCall(final String arg0, final int arg1, final int arg2) throws SQLException {
        return this.connection.prepareCall(arg0, arg1, arg2);
    }

    @Override
    public CallableStatement prepareCall(final String arg0, final int arg1, final int arg2, final int arg3) throws SQLException {
        return this.connection.prepareCall(arg0, arg1, arg2, arg3);
    }

    @Override
    public PreparedStatement prepareStatement(final String arg0, final int arg1) throws SQLException {
        return this.connection.prepareStatement(arg0, arg1);
    }

    @Override
    public PreparedStatement prepareStatement(final String arg0, final int[] arg1) throws SQLException {
        return this.connection.prepareStatement(arg0, arg1);
    }

    @Override
    public PreparedStatement prepareStatement(final String arg0, final String[] arg1) throws SQLException {
        return this.connection.prepareStatement(arg0, arg1);
    }

    @Override
    public PreparedStatement prepareStatement(final String arg0, final int arg1, final int arg2) throws SQLException {
        return this.connection.prepareStatement(arg0, arg1, arg2);
    }

    @Override
    public PreparedStatement prepareStatement(final String arg0, final int arg1, final int arg2, final int arg3) throws SQLException {
        return this.connection.prepareStatement(arg0, arg1, arg2, arg3);
    }

    @Override
    public void releaseSavepoint(final Savepoint arg0) throws SQLException {
        this.connection.releaseSavepoint(arg0);
    }

    @Override
    public void rollback(final Savepoint arg0) throws SQLException {
        this.connection.rollback(arg0);

    }

    @Override
    public void setClientInfo(final Properties arg0) throws SQLClientInfoException {
        this.connection.setClientInfo(arg0);
    }

    @Override
    public void setClientInfo(final String arg0, final String arg1) throws SQLClientInfoException {
        this.connection.setClientInfo(arg0, arg1);
    }

    @Override
    public void setHoldability(final int arg0) throws SQLException {
        this.connection.setHoldability(arg0);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return this.connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(final String arg0) throws SQLException {
        return this.connection.setSavepoint(arg0);
    }

    @Override
    public void setTypeMap(final Map<String, Class<?>> arg0) throws SQLException {
        this.connection.setTypeMap(arg0);
    }

    @Override
    public boolean isWrapperFor(final Class<?> arg0) throws SQLException {
        return this.connection.isWrapperFor(arg0);
    }

    @Override
    public <T> T unwrap(final Class<T> arg0) throws SQLException {
        return this.connection.unwrap(arg0);
    }

    @Override
    public String toString() {
        return "connection " + this.getId()
               + "[isLocked:" + this.isLocked() + "]"
               + "[isObsolete:" + this.isObsolete() + "]"
               + "[isLockedSinceTooManyTime:" + this.isLockedSinceTooManyTime() + "]"
               + "[createTimeStamp:" + this.getCreateTimestamp() + "]";
    }
}
