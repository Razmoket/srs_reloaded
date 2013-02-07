/*
 * $Id: UserGenerator.java,v 1.4 2010/08/17 14:49:21 ginguene Exp $
 * $Revision: 1.4 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public final class UserGenerator {

    /** Id dans nicpers, utilisé notamment lors des accès à certaines couches basses */
    public static final String DEFAULT_NICOPE_USER_ID = "JG";

    public static final String ROOT_LOGIN = "jerome.ginguene@afnic.fr";

    public static final String UNKNOWN_LOGIN = "bouba";

    public static final int UNKNOWN_USER_ID = 999999999;

    /** Nombre de user générés */
    private static int userCount = 0;

    public static UserId getRootUserId() throws ServiceException {
        return new UserId(1);//AppServiceFacade.getUserService().getUser(ROOT_LOGIN, new UserId(1), TldServiceFacade.Fr).getId();

    }

    /**
     * Retourne un user avec le profil Root.
     * 
     * @return
     * @throws ServiceException
     */
    public static User generateRootUser() throws ServiceException {
        return AppServiceFacade.getUserService().getUser(UserGenerator.ROOT_LOGIN, new UserId(22), TldServiceFacade.Fr);
    }

    /**
     * Retourne un user avec le profil Root.
     * 
     * @return
     * @throws ServiceException
     */
    public static User generateGddUser() throws ServiceException {
        return AppServiceFacade.getUserService().getUser("gddUser1", new UserId(22), TldServiceFacade.Fr);
    }

    /**
     * Retourne un user avec le profil Root.
     * 
     * @return
     * @throws ServiceException
     */
    public static User generateAnotherGddUser() throws ServiceException {
        return AppServiceFacade.getUserService().getUser("gddUser2", new UserId(22), TldServiceFacade.Fr);
    }

    /**
     * Retourne un user avec le profil Root.
     * 
     * @return
     * @throws ServiceException
     */
    public static User generateVisitorUser() throws ServiceException {
        return AppServiceFacade.getUserService().getUser("guest@afnic.fr", new UserId(22), TldServiceFacade.Fr);
    }

    public static User generateUserWithProfile(UserProfile profile) throws ServiceException {
        User user = UserGenerator.generateUser();
        user.setProfile(profile);
        return user;
    }

    /**
     * Génère un pro
     * 
     * @param rights
     * @return
     * @throws ServiceException
     */
    public static User generateUserWithRights(UserRight... rights) throws ServiceException {
        User user = UserGenerator.generateUser();

        UserProfile profile = new UserProfile("Test");

        for (UserRight right : rights) {
            profile.addRight(right);
        }

        user.setProfile(profile);
        return user;
    }

    private static User generateUser() throws ServiceException {
        User user = new User(new UserId(22), TldServiceFacade.Fr);
        user.setNicpersId(UserGenerator.DEFAULT_NICOPE_USER_ID);
        user.setLogin(UserGenerator.generateUserLogin());
        user.setPassword("pwd");
        AppServiceFacade.getUserService().addUser(user, new UserId(22), TldServiceFacade.Fr);
        return user;
    }

    private static String generateUserLogin() {
        return "userTest" + UserGenerator.userCount++;
    }

    public static User getSupportUser() throws ServiceException, ServiceFacadeException {
        return AppServiceFacade.getUserService().getUser("supportUser", new UserId(22), TldServiceFacade.Fr);
    }

    public static User getGddUser1() throws ServiceException, ServiceFacadeException {
        return AppServiceFacade.getUserService().getUser("gddUser1", new UserId(22), TldServiceFacade.Fr);
    }

    public static User getGddUser2() throws ServiceException, ServiceFacadeException {
        return AppServiceFacade.getUserService().getUser("gddUser2", new UserId(22), TldServiceFacade.Fr);
    }

    public static User getGddAdminUser() throws ServiceException, ServiceFacadeException {
        return AppServiceFacade.getUserService().getUser("gddAdminUser", new UserId(22), TldServiceFacade.Fr);
    }

    private UserGenerator() {

    }

}
