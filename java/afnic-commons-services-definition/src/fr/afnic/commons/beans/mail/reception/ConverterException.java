package fr.afnic.commons.beans.mail.reception;

import fr.afnic.AfnicException;

/**
 * Exception levée lors d'une erreur de conversion d'un mail.
 * 
 * 
 */
public class ConverterException extends AfnicException {

    private static final long serialVersionUID = 1L;

    public ConverterException(String message, Exception exception) {
        super(message, exception);
    }

    public ConverterException(String message) {
        super(message);
    }

}
