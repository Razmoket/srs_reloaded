package fr.afnic.utils.sql;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.afnic.commons.beans.Authentification;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.pool.PoolSqlConnectionConfiguration;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

/**
 * Charge une instance de ISqlConnectionFactory depuis un fichier sql.xml au format spring qui doit <br/>
 * se trouver dans le classpath.
 * 
 * @author ginguene
 * 
 */
final class SpringSqlFacade implements ISqlFacade {

    private static final String CONFIGURATION_FILE_NAME = "sql.xml";

    private final PoolSqlConnectionConfiguration configuration;

    private final ISqlConnectionFactory nicopeFactory;
    private final ISqlConnectionFactory boaFactory;
    private final ISqlConnectionFactory agtfFactory;
    private final ISqlConnectionFactory legalFactory;
    private final ISqlConnectionFactory gericoFactory;
    private ISqlConnectionFactory gericoSandboxFactory;

    private final Authentification boaAuthenfication;
    private final Authentification agtfAuthenfication;
    private final Authentification legalAuthenfication;
    private final Authentification gericoAuthenfication;
    private Authentification gericoSandboxAuthenfication;
    private final Authentification nicopeAuthenfication;

    SpringSqlFacade() {
        final XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource(SpringSqlFacade.CONFIGURATION_FILE_NAME));

        try {
            this.nicopeFactory = beanFactory.getBean("sqlNicopeConnectionFactory", ISqlConnectionFactory.class);
            this.boaFactory = beanFactory.getBean("sqlBoaConnectionFactory", ISqlConnectionFactory.class);
            this.agtfFactory = beanFactory.getBean("sqlAgtfConnectionFactory", ISqlConnectionFactory.class);
            this.legalFactory = beanFactory.getBean("sqlLegalConnectionFactory", ISqlConnectionFactory.class);
            this.gericoFactory = beanFactory.getBean("sqlGericoConnectionFactory", ISqlConnectionFactory.class);
            this.configuration = beanFactory.getBean("jdbcConf", PoolSqlConnectionConfiguration.class);
            this.nicopeAuthenfication = beanFactory.getBean("nicopeAuthenfication", Authentification.class);
            this.gericoAuthenfication = beanFactory.getBean("gericoAuthenfication", Authentification.class);
            this.boaAuthenfication = beanFactory.getBean("boaAuthenfication", Authentification.class);
            this.legalAuthenfication = beanFactory.getBean("legalAuthenfication", Authentification.class);
            this.agtfAuthenfication = beanFactory.getBean("agtfAuthenfication", Authentification.class);
        } catch (Exception e) {
            throw new RuntimeException("Load " + SpringSqlFacade.CONFIGURATION_FILE_NAME + " failed", e);
        }

        try {
            this.gericoSandboxFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(SqlDatabaseEnum.GericoSandbox, TldServiceFacade.Fr);
            this.gericoSandboxAuthenfication = PoolSqlConnectionFactoryFacade.loadAuthentificationFromConfigurationFile("GericoSandbox" + "Authenfication-" + TldServiceFacade.Fr);
        } catch (Exception e) {
            //Pour les les env sauf la prod, il n'y a pas de sandbox de définit
            System.err.println("No Sandbox environnement defined");
        }
    }

    @Override
    public ISqlConnectionFactory getNicopeFactoryImpl() {
        return this.nicopeFactory;
    }

    @Override
    public ISqlConnectionFactory getBoaFactoryImpl() {
        return this.boaFactory;
    }

    @Override
    public ISqlConnectionFactory getLegalFactoryImpl() {
        return this.legalFactory;
    }

    @Override
    public ISqlConnectionFactory getAgtfFactoryImpl() {
        return this.agtfFactory;
    }

    @Override
    public ISqlConnectionFactory getGericoFactoryImpl() {
        return this.gericoFactory;
    }

    @Override
    public PoolSqlConnectionConfiguration getConfigurationImpl() {
        return this.configuration;
    }

    @Override
    public Authentification getBoaAuthentificationImpl() {
        return this.boaAuthenfication;
    }

    @Override
    public Authentification getLegalAuthentificationImpl() {
        return this.legalAuthenfication;
    }

    @Override
    public Authentification getAgtfAuthentificationImpl() {
        return this.agtfAuthenfication;
    }

    @Override
    public Authentification getGericoAuthentificationImpl() {
        return this.gericoAuthenfication;
    }

    @Override
    public Authentification getNicopeAuthentificationImpl() {
        return this.nicopeAuthenfication;
    }

    @Override
    public ISqlConnectionFactory getGericoSandboxFactoryImpl() {
        return this.gericoSandboxFactory;
    }

    @Override
    public Authentification getGericoSandboxAuthentificationImpl() {
        return this.gericoSandboxAuthenfication;
    }

}
