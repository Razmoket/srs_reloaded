/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/IllegalArgumentException.java#3 $
 * $Revision: #3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception;

/**
 * Exception declenchée en cas de parametres invalide lors d'un appel à un Data Access Object
 * 
 * @author ginguene
 * 
 */
public class IllegalArgumentException extends ServiceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IllegalArgumentException(String message) {
        super(message);
    }

}
