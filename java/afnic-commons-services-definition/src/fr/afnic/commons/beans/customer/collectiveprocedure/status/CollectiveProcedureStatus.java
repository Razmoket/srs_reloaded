/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.collectiveprocedure.status;

import java.util.List;
import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.beans.history.HistoryEvent;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.utils.ToStringHelper;

public abstract class CollectiveProcedureStatus extends HistoryEvent implements IDescribedInternalObject {

    private static final long serialVersionUID = 1L;

    private final String description;

    protected CollectiveProcedureStatus(String description) {
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
        return this.getClass().getSimpleName();
    }

    public final boolean isNextAllowedStatus(CollectiveProcedureStatus status) {
        return this.getNextAllowedStatus().contains(status.getClass());
    }

    public final boolean isFinalStatus() {
        return this.getNextAllowedStatus().isEmpty();
    }

    public abstract List<Class<? extends CollectiveProcedureStatus>> getNextAllowedStatus();

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
