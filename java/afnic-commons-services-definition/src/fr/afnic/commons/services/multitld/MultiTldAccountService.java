package fr.afnic.commons.services.multitld;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IAccountService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldAccountService implements IAccountService {

    protected MultiTldAccountService() {
        super();
    }

    @Override
    public User getUser(String login, String pwd, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAccountService().getUser(login, pwd, userId, tld);
    }

    @Override
    public User getUser(String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAccountService().getUser(login, userId, tld);
    }

    @Override
    public void addUser(User user, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getAccountService().addUser(user, userId, tld);
    }

    @Override
    public void removeUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getAccountService().removeUser(userLogin, userId, tld);
    }

    @Override
    public boolean isKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAccountService().isKnownUser(userLogin, userId, tld);
    }

    @Override
    public boolean isNotKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAccountService().isNotKnownUser(userLogin, userId, tld);
    }
}
