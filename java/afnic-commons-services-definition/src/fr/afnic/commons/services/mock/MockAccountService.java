package fr.afnic.commons.services.mock;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IAccountService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockAccountService implements IAccountService {

    @Override
    public User getUser(String login, String pwd, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getUserService().authenticate(login, pwd);
    }

    @Override
    public User getUser(String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getUserService().getUser(login, userId, tld);
    }

    @Override
    public void addUser(User user, UserId userId, TldServiceFacade tld) throws ServiceException {
        AppServiceFacade.getUserService().addUser(user, userId, tld);
    }

    @Override
    public void removeUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        AppServiceFacade.getUserService().removeUser(userLogin, userId, tld);
    }

    @Override
    public boolean isKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getUserService().isKnownUser(userLogin, userId, tld);
    }

    @Override
    public boolean isNotKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getUserService().isNotKnownUser(userLogin, userId, tld);
    }

}
