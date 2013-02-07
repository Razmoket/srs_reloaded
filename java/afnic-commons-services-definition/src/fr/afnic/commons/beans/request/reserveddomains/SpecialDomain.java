/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.beans.request.reserveddomains;

public class SpecialDomain extends ReservedDomainNameMotivation {

    private static final long serialVersionUID = 1L;

    public SpecialDomain(String description) {
        super("Domaine spécial [" + description + "]");
    }

}
