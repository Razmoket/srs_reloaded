package fr.afnic.commons.services.exception.invalidformat;

/**
 * Exception retournée si un parametre désignant une addresse mail n'a pas le bon format.
 * 
 * @author ginguene
 * 
 */
public class InvalidEmailAddressException extends InvalidFormatException {

    private static final long serialVersionUID = 1L;

    private String emailAddress;

    public InvalidEmailAddressException(String emailAddress) {
        super("'" + emailAddress + "' is not a valid email address");
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

}
