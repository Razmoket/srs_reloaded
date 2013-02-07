/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.facade.exception;

/**
 * Exception levée lors de l'appel aux methode ServiceFacade.get[...]Service().<br/>
 * <br/>
 * Hérite du runtime exception car on utilise les facade pour déclarer les logger dans des attributs statique et finaux:<br/>
 * private static final ILogger LOGGER = ServiceFacade.getLoggerService().getLogger([...].class);<br/>
 * <br/>
 * or cet appel est impossible si l'exception levée par ServiceFacade.getLoggerService() n'est pas de type RuntimeException.<br/>
 * 
 * @author ginguene
 * 
 */
public class ServiceFacadeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceFacadeException(String message, Exception exception) {
        super(message, exception);
    }

    public ServiceFacadeException(String message) {
        super(message);
    }

}
