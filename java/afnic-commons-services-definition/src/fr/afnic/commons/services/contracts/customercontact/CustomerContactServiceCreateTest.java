package fr.afnic.commons.services.contracts.customercontact;

import junit.framework.TestCase;
import junitx.framework.ComparableAssert;

import org.junit.Test;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.contact.CustomerContactStatus;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.contactdetails.PhoneNumber;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.utils.DateUtils;

public class CustomerContactServiceCreateTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullCustomerContact() throws Exception {
        AppServiceFacade.getCustomerContactService().createCustomerContact(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullCreateUserId() throws Exception {

        CustomerContact contact = CustomerContact.createIndividualCustomer(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        AppServiceFacade.getCustomerContactService().createCustomerContact(contact, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void testCreate() throws Exception {
        CustomerContact contact1 = CustomerContact.createIndividualCustomer(UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        contact1.getIndividualIdentity().setFirstName("Bob");
        contact1.getIndividualIdentity().setLastName("Leponge");
        contact1.getContactDetails().add(new PhoneNumber("0102030405"));
        contact1.getContactDetails().add(new PhoneNumber("9102030405"));
        contact1.getContactDetails().add(new EmailAddress("bob.leponge@gmail.com"));

        PostalAddress postalAddress1 = new PostalAddress(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        postalAddress1.setCity("Paris");
        postalAddress1.setCountryCode("FR");
        postalAddress1.setPostCode("78340");
        postalAddress1.setStreet("1 rue de la mare", "au fond à gauche");
        postalAddress1.setOrganization("my org");
        contact1.setPostalAddress(postalAddress1);

        contact1.setCustomerId(new CustomerId(59)); //TEST
        contact1.setCreateUserId(new UserId(1));

        CustomerContactId newId = AppServiceFacade.getCustomerContactService().createCustomerContact(contact1, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertNotNull(newId);
        ComparableAssert.assertGreater(0, newId.getIntValue());

        CustomerContact contact2 = AppServiceFacade.getCustomerContactService().getCustomerContact(newId, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(contact2);

        PostalAddress postalAddress2 = contact2.getPostalAddress();
        TestCase.assertNotNull(contact2);
        TestCase.assertEquals(postalAddress1.getCity(), postalAddress2.getCity());
        TestCase.assertEquals(postalAddress1.getCountryCode(), postalAddress2.getCountryCode());
        TestCase.assertEquals(postalAddress1.getStreetStr(), postalAddress2.getStreetStr());
        TestCase.assertEquals(postalAddress1.getOrganization(), postalAddress2.getOrganization());
        TestCase.assertEquals(postalAddress1.getPostCode(), postalAddress2.getPostCode());

        TestCase.assertEquals(CustomerContactStatus.Active, contact2.getStatus());
        TestCase.assertEquals(contact1.getIndividualIdentity().getFirstName(), contact2.getIndividualIdentity().getFirstName());
        TestCase.assertEquals(contact1.getIndividualIdentity().getLastName(), contact2.getIndividualIdentity().getLastName());
        TestCase.assertEquals(contact1.getFirstFaxNumber(), contact2.getFirstFaxNumber());
        TestCase.assertEquals(contact1.getFirstPhoneNumber(), contact2.getFirstPhoneNumber());
        TestCase.assertEquals(contact1.getFirstEmailAddress(), contact2.getFirstEmailAddress());
        TestCase.assertEquals(contact1.getCustomerId(), contact2.getCustomerId());

        TestCase.assertEquals(contact1.getCreateUserId(), contact2.getCreateUserId());

        TestCase.assertTrue(DateUtils.isToday(contact2.getCreateDate()));

        TestCase.assertEquals(0, contact2.getObjectVersion());

    }
}
