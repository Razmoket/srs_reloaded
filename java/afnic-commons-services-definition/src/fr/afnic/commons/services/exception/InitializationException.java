/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/InitializationException.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception;

/**
 * Exception déclenchée lors d'une erreur d'initialisation d'un Data Access Object
 * 
 * @author ginguene
 * 
 */
public class InitializationException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public InitializationException(String message, Exception exception) {
        super(message, exception);
    }

    public InitializationException(String message) {
        super(message);
    }

}
