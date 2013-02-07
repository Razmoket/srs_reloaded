/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.termination;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;

public enum TerminationType implements IDescribedInternalObject {

    MembershipTermination("Résiliation par le client"),
    MembershipPaymentTermination("Résiliation par défaut de paiement");

    private final String description;

    private TerminationType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
