/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/corporateentity/OtherLegalStructure.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.corporateentity;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Structure légale qui n'est ni une association, ni une entreprise. Cela peut concerner des mairies ou des structures administratives.
 * 
 * @author ginguene
 * 
 */
public class OtherLegalStructure extends CorporateEntity {

    private static final long serialVersionUID = -4466707171245024025L;

    private String description;

    protected OtherLegalStructure(UserId userId, TldServiceFacade tld) {
        super(OrganizationType.Other, userId, tld);
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

}
