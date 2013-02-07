package fr.afnic.commons.beans.mail.reception;

import fr.afnic.commons.services.exception.ServiceException;

/**
 * Exception retournée en cas d'erreur sur les methode<br/>
 * de l'interface IMailBox
 * 
 * @author ginguene
 * 
 */
public class EmailBoxException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public EmailBoxException(String message, Exception exception) {
        super(message, exception);
    }

    public EmailBoxException(String message) {
        super(message);
    }

}
