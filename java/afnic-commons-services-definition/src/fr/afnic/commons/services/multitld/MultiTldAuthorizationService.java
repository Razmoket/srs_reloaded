package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.AuthorizationSearchCriteria;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IAuthorizationService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldAuthorizationService implements IAuthorizationService {

    protected MultiTldAuthorizationService() {
        super();
    }

    @Override
    public Authorization getAuthorizationWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationService().getAuthorizationWithId(id, userId, tld);
    }

    @Override
    public List<Authorization> getAuthorizations(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationService().getAuthorizations(userId, tld);
    }

    @Override
    public List<Authorization> getAuthorizations(String domain, String registrarHandle, String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationService().getAuthorizations(domain, registrarHandle, holderHandle, userId, tld);
    }

    @Override
    public List<Authorization> getAuthorizations(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationService().getAuthorizations(domain, userId, tld);
    }

    @Override
    public Authorization getUsableAuthorization(String domain, String registrarCode, String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationService().getUsableAuthorization(domain, registrarCode, holderHandle, userId, tld);
    }

    @Override
    public List<Authorization> searchAuthorization(AuthorizationSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationService().searchAuthorization(criteria, userId, tld);
    }

    @Override
    public void invalidateAuthorization(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getAuthorizationService().invalidateAuthorization(id, userId, tld);
    }

    @Override
    public int createAuthorization(Authorization authorization, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAuthorizationService().createAuthorization(authorization, userId, tld);
    }

    @Override
    public void updateAuthorization(Authorization authorization, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getAuthorizationService().updateAuthorization(authorization, userId, tld);
    }

}
