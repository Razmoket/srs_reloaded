/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.facade.exception;

import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Exception levée si aucune implémentation n'a été défini pour un service que l'on souhaite utiliser.
 * 
 * @author ginguene
 * 
 */
public class ServiceNotInitializedException extends ServiceFacadeException {

    private static final long serialVersionUID = 1L;

    public ServiceNotInitializedException(String serviceName) {
        super("No implementation defined in ServiceFacade for service " + serviceName + ". Please check file " + AppServiceFacade.CONFIGURATION_FILE_NAME + ".");
    }

}