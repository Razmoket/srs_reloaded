package fr.afnic.utils.sql;

import fr.afnic.commons.beans.Authentification;
import fr.afnic.utils.sql.pool.PoolSqlConnectionConfiguration;

public abstract class SqlFacade {

    protected static final ISqlFacade INSTANCE = new SpringSqlFacade();

    protected static ISqlFacade getInstance() {
        return INSTANCE;
    }

    public static PoolSqlConnectionConfiguration getConfiguration() {
        return getInstance().getConfigurationImpl();
    }

    public static ISqlConnectionFactory getNicopeFactory() {
        return getInstance().getNicopeFactoryImpl();
    }

    public static ISqlConnectionFactory getBoaFactory() {
        return getInstance().getBoaFactoryImpl();
    }

    public static ISqlConnectionFactory getAgtfFactory() {
        return getInstance().getAgtfFactoryImpl();
    }

    public static ISqlConnectionFactory getLegalFactory() {
        return getInstance().getLegalFactoryImpl();
    }

    public static ISqlConnectionFactory getGericoFactory() {
        return getInstance().getGericoFactoryImpl();
    }

    public static ISqlConnectionFactory getGericoSandboxFactory() {
        return getInstance().getGericoSandboxFactoryImpl();
    }

    public static Authentification getNicopeAuthenfication() {
        return getInstance().getNicopeAuthentificationImpl();
    }

    public static Authentification getBoaAuthenfication() {
        return getInstance().getBoaAuthentificationImpl();
    }

    public static Authentification getJuriduqeAuthenfication() {
        return getInstance().getLegalAuthentificationImpl();
    }

    public static Authentification getAgtfAuthenfication() {
        return getInstance().getAgtfAuthentificationImpl();
    }

    public static Authentification getGericoaAuthenfication() {
        return getInstance().getGericoAuthentificationImpl();
    }

}
