package fr.afnic.commons.services.multitld;

import java.util.Date;
import java.util.List;

import org.joda.time.Interval;

import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.services.IAuthorizationRequestService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldAuthorizationRequestService implements IAuthorizationRequestService {

    protected MultiTldAuthorizationRequestService() {
        super();
    }

    @Override
    public AuthorizationRequest getAuthorizationRequestWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationRequestService().getAuthorizationRequestWithId(id, userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequests(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationRequestService().getAuthorizationRequests(userId, tld);
    }

    @Override
    public int createAuthorizationRequest(AuthorizationRequest authorizationRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationRequestService().createAuthorizationRequest(authorizationRequest, userId, tld);
    }

    @Override
    public void updateAuthorizationRequest(AuthorizationRequest authorizationRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getAuthorizationRequestService().updateAuthorizationRequest(authorizationRequest, userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationRequestService().getAuthorizationRequestsWithDomain(domain, userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomainLike(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationRequestService().getAuthorizationRequestsWithDomainLike(domain, userId, tld);
    }

    @Override
    public AuthorizationRequest getRequestToLinkWithDocument(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationRequestService().getRequestToLinkWithDocument(document, userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsInNonFinalStatus(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationRequestService().getAuthorizationRequestsInNonFinalStatus(userId, tld);
    }

    @Override
    public List<AuthorizationRequest> getCreatedRequestsBewteenTwoDates(Date start, Date end, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationRequestService().getCreatedRequestsBewteenTwoDates(start, end, userId, tld);
    }

    @Override
    public Interval getSunrisePeriod(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationRequestService().getSunrisePeriod(userId, tld);
    }

}
