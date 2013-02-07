/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.description.describer;

import java.util.Locale;

import fr.afnic.commons.beans.customer.status.Blocked;
import fr.afnic.commons.beans.customer.status.CustomerStatus;
import fr.afnic.commons.beans.description.IDescriber;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class CustomerStatusDescriber implements IDescriber<CustomerStatus> {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(CustomerStatusDescriber.class);

    @Override
    public String getDescription(CustomerStatus status, Locale locale, UserId userId, TldServiceFacade tld) {

        if (status == CustomerStatus.ACTIVE) return "Actif";
        if (status == CustomerStatus.ARCHIVED) return "Archivé";
        if (status == CustomerStatus.INACTIVE) return "Inactif";
        if (status instanceof Blocked) return "Bloqué";

        CustomerStatusDescriber.LOGGER.warn("No description defined for customer status:" + status);
        return status.getDictionaryKey();
    }
}
