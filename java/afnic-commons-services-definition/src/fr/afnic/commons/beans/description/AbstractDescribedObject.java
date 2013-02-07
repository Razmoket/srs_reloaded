/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.beans.description;

import java.util.Locale;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Si l'on peut se servir de l'héritage, cet objet impléménte toutes les fonctions de IDescribedObject.
 */
public abstract class AbstractDescribedObject implements IDescribedExternallyObject {

    private static final long serialVersionUID = 1L;

    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDictionaryService().getDescription(this, userId, tld);
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDictionaryService().getDescription(this, locale, userId, tld);
    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

}
