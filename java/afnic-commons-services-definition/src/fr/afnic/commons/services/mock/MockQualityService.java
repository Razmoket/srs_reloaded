package fr.afnic.commons.services.mock;

import fr.afnic.commons.beans.domain.DomainNameDetail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IQualityService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockQualityService implements IQualityService {

    @Override
    public DomainNameDetail normalizeDomainName(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        DomainNameDetail detail = new DomainNameDetail();
        detail.setLdh(domain);
        detail.setAsciiEquivalent(domain);
        detail.setUtf8(domain);
        return detail;
    }

    @Override
    public boolean isLegalDomainName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        return true;
    }

}
