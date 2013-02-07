/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/invalidformat/InvalidFormatException.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.validatable;


/**
 * Exception levé en cas de données ne possédant pas un format correcte.
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class InvalidDataException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final InvalidDataDescription invalidDataDescription;

    public InvalidDataException(InvalidDataDescription invalidDataDescription) {
        this.invalidDataDescription = invalidDataDescription;
    }

    public InvalidDataDescription getInvalidDataDescription() {
        return this.invalidDataDescription;
    }

    @Override
    public String getMessage() {

        return this.invalidDataDescription.getDescription();
    }
}
