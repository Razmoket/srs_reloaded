package fr.afnic.commons.services.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import oracle.jdbc.pool.OracleDataSource;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;

public class DataSourceSqlConnectionFactory implements ISqlConnectionFactory {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(DataSourceSqlConnectionFactory.class);

    private static final String CONTEXT_FACTORY = "com.sun.jndi.fscontext.RefFSContextFactory";
    private static final String PROVIDER_LOCATION = "file:///var";

    private final static String CACHE_NAME = "MYCACHE";

    private String dataSourceClasse = null; // "oracle.jdbc.pool.OracleDataSourceFactory";
    private String dataSourceContextName = null; // "jdbc/MaDataSource";
    private String username = null; // "";
    private String password = null; // "";
    private String driverType = null; // "thin";
    private String refDataSource = null; // "oracle.jdbc.pool.OracleDataSource"
    private String connectionUrl = null; // url complete de connection à la base de

    private OracleDataSource source = null;

    @Override
    public Connection createConnection() throws ConnectionException {
        try {
            if (this.source == null) {
                this.initDataSource2();
            }
            return this.source.getConnection();
        } catch (final SQLException e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    private void initDataSource3() {

    }

    private void initDataSource2() throws ConnectionException {
        try {
            this.source = new OracleDataSource();
            this.source.setURL(this.connectionUrl);
            this.source.setUser(this.username);
            this.source.setPassword(this.password);

            // caching parms
            //this.source.setConnectionCachingEnabled(true);
            //this.source.setConnectionCacheName(CACHE_NAME);
            java.util.Properties prop = new java.util.Properties();
            prop.setProperty("MinLimit", "5"); // the cache size is 5 at least 
            prop.setProperty("MaxLimit", "25");
            prop.setProperty("InitialLimit", "3"); // create 3 connections at startup
            prop.setProperty("InactivityTimeout", "1800"); //  seconds
            prop.setProperty("AbandonedConnectionTimeout", "900"); //  seconds
            prop.setProperty("MaxStatementsLimit", "10");
            prop.setProperty("PropertyCheckInterval", "1");

            this.source.setConnectionCacheProperties(prop);

        } catch (final Exception e) {
            throw new ConnectionException(e.getMessage(), e);
        }

    }

    private void initDataSource() throws ConnectionException {
        try {
            final Context ctx = this.createAndInitiateContext();
            this.source = (OracleDataSource) ctx.lookup(this.dataSourceContextName);
        } catch (final NamingException e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    private InitialContext createAndInitiateContext() throws ConnectionException {
        try {
            // initialisation du contexte
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, DataSourceSqlConnectionFactory.CONTEXT_FACTORY);
            System.setProperty(Context.PROVIDER_URL, DataSourceSqlConnectionFactory.PROVIDER_LOCATION);
            final InitialContext ic = new InitialContext();

            this.printInfo();
            // création d'une référence sur la DataSource
            // TODO externaliser le type de dataSrouce en plus de la factory
            final Reference ref = new Reference(this.refDataSource, this.dataSourceClasse, null);

            ref.add(new StringRefAddr("driverType", this.driverType));
            ref.add(new StringRefAddr("user", this.username));
            ref.add(new StringRefAddr("password", this.password));
            ref.add(new StringRefAddr("url", this.connectionUrl));

            // XXX Test de valeur pour le driver OJDBC...
            ref.add(new StringRefAddr("maxActive", "1000"));
            ref.add(new StringRefAddr("maxIdle", "100"));
            ref.add(new StringRefAddr("maxWait", "-1"));

            ic.rebind(this.dataSourceContextName, ref);
            return ic;
        } catch (final NamingException e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    private void printInfo() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("######################################################");
            LOGGER.info("DataSource " + this.dataSourceContextName + " config:");
            LOGGER.info("\t\t dataSourceContextName: " + this.dataSourceContextName);
            LOGGER.info("\t\t dataSourceClasse: " + this.refDataSource);
            LOGGER.info("\t\t dataSourceClasseFactory: " + this.dataSourceClasse);
            LOGGER.info("\t\t username: " + this.username);
            LOGGER.info("\t\t password: " + this.password);
            LOGGER.info("\t\t driverType: " + this.driverType);
            LOGGER.info("------------------------------------------------------");
            LOGGER.info("\t\t connection Url: " + this.connectionUrl);
            LOGGER.info("######################################################");
        }
    }

    @Override
    public void closeConnection(Connection connection) throws ConnectionException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    public String getDataSourceClasse() {
        return this.dataSourceClasse;
    }

    public void setDataSourceClasse(String dataSourceClasse) {
        this.dataSourceClasse = dataSourceClasse;
    }

    public String getDataSourceContextName() {
        return this.dataSourceContextName;
    }

    public void setDataSourceContextName(String dataSourceContextName) {
        this.dataSourceContextName = dataSourceContextName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverType() {
        return this.driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getRefDataSource() {
        return this.refDataSource;
    }

    public void setRefDataSource(String refDataSource) {
        this.refDataSource = refDataSource;
    }

    public String getConnectionUrl() {
        return this.connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

}
