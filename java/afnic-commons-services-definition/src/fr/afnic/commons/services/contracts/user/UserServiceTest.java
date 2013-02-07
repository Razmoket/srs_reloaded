/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.user;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class UserServiceTest {

    private static final int ROOT_PROFILE_ID = 5;
    private static final int MARKETING_PROFILE_ID = 8;

    @Test
    public void testGetUserWithValidUser() throws Exception {
        String userLogin = UserGenerator.ROOT_LOGIN;
        String userId = UserGenerator.DEFAULT_NICOPE_USER_ID;

        User user = AppServiceFacade.getUserService().getUser(userLogin, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(userLogin, user.getLogin());
        TestCase.assertEquals(userId, user.getNicpersId());

        UserProfile profile = user.getProfile();
        TestCase.assertNotNull(profile);
        TestCase.assertTrue(profile.hasRight(UserRight.Viewer));
    }

    @Test(expected = NotFoundException.class)
    public void testGetUserWithValidUserWithUnknownUser() throws Exception {
        AppServiceFacade.getUserService().getUser("unLoginQuiNExistePas", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void testGetUserWithRootLogin() throws Exception {
        AppServiceFacade.getUserService().getUser(UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void testGetUserWithId() throws Exception {
        AppServiceFacade.getUserService().getUser(new UserId(1), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void testGetUserWithProcessLogin() throws Exception {
        AppServiceFacade.getUserService().getUser("boa@afnic.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void testGetProfileWithRootId() throws Exception {
        UserProfile profile = AppServiceFacade.getProfileService().getProfileWithId(ROOT_PROFILE_ID, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals("Root", profile.getName());
        TestCase.assertEquals(ROOT_PROFILE_ID, profile.getId());
    }

    @Test
    public void testGetProfileRight() throws Exception {
        List<UserRight> rootRights = AppServiceFacade.getProfileService().getProfileRight(ROOT_PROFILE_ID, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertTrue(rootRights.contains(UserRight.AuthorizationWrite));

        List<UserRight> marketingRights = AppServiceFacade.getProfileService().getProfileRight(MARKETING_PROFILE_ID, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertTrue(marketingRights.contains(UserRight.DomainRead));
        TestCase.assertFalse(marketingRights.contains(UserRight.AuthorizationWrite));

    }

}
