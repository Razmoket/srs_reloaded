package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.IUserService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * UserService de bouchonnage. <br/>
 * Les users sont stockés dans une List
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class MockUserService implements IUserService {

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(IUserService.class);

    private final HashMap<String, User> usersMap = new HashMap<String, User>();

    private final HashMap<Integer, User> userIdMap = new HashMap<Integer, User>();

    private static int nextUserId = 2;

    public MockUserService() throws ServiceException {
        User defaultUser = new User(new UserId(1), TldServiceFacade.Fr);
        defaultUser.setLogin("guest@afnic.fr");
        defaultUser.setPassword("password");
        defaultUser.setNicpersId("GU");

        UserProfile profile = new UserProfile();

        for (UserRight right : UserRight.values()) {
            profile.addRight(right);
        }
        defaultUser.setProfile(profile);
        this.saveUser(defaultUser);

        // Le but n'est pas de passer à la postérité mais beaucoup de tests font encore appel aux UserGenerator
        // qui fait réfénrece à ce user qui à l'avantage de fonctionner.
        // A terme cela doit etre enlevé.
        User ginguene = new User(new UserId(1), TldServiceFacade.Fr);

        ginguene.setLogin(UserGenerator.ROOT_LOGIN);
        ginguene.setPassword("ginguene");
        ginguene.setNicpersId(UserGenerator.DEFAULT_NICOPE_USER_ID);
        ginguene.setNicpersLogin("ginguene");
        ginguene.setProfile(profile);
        this.saveUser(ginguene);

        /*
        User root = new User();
        root.setLogin("root");
        root.setPassword("root");
        root.setNicpersId("JG");
        root.setProfile(profile);
        root.setId(new UserId(23));

        this.saveUser(root);*/

        User visitorUser = new User(new UserId(1), TldServiceFacade.Fr);

        visitorUser.setLogin("visitorUser");
        visitorUser.setPassword("visitorUser");
        visitorUser.setNicpersId("VU");
        visitorUser.setProfile(profile);
        this.saveUser(visitorUser);

    }

    private UserId saveUser(User user) {
        this.usersMap.put(user.getLogin(), user);

        UserId userId = user.getId();
        if (userId == null) {
            userId = this.getNextUserId();
        }

        user.setId(userId);
        this.userIdMap.put(userId.getIntValue(), user);

        return userId;

    }

    private UserId getNextUserId() {
        return new UserId(nextUserId++);
    }

    @Override
    public User authenticate(String login, String pwd) throws ServiceException {
        if (this.usersMap == null) {
            throw new NotFoundException("User " + login + "/" + pwd + " cannot be found: no user list loaded");
        }

        User user = this.usersMap.get(login);
        boolean match = false;
        if (user != null) {
            match = user.isValidPassword(pwd);
        }

        if (match) {
            return user;
        } else {
            throw new NotFoundException("User " + login + "/" + pwd + " not found");
        }
    }

    @Override
    public User getUser(String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.usersMap == null) throw new NotFoundException("User " + login + " cannot be found: no user list loaded");

        User user = this.usersMap.get(login);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundException("User " + login + " not found");
        }
    }

    @Override
    public String getUserLogin(String nicpersId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getUserWithNicpersId(nicpersId, userId, tld).getLogin();
    }

    @Override
    public User getUserWithNicpersId(String nicpersId, UserId userId, TldServiceFacade tld) throws ServiceException {
        for (User user : this.usersMap.values()) {
            if (user.getNicpersId().equals(nicpersId)) {
                return user;
            }
        }
        throw new NotFoundException("User with nicope id '" + nicpersId + "' not found");
    }

    @Override
    public List<User> getUsers(UserId userId, TldServiceFacade tld) {
        ArrayList<User> ret = new ArrayList<User>();
        ret.addAll(this.usersMap.values());
        return ret;
    }

    public void setUsers(List<User> users, UserId userId, TldServiceFacade tld) throws ServiceException {
        for (User user : users) {
            this.addUser(user, userId, tld);
        }
    }

    @Override
    public void addUser(User user, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            User copy = user.copy();
            this.saveUser(copy);
        } catch (Exception e) {
            throw new ServiceException("addUser() failed", e);
        }
    }

    /**
     * Méthode d'ajout rapide d'un user.<br/>
     * Le mot de passe est le login.
     * 
     * @param login
     * @return
     * @throws ServiceException
     */
    public User addUser(String login, UserId userId, TldServiceFacade tld, UserRight... userRights) throws ServiceException {
        User user = new User(userId, tld);
        user.setLogin(login);
        user.setPassword(login);
        if (userRights != null) {

            UserProfile profile = new UserProfile("Test");

            for (UserRight userRight : userRights) {
                profile.addRight(userRight);
            }
            user.setProfile(profile);
        }
        this.addUser(user, userId, tld);
        return user;
    }

    @Override
    public boolean isKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.usersMap.containsKey(userLogin);
    }

    @Override
    public boolean isNotKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return !this.isKnownUser(userLogin, userId, tld);
    }

    @Override
    public void removeUser(String userLogin, UserId userId, TldServiceFacade tld) {
        this.usersMap.remove(userLogin);

    }

    @Override
    public List<String> getAccountManagersLogin(UserRight userRight, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<String> logins = new ArrayList<String>();
        for (User user : this.usersMap.values()) {
            if (user.hasRight(userRight)) {
                logins.add(user.getLogin());
            }
        }
        return logins;
    }

    @Override
    public User getUser(UserId userId, UserId userIdCaller, TldServiceFacade tld) throws ServiceException {
        User user = this.userIdMap.get(userId.getIntValue());
        if (user == null) {
            throw new NotFoundException("No user found with id " + userId);
        } else {
            return user;
        }
    }

    @Override
    public User createAndGetUser(User user, UserId createUserId, TldServiceFacade tld) throws ServiceException {
        return this.getUser(this.saveUser(user), createUserId, tld);
    }

    @Override
    public void updatePassord(UserId userId, String newPassword, UserId updater, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();

    }

}
