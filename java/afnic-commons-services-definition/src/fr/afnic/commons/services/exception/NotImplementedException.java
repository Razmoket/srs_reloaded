/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/NotImplementedException.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception;

public class NotImplementedException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public NotImplementedException() {
        super("Not implemented");
    }

    public NotImplementedException(String message) {
        super(message);
    }

}
