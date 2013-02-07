package fr.afnic.commons.services.exception;

public class IllegalDomainNameException extends ServiceException {
    private static final long serialVersionUID = 1L;

    private final String domainName;

    public IllegalDomainNameException(String domainName) {
        super("'" + domainName + "' is not a valid domain name.");
        this.domainName = domainName;
    }

    public String getDomainName() {
        return this.domainName;
    }

}
