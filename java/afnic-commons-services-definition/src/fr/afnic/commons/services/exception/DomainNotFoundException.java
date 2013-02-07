/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.exception;

/**
 * Exception levée si un domaine n'est pas trouvé.
 * 
 */
public class DomainNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    private String domainName;

    public DomainNotFoundException(String domainName) {
        super("Domain " + domainName + " not found");
        this.domainName = domainName;
    }

    public String getDomainName() {
        return this.domainName;
    }

}
