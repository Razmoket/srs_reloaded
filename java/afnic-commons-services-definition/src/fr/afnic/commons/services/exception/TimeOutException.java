/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/TimeOutException.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.services.exception;

/**
 * Retourné lorsqu'une demande s'est terminée par un timeout. L'opération a bien été enregistré auprès du service mais elle prend trop de temps à
 * s'executer.
 * 
 * @author ginguene
 * 
 */
public class TimeOutException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public TimeOutException(String message, Exception exception) {
        super(message, exception);
    }

    public TimeOutException(String message) {
        super(message);
    }

}
