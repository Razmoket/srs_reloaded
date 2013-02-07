package fr.afnic.commons.services.tld;

import java.io.IOException;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeInitializationException;
import fr.afnic.commons.services.provider.TldServiceProvider;

public enum TldServiceFacade {

    Fr(1),
    Paris(2);

    private TldServiceProvider provider;

    private int idTldGrc;

    public static final String CONFIGURATION_FILE_NAME = "afnic-commons-service-definition.xml";
    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(TldServiceFacade.class);

    private TldServiceFacade(int id) {
        this.idTldGrc = id;
    }

    public static TldServiceFacade findTldById(int toFind) {
        for (TldServiceFacade tldTemp : TldServiceFacade.values()) {
            if (tldTemp.getIdTldGrc() == toFind)
                return tldTemp;
        }
        return null;
    }

    public TldServiceProvider getServiceProvider() {
        if (this.provider == null) {
            this.loadInstanceFromConfigurationFile();
        }
        return this.provider;
    }

    public int getIdTldGrc() {
        return this.idTldGrc;
    }

    /**
     * A N'UTILISER QUE POUR DES TESTS
     */
    public void setServiceProvider(TldServiceProvider provider) {
        this.provider = provider;
    }

    public void loadInstanceFromConfigurationFile() throws ServiceFacadeInitializationException {

        ClassPathResource classPathResource = null;
        System.out.println("chargement de l'enum du tld");
        try {
            LOGGER.debug("load " + AppServiceFacade.CONFIGURATION_FILE_NAME);
            classPathResource = new ClassPathResource(AppServiceFacade.CONFIGURATION_FILE_NAME);
            XmlBeanFactory beanFactory = new XmlBeanFactory(classPathResource);
            TldServiceProvider facade = beanFactory.getBean(this.name() + "Provider", TldServiceProvider.class);
            this.setServiceProvider(facade);
        } catch (Exception e) {
            String fileName = "";
            if (classPathResource != null) {
                try {
                    fileName = "[" + classPathResource.getFile().getAbsolutePath() + "]";
                } catch (IOException e1) {
                    fileName = "[" + AppServiceFacade.CONFIGURATION_FILE_NAME + " not found" + "]";
                }
            }
            throw new ServiceFacadeInitializationException("loadInstanceFromConfigurationFile() failed " + fileName, e);
        }
    }

}
