package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.beans.request.Request;
import fr.afnic.commons.services.exception.AuthorizationRequestNotFoundException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.proxy.ProxyAuthorizationRequestService;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.filters.Filter;

public class MockAuthorizationRequestService extends ProxyAuthorizationRequestService {

    /** Clé: id de l'autorisation; Valeur: Authorization */
    public final static Map<Integer, AuthorizationRequest> MAP = new HashMap<Integer, AuthorizationRequest>();

    private static int LAST_ID = 1;

    @Override
    public void updateAuthorizationRequest(AuthorizationRequest authorizationRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        AppServiceFacade.getRequestService().history(authorizationRequest, userId, tld);
        authorizationRequest.setLastStatusUpdate(new Date());
        MAP.put(authorizationRequest.getId(), authorizationRequest.copy());
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomain(final String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        Filter<AuthorizationRequest> filter = new Filter<AuthorizationRequest>() {
            @Override
            public boolean test(AuthorizationRequest element) {
                return Objects.equal(domainName, element.getRequestedDomainName());
            }
        };

        return filter.findElements(MAP.values());
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomainLike(final String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        Filter<AuthorizationRequest> filter = new Filter<AuthorizationRequest>() {
            @Override
            public boolean test(AuthorizationRequest element) {
                return Objects.equal(domainName, element.getRequestedDomainName());
            }
        };

        return filter.findElements(MAP.values());
    }

    @Override
    public AuthorizationRequest getRequestToLinkWithDocument(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            Request request = AppServiceFacade.getRequestService().getLinkedRequestToDocumentWithHandle(document.getHandle(), userId, tld);
            if (request instanceof AuthorizationRequest) {
                return (AuthorizationRequest) request;
            } else {
                return null;
            }
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Override
    public AuthorizationRequest getAuthorizationRequestWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        AuthorizationRequest authorizationRequest = MAP.get(id);
        if (authorizationRequest == null) {
            throw new AuthorizationRequestNotFoundException(id);
        } else {
            authorizationRequest.setDocuments(null);
            return authorizationRequest;
        }
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequests(UserId userId, TldServiceFacade tld) throws ServiceException {
        return new ArrayList<AuthorizationRequest>(MAP.values());
    }

    @Override
    public int createAuthorizationRequest(AuthorizationRequest authorizationRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        int id = LAST_ID++;
        authorizationRequest.setId(id);
        MAP.put(id, authorizationRequest.copy());
        return id;
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsInNonFinalStatus(UserId userId, TldServiceFacade tld) throws ServiceException {
        Filter<AuthorizationRequest> filter = new Filter<AuthorizationRequest>() {
            @Override
            public boolean test(AuthorizationRequest element) {
                return element.hasNotFinalStatus();
            }
        };
        return filter.findElements(MAP.values());
    }

    @Override
    public List<AuthorizationRequest> getCreatedRequestsBewteenTwoDates(final Date start, final Date end, UserId userId, TldServiceFacade tld) throws ServiceException {

        Filter<AuthorizationRequest> filter = new Filter<AuthorizationRequest>() {
            @Override
            public boolean test(AuthorizationRequest element) {
                return start.before(element.getCreateDate())
                       && end.after(element.getCreateDate());
            }
        };
        return filter.findElements(MAP.values());
    }

    public void changeAuthorizationRequestCreateDate(int requestId, Date newDate) {
        MAP.get(requestId).setCreateDate(newDate);
    }

    public void changeAuthorizationRequestLastUpdate(int requestId, Date newDate) {
        MAP.get(requestId).setLastStatusUpdate(newDate);
    }

}
