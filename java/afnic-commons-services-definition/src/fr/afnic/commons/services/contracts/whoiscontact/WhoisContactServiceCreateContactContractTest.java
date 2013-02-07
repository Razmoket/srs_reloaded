package fr.afnic.commons.services.contracts.whoiscontact;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.ContactIdentificationStatus;
import fr.afnic.commons.beans.CorporateEntityWhoisContact;
import fr.afnic.commons.beans.IndividualWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationItemStatus;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.EmailAddressGenerator;
import fr.afnic.commons.test.generator.PhoneNumberGenerator;
import fr.afnic.commons.test.generator.PostalAddressGenerator;
import fr.afnic.commons.test.generator.RegistrarGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * Valide le comportement de la méthode createContact d'une implémentation du service IContactService.
 * 
 * @author ginguene
 * 
 */
public class WhoisContactServiceCreateContactContractTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContactWithNullContact() throws Exception {
        AppServiceFacade.getWhoisContactService().createContact(null, "guest", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContactWithNullUser() throws Exception {
        IndividualWhoisContact contact = new IndividualWhoisContact(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        contact.setFirstName("bob");
        contact.setLastName("Morane");

        AppServiceFacade.getWhoisContactService().createContact(contact, null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    /**
     * On ne peut pas créer un contact sans un login de user connu.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContactWithUnknownUser() throws Exception {
        IndividualWhoisContact contact = new IndividualWhoisContact(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        contact.setFirstName("bob");
        contact.setLastName("Morane");
        AppServiceFacade.getWhoisContactService().createContact(contact, "unknonwUser", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    /**
     * On ne peut pas créer un contact sans un login de user connu.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateIndividualContactWithIdentificationStatus() throws Exception {
        IndividualWhoisContact contact = new IndividualWhoisContact(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        contact.setFirstName("bob");
        contact.setLastName("Morane");
        contact.setIdentificationStatus(ContactIdentificationStatus.Ok);

        AppServiceFacade.getWhoisContactService().createContact(contact, "unknonwUser", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIndividualContactWithoutRegistrar() throws Exception {
        IndividualWhoisContact contact = new IndividualWhoisContact(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        contact.setFirstName("bob");
        contact.setLastName("Morane");

        WhoisContact createdContact = AppServiceFacade.getWhoisContactService().createContact(contact, "guest", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        WhoisContact gotContact = AppServiceFacade.getWhoisContactService().getContactWithHandle(createdContact.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(createdContact.getHandle(), gotContact.getHandle());
    }

    @Test
    public void isExistingNicHandleWithNull() throws ServiceException, ServiceFacadeException {
        try {
            AppServiceFacade.getWhoisContactService().isExistingNicHandle(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("nicHandle cannot be null.", e.getMessage());
        }
    }

    @Test
    public void isExistingNicHandleWithInvalidFormat() throws ServiceException, ServiceFacadeException {
        String invalidNicHandle = "ToTo";
        try {
            AppServiceFacade.getWhoisContactService().isExistingNicHandle(invalidNicHandle, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("nicHandle: '" + invalidNicHandle + "' is not a valid nicHandle.", e.getMessage());
        }
    }

    @Test
    public void isExistingNicHandleWithNonExisting() throws ServiceException, ServiceFacadeException {
        String invalidNicHandle = "ABC123-FRNIC";
        TestCase.assertFalse(AppServiceFacade.getWhoisContactService().isExistingNicHandle(invalidNicHandle, UserGenerator.getRootUserId(), TldServiceFacade.Fr));

    }

    @Test
    public void isExistingNicHandleWithExisting() throws ServiceException, ServiceFacadeException {
        String invalidNicHandle = "VL123-FRNIC";
        TestCase.assertTrue(AppServiceFacade.getWhoisContactService().isExistingNicHandle(invalidNicHandle, UserGenerator.getRootUserId(), TldServiceFacade.Fr));

    }

    @Test
    public void getListHolderWithCustomerIdForW4WithExisting() throws ServiceException, ServiceFacadeException {
        String validCustomer = "RENATER";
        List<WhoisContact> list = AppServiceFacade.getWhoisContactService().getHoldersWithRegistrarCode(validCustomer, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(list.get(0));
    }

    @Test
    public void getListHolderWithCustomerIdForW4WithNotExisting() throws ServiceException, ServiceFacadeException {
        String invalidCustomer = "RENAR";
        List<WhoisContact> list = AppServiceFacade.getWhoisContactService().getHoldersWithRegistrarCode(invalidCustomer, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(list.size(), 0);
    }

    @Test
    public void getListHolderWithCustomerIdForW4WithNull() throws ServiceException, ServiceFacadeException {
        try {
            AppServiceFacade.getWhoisContactService().getHoldersWithRegistrarCode(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("customerId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateIndividualContact() throws Exception {
        IndividualWhoisContact contact = new IndividualWhoisContact(UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        PostalAddress postalAddress = PostalAddressGenerator.generateRandomPostalAddressInParis();

        contact.setFirstName("bob");
        contact.setLastName("leponge");
        contact.setRegistrarCode(RegistrarGenerator.CODE_TEST);
        contact.setPostalAddress(postalAddress);

        WhoisContact createdContact = AppServiceFacade.getWhoisContactService().createContact(contact, "guest@afnic.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        String handle = createdContact.getHandle();
        WhoisContact gettedContact = AppServiceFacade.getWhoisContactService().getContactWithHandle(handle, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertEquals(createdContact.getHandle(), gettedContact.getHandle());

        TestCase.assertNull(handle, gettedContact.getIdentificationDate());
        TestCase.assertNull(handle, contact.getIdentificationExpirationDate());
        TestCase.assertNull(handle, gettedContact.getReachabilityQualificationDate());
        TestCase.assertNull(handle, contact.getReachMedia());

        TestCase.assertEquals(handle, PublicQualificationItemStatus.NotIdentified, gettedContact.getReachStatus());
        TestCase.assertEquals(handle, PublicQualificationItemStatus.NotIdentified, gettedContact.getEligibilityStatus());
        TestCase.assertEquals(handle, ContactIdentificationStatus.NotIdentified, gettedContact.getIdentificationStatus());
    }

    @Test
    public void testCreateCorporateEntityContact() throws Exception {
        CorporateEntity company = ContactGenerator.getCompanyWithTradeMark();

        CorporateEntityWhoisContact contact = new CorporateEntityWhoisContact(company, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        contact.setName("Sarl test");
        contact.setRegistrarCode(RegistrarGenerator.CODE_TEST);
        CorporateEntityWhoisContact corporateEntityContact = new CorporateEntityWhoisContact(company, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        corporateEntityContact.setPostalAddress(PostalAddressGenerator.generateRandomPostalAddressInParis());
        corporateEntityContact.addFaxNumber(PhoneNumberGenerator.getFaxNumber());
        corporateEntityContact.addPhoneNumber(PhoneNumberGenerator.getPhoneNumber());
        corporateEntityContact.addEmailAddress(EmailAddressGenerator.getEmailAddress());

        WhoisContact createdContact = AppServiceFacade.getWhoisContactService().createContact(contact, "guest@afnic.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        String handle = createdContact.getHandle();
        WhoisContact gettedContact = AppServiceFacade.getWhoisContactService().getContactWithHandle(handle, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertEquals(createdContact.getHandle(), gettedContact.getHandle());

        TestCase.assertNull(handle, gettedContact.getIdentificationDate());
        TestCase.assertNull(handle, gettedContact.getIdentificationExpirationDate());
        TestCase.assertNull(handle, gettedContact.getReachabilityQualificationDate());
        TestCase.assertNull(handle, gettedContact.getReachMedia());

        TestCase.assertEquals(handle, PublicQualificationItemStatus.NotIdentified, gettedContact.getReachStatus());
        TestCase.assertEquals(handle, PublicQualificationItemStatus.NotIdentified, gettedContact.getEligibilityStatus());
        TestCase.assertEquals(handle, ContactIdentificationStatus.NotIdentified, gettedContact.getIdentificationStatus());
    }

}
