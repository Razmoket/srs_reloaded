/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/invalidformat/InvalidNichandleException.java#4 $
 * $Revision: #4 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.services.exception.invalidformat;

public class InvalidNichandleException extends InvalidFormatException {
    private static final long serialVersionUID = 1L;

    private final String nichandle;

    public InvalidNichandleException(String nichandle) {
        super("'" + nichandle + "' is not a valid nicHandle.");
        this.nichandle = nichandle;
    }

    public String getNichHandle() {
        return this.nichandle;
    }

}
