/*
 * $Id: $ $Revision: $
 */

package fr.afnic.commons.beans.request.reserveddomains;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;

public abstract class ReservedDomainNameMotivation implements IDescribedInternalObject {

    private static final long serialVersionUID = 1L;

    private final String description;

    protected ReservedDomainNameMotivation(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;
    }

    @Override
    public String getDictionaryKey() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        return this.getDescription();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
