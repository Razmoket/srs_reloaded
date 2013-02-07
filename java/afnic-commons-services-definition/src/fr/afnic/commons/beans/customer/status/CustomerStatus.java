/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.beans.customer.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.history.HistoryEvent;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.ToStringHelper;

public abstract class CustomerStatus extends HistoryEvent implements IDescribedExternallyObject {

    private static final long serialVersionUID = -1525027154867983126L;

    public static final CustomerStatus ACTIVE = new Active();
    public static final CustomerStatus BLOCKED = new Blocked();
    public static final CustomerStatus INACTIVE = new Inactive();
    public static final CustomerStatus ARCHIVED = new Archived();
    public static final CustomerStatus DELETED = new Deleted();

    protected CustomerStatus() {
    }

    /**
     * indique les status dans lesquels on peut passer à partir du statut courant.
     * 
     * @return
     */
    public <S extends CustomerStatus> List<Class<S>> getNextAllowedStatus(Customer customer) {
        List<Class<S>> nextAllowedStatus = new ArrayList<Class<S>>();
        this.populateNextAllowedStatus(nextAllowedStatus, customer);
        return Collections.unmodifiableList(nextAllowedStatus);
    }

    /**
     * Permet de remplir la liste des status accessible à partir du statut courant.
     * 
     * @return
     */
    public abstract <S extends CustomerStatus> void populateNextAllowedStatus(List<Class<S>> list, Customer customer);

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

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
        return this.getClass().getSimpleName();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
