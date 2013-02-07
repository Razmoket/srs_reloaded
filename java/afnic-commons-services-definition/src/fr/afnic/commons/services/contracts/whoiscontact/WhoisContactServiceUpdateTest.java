package fr.afnic.commons.services.contracts.whoiscontact;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationItemStatus;
import fr.afnic.commons.beans.operations.qualification.PublicReachMedia;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;
import fr.afnic.utils.DateUtils;

public class WhoisContactServiceUpdateTest {

    @Test
    public void updateEligibilityStatus() throws GeneratorException, ServiceException {
        WhoisContact contact = ContactGenerator.createCorporateEntityContact();
        TestCase.assertNull(contact.getIdentificationDate());

        for (PublicQualificationItemStatus status : PublicQualificationItemStatus.values()) {
            contact.setEligibilityStatus(status);
            AppServiceFacade.getWhoisContactService().updateContact(contact, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.assertEquals(status, contact.getEligibilityStatus());
        }
    }

    @Test
    public void updateEligibilityStatusOk() throws GeneratorException, ServiceException {
        WhoisContact contact = ContactGenerator.createCorporateEntityContact();
        TestCase.assertNull(contact.getIdentificationDate());

        contact.setEligibilityStatus(PublicQualificationItemStatus.Ok);
        AppServiceFacade.getWhoisContactService().updateContact(contact, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        contact = AppServiceFacade.getWhoisContactService().getContactWithHandle(contact.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(contact.getIdentificationDate());
        TestCase.assertTrue(DateUtils.isToday(contact.getIdentificationDate()));

    }

    @Test
    public void updateReachStatus() throws GeneratorException, ServiceException {
        WhoisContact contact = ContactGenerator.createCorporateEntityContact();

        contact.setReachMedia(PublicReachMedia.Phone);

        for (PublicQualificationItemStatus status : PublicQualificationItemStatus.values()) {
            contact.setReachStatus(status);
            AppServiceFacade.getWhoisContactService().updateContact(contact, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

            contact = AppServiceFacade.getWhoisContactService().getContactWithHandle(contact.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

            TestCase.assertEquals(status, contact.getReachStatus());
        }
    }

    @Test
    public void updateReachStatusOk() throws GeneratorException, ServiceException {
        WhoisContact contact = ContactGenerator.createCorporateEntityContact();

        contact.setReachMedia(PublicReachMedia.Phone);
        contact.setReachStatus(PublicQualificationItemStatus.Ok);
        AppServiceFacade.getWhoisContactService().updateContact(contact, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        contact = AppServiceFacade.getWhoisContactService().getContactWithHandle(contact.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        //TestCase.assertNotNull(contact.getReachabilityQualificationDate());
        //TestCase.assertTrue(DateUtils.isToday(contact.getReachabilityQualificationDate()));
    }

    @Test
    public void updateInFinalReachStatusWithoutReachMedia() throws GeneratorException, ServiceException {

        WhoisContact contact = ContactGenerator.createCorporateEntityContact();

        contact.setReachMedia(null);
        contact.setReachStatus(PublicQualificationItemStatus.Ok);

        try {
            AppServiceFacade.getWhoisContactService().updateContact(contact, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown");
        } catch (ServiceException e) {
            TestCase.assertEquals("Media is mandatory when updating Reachability data to a final status", e.getFirstCauseMessage());

        }

    }

    @Test
    public void updateReachMedia() throws GeneratorException, ServiceException {
        WhoisContact contact = ContactGenerator.createCorporateEntityContact();

        for (PublicReachMedia media : PublicReachMedia.values()) {
            contact.setReachMedia(media);
            AppServiceFacade.getWhoisContactService().updateContact(contact, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.assertEquals(media, contact.getReachMedia());
        }
    }

}
