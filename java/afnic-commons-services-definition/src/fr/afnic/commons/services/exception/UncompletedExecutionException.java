/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/UncompletedExecutionException.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.services.exception;

import java.util.List;

/**
 * Retourné lorsqu'une demande s'est executee partiellement. On peut accéder à la liste des elements qui ont réussi et à ceux qui ont échoués
 * 
 * @author ginguene
 * 
 */
public class UncompletedExecutionException extends ServiceException {

    private static final long serialVersionUID = 1L;

    protected List<String> succed;
    protected List<String> failed;

    public UncompletedExecutionException(List<String> succed, List<String> failed, String message, Exception exception) {
        super(message, exception);
        this.succed = succed;
        this.failed = failed;
    }

    public UncompletedExecutionException(List<String> succed, List<String> failed, String message) {
        super(message);
        this.succed = succed;
        this.failed = failed;
    }

    public List<String> getSucced() {
        return succed;
    }

    public List<String> getFailed() {
        return failed;
    }

}
