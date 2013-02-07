package fr.afnic.commons.services.exception.invalidformat;

/**
 * Exception retournée si un parametre désignant un nom de domaine n'a pas le bon format.
 * 
 * @author ginguene
 * 
 */
public class InvalidSecondLevelDomainNameException extends InvalidFormatException {

    private static final long serialVersionUID = 1L;

    private String domainName;

    public InvalidSecondLevelDomainNameException(String domainName) {
        super("'" + domainName + "' is not a valid second level domain name");
        this.domainName = domainName;
    }

    public String getDomainName() {
        return this.domainName;
    }

}
