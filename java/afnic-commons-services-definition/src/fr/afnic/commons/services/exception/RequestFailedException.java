/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/RequestFailedException.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.services.exception;

public class RequestFailedException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public RequestFailedException(String message, Exception exception) {
        super(message, exception);
    }

    public RequestFailedException(String message) {
        super(message);
    }

}
