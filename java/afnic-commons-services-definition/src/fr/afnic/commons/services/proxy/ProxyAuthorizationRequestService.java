package fr.afnic.commons.services.proxy;

import java.util.Date;
import java.util.List;

import org.joda.time.Interval;

import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.services.IAuthorizationRequestService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class ProxyAuthorizationRequestService extends ProxyService<IAuthorizationRequestService> implements IAuthorizationRequestService {

    protected ProxyAuthorizationRequestService() {
        super();
    }

    protected ProxyAuthorizationRequestService(IAuthorizationRequestService delegationService) {
        super(delegationService);
    }

    @Override
    public AuthorizationRequest getAuthorizationRequestWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizationRequestWithId(id, userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequests(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizationRequests(userId, tld);
    }

    @Override
    public int createAuthorizationRequest(AuthorizationRequest authorizationRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().createAuthorizationRequest(authorizationRequest, userId, tld);
    }

    @Override
    public void updateAuthorizationRequest(AuthorizationRequest authorizationRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().updateAuthorizationRequest(authorizationRequest, userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizationRequestsWithDomain(domain, userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomainLike(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizationRequestsWithDomainLike(domain, userId, tld);
    }

    @Override
    public AuthorizationRequest getRequestToLinkWithDocument(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getRequestToLinkWithDocument(document, userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsInNonFinalStatus(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizationRequestsInNonFinalStatus(userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getCreatedRequestsBewteenTwoDates(Date start, Date end, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getCreatedRequestsBewteenTwoDates(start, end, userId, tld);
    }

    @Override
    public Interval getSunrisePeriod(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getSunrisePeriod(userId, tld);
    }
}
