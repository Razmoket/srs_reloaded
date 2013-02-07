package fr.afnic.utils.sql;

import fr.afnic.commons.beans.Authentification;
import fr.afnic.utils.sql.pool.PoolSqlConnectionConfiguration;

public interface ISqlFacade {

    public ISqlConnectionFactory getNicopeFactoryImpl();

    public ISqlConnectionFactory getBoaFactoryImpl();

    public ISqlConnectionFactory getAgtfFactoryImpl();

    public ISqlConnectionFactory getLegalFactoryImpl();

    public ISqlConnectionFactory getGericoFactoryImpl();

    public ISqlConnectionFactory getGericoSandboxFactoryImpl();

    public PoolSqlConnectionConfiguration getConfigurationImpl();

    public Authentification getBoaAuthentificationImpl();

    public Authentification getAgtfAuthentificationImpl();

    public Authentification getLegalAuthentificationImpl();

    public Authentification getGericoAuthentificationImpl();

    public Authentification getGericoSandboxAuthentificationImpl();

    public Authentification getNicopeAuthentificationImpl();

}
