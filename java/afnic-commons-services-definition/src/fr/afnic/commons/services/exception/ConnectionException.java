/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/ConnectionException.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception;

/**
 * Exception remontée lors d'une connection à un service.
 * 
 * @author ginguene
 * 
 */
public class ConnectionException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public ConnectionException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ConnectionException(String message) {
        super(message);
    }

}
