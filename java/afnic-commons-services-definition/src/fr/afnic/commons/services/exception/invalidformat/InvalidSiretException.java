/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/invalidformat/InvalidNichandleException.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception.invalidformat;

public class InvalidSiretException extends InvalidFormatException {
    private static final long serialVersionUID = 1L;

    private String siret;

    public InvalidSiretException(String siret) {
        super("'" + siret + "' is not a valid siret");
        this.siret = siret;
    }

    public String getSiret() {
        return this.siret;
    }

}
