/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.exception;

/**
 * Exception retourée si un parametre est null.
 * 
 * @author ginguene
 * 
 */
public class NullArgumentException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    private String argumentName;

    public NullArgumentException(String argumentName) {
        super(argumentName + " cannot be null");
        this.argumentName = argumentName;
    }

    public String getArgumentName() {
        return this.argumentName;
    }

}
