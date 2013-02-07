package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.IUserService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldUserService implements IUserService {

    protected MultiTldUserService() {
        super();
    }

    @Override
    public List<User> getUsers(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getUserService().getUsers(userId, tld);
    }

    @Override
    public User authenticate(String login, String pwd) throws ServiceException {
        return TldServiceFacade.Fr.getServiceProvider().getUserService().authenticate(login, pwd);
    }

    @Override
    public User getUser(String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getUserService().getUser(login, userId, tld);
    }

    @Override
    public User getUser(UserId userid, UserId userIdCaller, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getUserService().getUser(userid, userIdCaller, tld);
    }

    @Override
    public String getUserLogin(String nicopeId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getUserService().getUserLogin(nicopeId, userId, tld);
    }

    @Override
    public User getUserWithNicpersId(String nicopeId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getUserService().getUserWithNicpersId(nicopeId, userId, tld);
    }

    @Override
    public void addUser(User user, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getUserService().addUser(user, userId, tld);
    }

    @Override
    public void removeUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getUserService().removeUser(userLogin, userId, tld);
    }

    @Override
    public boolean isKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getUserService().isKnownUser(userLogin, userId, tld);
    }

    @Override
    public boolean isNotKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getUserService().isKnownUser(userLogin, userId, tld);
    }

    @Override
    public List<String> getAccountManagersLogin(UserRight userRight, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getUserService().getAccountManagersLogin(userRight, userId, tld);
    }

    @Override
    public User createAndGetUser(User user, UserId createUserId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getUserService().createAndGetUser(user, createUserId, tld);
    }

    @Override
    public void updatePassord(UserId userId, String newPassword, UserId updater, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getUserService().updatePassord(userId, newPassword, updater, tld);
    }
}
