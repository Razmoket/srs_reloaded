package fr.afnic.commons.services.multitld;

import fr.afnic.commons.beans.domain.DomainNameDetail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IQualityService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldQualityService implements IQualityService {

    protected MultiTldQualityService() {
        super();
    }

    @Override
    public DomainNameDetail normalizeDomainName(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualityService().normalizeDomainName(domain, userId, tld);
    }

    @Override
    public boolean isLegalDomainName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getQualityService().isLegalDomainName(domainName, userId, tld);
    }
}
