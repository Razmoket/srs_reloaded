/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.whoiscontact;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.SearchCriteria;
import fr.afnic.commons.services.exception.IllegalDomainNameException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.ToStringHelper;

public class WhoisContactSearchCriteria extends SearchCriteria<WhoisContactSearchCriterion> {

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(WhoisContactSearchCriteria.class);

    private final UserId userId;
    private final TldServiceFacade tld;

    public WhoisContactSearchCriteria(UserId userId, TldServiceFacade tld) {
        super();
        this.userId = userId;
        this.tld = tld;
        this.addCriterion(WhoisContactSearchCriterion.WhoisContactNameLike, true);
        this.addCriterion(WhoisContactSearchCriterion.DomainNameLike, true);
    }

    /**
     * Prend n'importe quel format de nom de domaine en entrée mais ne conserve que le format LDH.
     */
    public void setDomainName(String domainName) throws ServiceException {
        if (domainName != null) {
            if ("undefined".equals(domainName)) {
                domainName = null;
            } else {
                try {
                    domainName = AppServiceFacade.getQualityService().normalizeDomainName(domainName.trim(), this.userId, this.tld).getLdh();
                } catch (IllegalDomainNameException e) {
                    LOGGER.warn("setDomainName():" + domainName + " is not a valid domainName");
                } catch (ServiceException e) {
                    LOGGER.error("setDomainName() failed", e);
                }
            }
        }
        this.addCriterion(WhoisContactSearchCriterion.DomainName, domainName);
    }

    public String getDomainName() {
        return (String) this.getCriterionValue(WhoisContactSearchCriterion.DomainName);
    }

    public String getWhoisContactNicHandle() {
        return (String) this.getCriterionValue(WhoisContactSearchCriterion.WhoisContactNicHandle);
    }

    public void setWhoisContactName(String name) throws ServiceException {
        this.addCriterion(WhoisContactSearchCriterion.WhoisContactName, name);
    }

    public String getWhoisContactName() {
        return (String) this.getCriterionValue(WhoisContactSearchCriterion.WhoisContactName);
    }

    public void setWhoisContactNicHandle(String handle) throws ServiceException {
        if (handle != null) {
            handle = handle.toUpperCase().trim();
        }
        this.addCriterion(WhoisContactSearchCriterion.WhoisContactNicHandle, handle);
    }

    public String getWhoisContactIdentifier() {
        return (String) this.getCriterionValue(WhoisContactSearchCriterion.WhoisContactIdentifier);
    }

    public void setWhoisContactIdentifier(String handle) throws ServiceException {
        this.addCriterion(WhoisContactSearchCriterion.WhoisContactIdentifier, handle);
    }

    public void setWhoisContactNameLike(boolean like) {
        this.addCriterion(WhoisContactSearchCriterion.WhoisContactNameLike, like);
    }

    public boolean getDomainNameLike() {
        return (Boolean) this.getCriterionValue(WhoisContactSearchCriterion.DomainNameLike);
    }

    public void setDomainNameLike(boolean like) {
        this.addCriterion(WhoisContactSearchCriterion.DomainNameLike, like);
    }

    public boolean getWhoisContactNameLike() {
        return (Boolean) this.getCriterionValue(WhoisContactSearchCriterion.WhoisContactNameLike);
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

}
