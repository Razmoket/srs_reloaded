package fr.afnic.commons.services.contracts.account;

import junit.framework.Assert;

import org.junit.Test;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class AccountServiceGetUserContractTest {

    @Test
    public void testGetUserWithValidUser() throws ServiceException {
        String userLogin = "guest";
        String userId = "GU";

        User user = AppServiceFacade.getAccountService().getUser(userLogin, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertEquals(userLogin, user.getLogin());
        Assert.assertEquals(userId, user.getNicpersId());

        UserProfile profile = user.getProfile();
        Assert.assertTrue(profile.hasRight(UserRight.Viewer));
    }
}
