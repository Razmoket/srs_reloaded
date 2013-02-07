/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.beans.contact;

import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * JGI: Objet étrange utilisé par Syrelli, il devrait etre possible de le remplacer par
 * un GenericContact mais nous n'avons pas le temps pour le moment(20/10/2012)
 * 
 *
 */
@Deprecated
public class IndividualAndLegalStructure extends GrcContact {

    protected String organization;

    protected String vatnumber;

    protected String civility;

    public IndividualAndLegalStructure(ContactIdentity identity, UserId userIdCaller, TldServiceFacade tldCaller) {
        super(identity, userIdCaller, tldCaller);
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getVatnumber() {
        return this.vatnumber;
    }

    public void setVatnumber(String vatnumber) {
        this.vatnumber = vatnumber;
    }

    public String getCivility() {
        return this.civility;
    }

    public void setCivility(String civility) {
        this.civility = civility;
    }
}
