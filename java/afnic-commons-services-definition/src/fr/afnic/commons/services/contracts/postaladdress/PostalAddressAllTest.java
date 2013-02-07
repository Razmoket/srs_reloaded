package fr.afnic.commons.services.contracts.postaladdress;

import junit.framework.TestCase;
import junitx.framework.ComparableAssert;

import org.junit.Test;

import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class PostalAddressAllTest {

    @Test
    public void createAndGet() throws ServiceException {
        PostalAddress postalAddress1 = new PostalAddress(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        postalAddress1.setCity("Paris");
        postalAddress1.setCountryCode("FR");
        postalAddress1.setPostCode("78340");
        postalAddress1.setStreet("1 rue de la mare", "au fond à gauche");
        postalAddress1.setOrganization("my org");
        postalAddress1.setCreateUserId(new UserId(1));

        PostalAddressId newId = AppServiceFacade.getPostalAddressService().create(postalAddress1, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertNotNull(newId);
        ComparableAssert.assertGreater(0, newId.getIntValue());
        PostalAddress postalAddress2 = AppServiceFacade.getPostalAddressService().getPostalAddress(newId, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(postalAddress2);

        TestCase.assertEquals(postalAddress1.getCity(), postalAddress2.getCity());
        TestCase.assertEquals(postalAddress1.getCountryCode(), postalAddress2.getCountryCode());
        TestCase.assertEquals(postalAddress1.getStreetStr(), postalAddress2.getStreetStr());
        TestCase.assertEquals(postalAddress1.getOrganization(), postalAddress2.getOrganization());
        TestCase.assertEquals(postalAddress1.getPostCode(), postalAddress2.getPostCode());

    }

    @Test
    public void update() throws ServiceException {
        PostalAddress postalAddress1 = new PostalAddress(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        postalAddress1.setCity("Paris");
        postalAddress1.setCountryCode("FR");
        postalAddress1.setPostCode("75015");
        postalAddress1.setStreet("1 rue de la mare", "au fond à gauche");
        postalAddress1.setOrganization("my org");
        postalAddress1.setCreateUserId(new UserId(1));

        PostalAddressId newId = AppServiceFacade.getPostalAddressService().create(postalAddress1, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertNotNull(newId);
        ComparableAssert.assertGreater(0, newId.getIntValue());
        PostalAddress postalAddress2 = AppServiceFacade.getPostalAddressService().getPostalAddress(newId, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(postalAddress2);

        postalAddress2.setCity("London");
        postalAddress2.setCountryCode("GB");
        postalAddress2.setPostCode("88000");
        postalAddress2.setStreet("3 ducks's street", "on the left");
        postalAddress2.setOrganization("my new org");

        AppServiceFacade.getPostalAddressService().update(postalAddress2, new UserId(2), TldServiceFacade.Fr);

        PostalAddress postalAddress3 = AppServiceFacade.getPostalAddressService().getPostalAddress(newId, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertEquals(postalAddress2.getCity(), postalAddress3.getCity());
        TestCase.assertEquals(postalAddress2.getCountryCode(), postalAddress3.getCountryCode());
        TestCase.assertEquals(postalAddress2.getStreetStr(), postalAddress3.getStreetStr());
        TestCase.assertEquals(postalAddress2.getOrganization(), postalAddress3.getOrganization());
        TestCase.assertEquals(postalAddress2.getPostCode(), postalAddress3.getPostCode());

    }
}
