package fr.afnic.commons.services.proxy;

import java.util.List;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.AuthorizationSearchCriteria;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IAuthorizationService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class ProxyAuthorizationService extends ProxyService<IAuthorizationService> implements IAuthorizationService {

    protected ProxyAuthorizationService() {
        super();
    }

    protected ProxyAuthorizationService(IAuthorizationService delegationService) {
        super(delegationService);
    }

    @Override
    public Authorization getAuthorizationWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizationWithId(id, userId, tld);
    }

    @Override
    public List<Authorization> getAuthorizations(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizations(userId, tld);
    }

    @Override
    public List<Authorization> getAuthorizations(String domain, String registrarHandle, String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizations(domain, registrarHandle, holderHandle, userId, tld);
    }

    @Override
    public List<Authorization> getAuthorizations(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getAuthorizations(domain, userId, tld);
    }

    @Override
    public int createAuthorization(Authorization authorization, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().createAuthorization(authorization, userId, tld);
    }

    @Override
    public void updateAuthorization(Authorization authorization, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().updateAuthorization(authorization, userId, tld);
    }

    @Override
    public Authorization getUsableAuthorization(String domain, String registrarCode, String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getUsableAuthorization(domain, registrarCode, holderHandle, userId, tld);
    }

    @Override
    public List<Authorization> searchAuthorization(AuthorizationSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().searchAuthorization(criteria, userId, tld);
    }

    @Override
    public void invalidateAuthorization(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().invalidateAuthorization(id, userId, tld);
    }

}
