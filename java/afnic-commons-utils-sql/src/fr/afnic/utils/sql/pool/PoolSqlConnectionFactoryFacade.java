/*
 * $Id: PersonalPoolSqlConnectionFactory.java,v 1.8 2010/08/17 14:49:21 ginguene Exp $ $Revision: 1.8 $ $Author: ginguene $
 */

package fr.afnic.utils.sql.pool;

import java.io.IOException;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.afnic.commons.beans.Authentification;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeInitializationException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Gestion de pool de connection.
 * 
 * @author ginguene
 */
public class PoolSqlConnectionFactoryFacade {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(PoolSqlConnectionFactoryFacade.class);

    public static PoolSqlConnectionFactory getSqlPoolConnectionFactory(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {

        String authentificationName = database.name() + "Authenfication-" + tld.name();
        String confName = database.name() + "Configuration-" + tld.name();

        Authentification authentification = loadAuthentificationFromConfigurationFile(authentificationName);
        PoolSqlConnectionConfiguration configuration = loadConfigurationFromConfigurationFile(confName);
        return new PoolSqlConnectionFactory(authentification, configuration);
    }

    public static Authentification loadAuthentificationFromConfigurationFile(String name) throws ServiceFacadeInitializationException {
        ClassPathResource classPathResource = null;
        System.out.println("chargement de l'authentification pour : " + name);
        try {
            LOGGER.debug("load " + AppServiceFacade.CONFIGURATION_FILE_NAME);
            classPathResource = new ClassPathResource(AppServiceFacade.CONFIGURATION_FILE_NAME);
            XmlBeanFactory beanFactory = new XmlBeanFactory(classPathResource);
            Authentification authentification = beanFactory.getBean(name, Authentification.class);
            return authentification;
        } catch (Exception e) {
            String fileName = "";
            if (classPathResource != null) {
                try {
                    fileName = "[" + classPathResource.getFile().getAbsolutePath() + "]";
                } catch (IOException e1) {
                    fileName = "[" + AppServiceFacade.CONFIGURATION_FILE_NAME + " not found" + "]";
                }
            }
            throw new ServiceFacadeInitializationException("loadAuthentificationFromConfigurationFile() failed " + fileName, e);
        }
    }

    public static PoolSqlConnectionConfiguration loadConfigurationFromConfigurationFile(String name) throws ServiceFacadeInitializationException {
        ClassPathResource classPathResource = null;
        System.out.println("chargement de la configuration pour : " + name);
        try {
            LOGGER.debug("load " + AppServiceFacade.CONFIGURATION_FILE_NAME);
            classPathResource = new ClassPathResource(AppServiceFacade.CONFIGURATION_FILE_NAME);
            XmlBeanFactory beanFactory = new XmlBeanFactory(classPathResource);
            PoolSqlConnectionConfiguration configuration = beanFactory.getBean(name, PoolSqlConnectionConfiguration.class);
            return configuration;
        } catch (Exception e) {
            String fileName = "";
            if (classPathResource != null) {
                try {
                    fileName = "[" + classPathResource.getFile().getAbsolutePath() + "]";
                } catch (IOException e1) {
                    fileName = "[" + AppServiceFacade.CONFIGURATION_FILE_NAME + " not found" + "]";
                }
            }
            throw new ServiceFacadeInitializationException("loadAuthentificationFromConfigurationFile() failed " + fileName, e);
        }
    }
}
