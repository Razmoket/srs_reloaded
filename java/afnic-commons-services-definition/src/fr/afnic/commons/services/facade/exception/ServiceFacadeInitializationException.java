/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.facade.exception;

/**
 * Exception levée si une erreur intervient au moment de l'initialisation d'une facade.
 * 
 * @author ginguene
 * 
 */
public class ServiceFacadeInitializationException extends ServiceFacadeException {

    private static final long serialVersionUID = 1L;

    public ServiceFacadeInitializationException(String message, Exception exception) {
        super(message, exception);
    }

    public ServiceFacadeInitializationException(String message) {
        super(message);
    }

}
