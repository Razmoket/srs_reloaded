/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.beans.request.reserveddomains;

import java.util.Locale;

import fr.afnic.commons.services.exception.ServiceException;

public class NormalDomain extends ReservedDomainNameMotivation {

    private static final long serialVersionUID = 1L;

    public NormalDomain() {
        super("Normal");
    }

    @Override
    public String getDescription() throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

}
