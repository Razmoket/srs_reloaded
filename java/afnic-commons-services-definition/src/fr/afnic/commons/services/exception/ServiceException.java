/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/ServiceException.java#6 $
 * $Revision: #6 $
 * $Author: chandavoine $
 */

package fr.afnic.commons.services.exception;

import fr.afnic.AfnicException;
import fr.afnic.commons.utils.Preconditions;

/**
 * Exception retournée lors de l'appel à un service.
 * 
 * @author ginguene
 * 
 */
public class ServiceException extends AfnicException {

    private static final long serialVersionUID = 1L;

    private final FaultCode faultCode;

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
        this.faultCode = FaultCode.Unknown;
    }

    public ServiceException(String message) {
        super(message);
        this.faultCode = FaultCode.Unknown;
    }

    public ServiceException(FaultCode faultCode, String message, Throwable throwable) {
        super(message, throwable);
        this.faultCode = Preconditions.checkNotNull(faultCode, "faultCode ");
    }

    public ServiceException(FaultCode faultCode, String message) {
        super(message);
        this.faultCode = Preconditions.checkNotNull(faultCode, "faultCode ");
    }

    public FaultCode getFaultCode() {
        return this.faultCode;
    }

}
