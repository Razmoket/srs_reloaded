/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.AuthorizationSearchCriteria;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.services.IAuthorizationService;
import fr.afnic.commons.services.exception.AuthorizationNotFoundException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.filters.Filter;

/**
 * Implémentation Mock du service IAuthorizationService.
 * 
 * @author ginguene
 * 
 */
public class MockAuthorizationService implements IAuthorizationService {

    /** Clé: id de l'autorisation; Valeur: Authorization */
    public static final Map<Integer, Authorization> MAP = new HashMap<Integer, Authorization>();

    private static int LAST_ID = 1;

    @Override
    public Authorization getAuthorizationWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        Authorization authorization = MAP.get(id);
        if (authorization == null) {
            throw new AuthorizationNotFoundException(id);
        } else {
            return authorization;
        }
    }

    @Override
    public List<Authorization> getAuthorizations(UserId userId, TldServiceFacade tld) throws ServiceException {
        return new ArrayList<Authorization>(MAP.values());
    }

    @Override
    public List<Authorization> getAuthorizations(final String domain, final String registrarCode, final String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        Filter<Authorization> filter = new Filter<Authorization>() {
            @Override
            public boolean test(Authorization element) {
                return Objects.equal(domain, element.getDomainName())
                       && Objects.equal(registrarCode, element.getRegistrarCode())
                       && Objects.equal(holderHandle, element.getHolderHandle());
            }
        };

        return filter.findElements(MAP.values());

    }

    @Override
    public List<Authorization> getAuthorizations(final String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        Filter<Authorization> filter = new Filter<Authorization>() {
            @Override
            public boolean test(Authorization element) {
                return Objects.equal(domain, element.getDomainName());
            }
        };
        return filter.findElements(MAP.values());
    }

    @Override
    public int createAuthorization(Authorization authorization, UserId userId, TldServiceFacade tld) throws ServiceException {
        int id = MockAuthorizationService.LAST_ID++;
        authorization.setId(id);
        MAP.put(id, authorization);

        // lien un peu fin mais qui suffit pour les tests, cela implique que l'on n'a pas plusieurs requetes d'autorisation pour un meme domaine

        List<AuthorizationRequest> requests = AppServiceFacade.getAuthorizationRequestService().getAuthorizationRequestsWithDomain(authorization.getDomainName(), userId, tld);
        if (requests.size() > 0) {
            AuthorizationRequest req = requests.get(0);
            MockAuthorizationRequestService.MAP.get(req.getId()).setAuthorizationId(id);
        }

        return id;
    }

    @Override
    public void updateAuthorization(Authorization authorization, UserId userId, TldServiceFacade tld) throws ServiceException {
        MAP.put(authorization.getId(), authorization);
    }

    @Override
    public Authorization getUsableAuthorization(final String domain, final String registrarCode, final String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        Filter<Authorization> filter = new Filter<Authorization>() {
            @Override
            public boolean test(Authorization element) {
                return Objects.equal(domain, element.getDomainName())
                       && Objects.equal(registrarCode, element.getRegistrarCode())
                       && Objects.equal(holderHandle, element.getHolderHandle())
                       && element.isActif()
                       && element.hasNotBeenUsed()
                       && element.getExpirationDate().after(new Date());
            }
        };

        return Preconditions.checkNotNull(filter.findFirstElement(MAP.values()),
                                          new NotFoundException("No usable authorization for domain " + domain
                                                                + ", registrarCode " + registrarCode
                                                                + ", holderHandle " + holderHandle));

    }

    public void changeAuthorizationDateOfUse(int authorizationId, Date newDate) {
        MAP.get(authorizationId).setUseDate(newDate);
    }

    public void changeAuthorizationValidityDate(int authorizationId, Date newDate) {
        MAP.get(authorizationId).setExpirationDate(newDate);
    }

    @Override
    public List<Authorization> searchAuthorization(AuthorizationSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void invalidateAuthorization(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub

    }

}
