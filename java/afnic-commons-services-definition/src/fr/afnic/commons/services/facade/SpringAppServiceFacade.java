package fr.afnic.commons.services.facade;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.afnic.commons.services.facade.exception.ServiceFacadeInitializationException;

public class SpringAppServiceFacade extends CustomAppServiceFacade {

    /**true => si une erreur est rencontrée au chargement d'un service une erreur est remonté*/
    /**false => si une erreur est rencontrée au chargement d'un service un warning est affiché dans les logs*/
    private final boolean failedOnLoadError;

    public SpringAppServiceFacade() {
        this(true);
    }

    public SpringAppServiceFacade(boolean failedOnLoadError) {
        this.failedOnLoadError = failedOnLoadError;
        java.lang.reflect.Field[] fields = AppServiceFacade.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isInterface() && !Modifier.isStatic(field.getModifiers())) {
                this.loadService(field);
            }
        }
    }

    private void loadService(Field field) {
        ClassPathResource classPathResource = null;
        String serviceName = field.getName();
        try {
            classPathResource = new ClassPathResource(AppServiceFacade.CONFIGURATION_FILE_NAME);
            XmlBeanFactory beanFactory = new XmlBeanFactory(classPathResource);
            field.set(this, beanFactory.getBean(serviceName));
        } catch (NoSuchBeanDefinitionException e) {
            this.processLoadServiceException(e, serviceName);
        } catch (CannotLoadBeanClassException e) {
            this.processLoadServiceException(e, serviceName);
        } catch (Exception e) {

            String fileName = "";
            if (classPathResource != null) {
                try {
                    fileName = "[" + classPathResource.getFile().getAbsolutePath() + "]";
                } catch (IOException e1) {
                    fileName = "[" + AppServiceFacade.CONFIGURATION_FILE_NAME + " not found" + "]";
                }
                throw new ServiceFacadeInitializationException("loadService(" + serviceName + ") from file " + fileName + " failed", e);
            }
        }
    }

    private void processLoadServiceException(Exception e, String serviceName) {
        if (this.failedOnLoadError) {
            throw new RuntimeException("loadService(" + serviceName + ") failed cause " + e.getMessage());
        } else {
            System.err.println("loadServiceFromField('" + serviceName + "') failed cause " + e.getMessage());
        }
    }

    /**
     * Définit que la facade à utiliser est celle-ci.
     */
    @Override
    public void use() {
        AppServiceFacade.use(this);
    }
}
