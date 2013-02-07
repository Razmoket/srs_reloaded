/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/invalidformat/InvalidNichandleException.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception.invalidformat;

public class InvalidSirenException extends InvalidFormatException {
    private static final long serialVersionUID = 1L;

    private String siren;

    public InvalidSirenException(String siren) {
        super("'" + siren + "' is not a valid siren");
        this.siren = siren;
    }

    public String getSiren() {
        return this.siren;
    }

}
