package fr.afnic.commons.services.exception.invalidformat;

/**
 * Exception retournée si un parametre désignant un nom de domaine n'a pas le bon format.
 * 
 * @author ginguene
 * 
 */
public class InvalidDomainNameException extends InvalidFormatException {

    private static final long serialVersionUID = 1L;

    private final String domainName;

    public InvalidDomainNameException(String domainName) {
        super("'" + domainName + "' is not a valid domain name.");
        this.domainName = domainName;
    }

    public String getDomainName() {
        return this.domainName;
    }

}
