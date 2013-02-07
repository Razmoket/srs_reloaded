/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.contactdetails;

import junit.framework.Assert;

import org.junit.Test;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class CountryTest {

    @Test
    public void testEquals() {
        Assert.assertTrue(new Country(1, "FR", new UserId(22), TldServiceFacade.Fr).equals(new Country(1, "FR", new UserId(22), TldServiceFacade.Fr)));

        Assert.assertFalse(new Country(1, "FR", new UserId(22), TldServiceFacade.Fr).equals(null));
        Assert.assertFalse(new Country(1, "FR", new UserId(22), TldServiceFacade.Fr).equals(new Object()));
        Assert.assertFalse(new Country(1, "FR", new UserId(22), TldServiceFacade.Fr).equals(new Country(1, "FA", new UserId(22), TldServiceFacade.Fr)));
        Assert.assertFalse(new Country(1, "FR", new UserId(22), TldServiceFacade.Fr).equals(new Country(1, "fr", new UserId(22), TldServiceFacade.Fr)));
    }

}
