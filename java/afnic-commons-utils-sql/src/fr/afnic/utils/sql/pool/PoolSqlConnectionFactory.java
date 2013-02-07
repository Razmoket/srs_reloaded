/*
 * $Id: PersonalPoolSqlConnectionFactory.java,v 1.8 2010/08/17 14:49:21 ginguene Exp $ $Revision: 1.8 $ $Author: ginguene $
 */

package fr.afnic.utils.sql.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import oracle.jdbc.driver.OracleDriver;
import fr.afnic.commons.beans.Authentification;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ConnectionGarbageCollector;
import fr.afnic.utils.sql.ISqlConnectionFactory;

/**
 * Gestion de pool de connection.
 * 
 * @author ginguene
 */
public class PoolSqlConnectionFactory implements ISqlConnectionFactory {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(PoolSqlConnectionFactory.class);

    private static final ConnectionGarbageCollector GARBAGE_COLLECTOR = new ConnectionGarbageCollector();

    private final PoolSqlConnectionConfiguration configuration;
    private final Authentification authentification;

    private final List<LockableConnection> connections;

    private int createCount = 0;

    /**Permet de connaitre le nombre maximal de connection simultanément utilisée que l'on a eu durant l'utilisation de la factory*/
    /**Sert uniquement de metrique*/
    private int maxConnectionCount = 0;

    private int nbCreateConnectionCall = 0;

    public List<LockableConnection> getConnections() {
        return this.connections;
    }

    /**
     * Etablie une connection a une base de donnée
     */
    public PoolSqlConnectionFactory(Authentification authentification, PoolSqlConnectionConfiguration configuration) throws ServiceException {
        //Ligne utile car sinon createNewConnection() plante, le driver n'étant pas chargé
        new OracleDriver();

        this.configuration = Preconditions.checkNotNull(configuration, "configuration");
        this.authentification = Preconditions.checkNotNull(authentification, "authentification");
        this.connections = new ArrayList<LockableConnection>(configuration.getPoolSize());
        this.printInfo();
    }

    public PoolSqlConnectionConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public void closeConnection(final Connection connection) throws ConnectionException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new ConnectionException("closeConnection() failed", e);
        }
    }

    @Override
    public Connection createConnection() throws ConnectionException {
        try {
            long start = System.currentTimeMillis();
            Connection connection = this.getOrCreateConnection();
            long delay = System.currentTimeMillis() - start;

            if (delay > 100) {
                LOGGER.debug("Time to create connection to database: " + delay + "ms (call:" + this.nbCreateConnectionCall + ")");
            }

            this.nbCreateConnectionCall++;
            return connection;
        } catch (final SQLException e) {
            throw new ConnectionException("createConnection() failed", e);
        }
    }

    protected synchronized LockableConnection getOrCreateConnection() throws SQLException, ConnectionException {

        long start = System.currentTimeMillis();

        /*La boucle s'arrete dès q'une connection libre est trouvée*/
        while (true) {
            this.garbageObsoleteConnections();
            LockableConnection connection = this.getExistingConnectionToUse();
            if (connection != null) {
                return connection;
            } else {
                if (this.isAllowedToCreateNewConnection()) {
                    return this.createNewConnection();
                }
            }

            long delay = System.currentTimeMillis() - start;
            if (delay > this.configuration.getTimeout()) {
                this.printPool();
                throw new ConnectionException("No unlocked connection after " + this.configuration.getTimeout() + " ms");
            }
            this.waitUnlockConnection();
        }
    }

    protected void printPool() {
        for (LockableConnection connection : this.connections) {
            LOGGER.debug(connection.toString());
        }

    }

    protected void waitUnlockConnection() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.error("waitUnlockConnection() failed", e);
        }
    }

    protected boolean isAllowedToCreateNewConnection() {
        return this.connections.size() < this.configuration.getPoolSize();
    }

    private LockableConnection getExistingConnectionToUse() {
        for (final LockableConnection jdbcConnection : this.connections) {
            if (jdbcConnection.lock()) {
                return jdbcConnection;
            }
        }
        return null;
    }

    public synchronized void garbageObsoleteConnections() {
        for (int i = this.connections.size() - 1; i >= 0; i--) {
            final LockableConnection jdbcConnection = this.connections.get(i);
            if (jdbcConnection.isObsolete() && !jdbcConnection.isLocked()) {
                GARBAGE_COLLECTOR.garbage(jdbcConnection);
                this.connections.remove(i);
            } else {
                //Pour les connection non fermées mais obsolete depuis très longtemps
                //Traitement utilse uniquement si des process ne ferment pas les connection
                if (jdbcConnection.isLockedSinceTooManyTime()) {
                    GARBAGE_COLLECTOR.garbage(jdbcConnection);
                    this.connections.remove(i);
                    LOGGER.warn("locked since too many time: " + jdbcConnection.toString());
                }
            }
        }
    }

    private LockableConnection createNewConnection() throws SQLException {

        final Connection connection = DriverManager.getConnection(this.configuration.getUrl(),
                                                                  this.authentification.getUserName(),
                                                                  this.authentification.getPassword());

        final LockableConnection lockableConnection = new LockableConnection(connection,
                                                                             this.configuration.getMaxUsePerConnection(),
                                                                             this.configuration.getMaxConnectionUseTime());
        lockableConnection.lock();
        this.connections.add(lockableConnection);

        if (this.connections.size() > this.maxConnectionCount) {
            this.maxConnectionCount = this.connections.size();
        }

        this.createCount++;
        if (this.createCount % 100 == 0) {
            LOGGER.debug("Connection to database createCount: " + this.createCount);
        }
        return lockableConnection;
    }

    public int getMaxConnectionCount() {
        return this.maxConnectionCount;
    }

    public void printInfo() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("######################################################");
            LOGGER.info("  - username: " + this.authentification.getUserName());
            LOGGER.info("  - poolSize: " + this.configuration.getPoolSize());
            LOGGER.info("  - maxUsePerConnection: " + this.configuration.getMaxUsePerConnection());
            LOGGER.info("------------------------------------------------------");
            LOGGER.info("  - connection Url: " + this.configuration.getUrl().replaceAll("\\s*", ""));
            LOGGER.info("######################################################");
        }
    }

}
