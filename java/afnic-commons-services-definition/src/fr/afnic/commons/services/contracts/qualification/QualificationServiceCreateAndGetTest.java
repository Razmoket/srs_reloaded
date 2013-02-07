package fr.afnic.commons.services.contracts.qualification;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.qualification.EligibilityStatus;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.QualificationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class QualificationServiceCreateAndGetTest {

    @Test
    public void getQualificationInProgressCountCount() throws ServiceException {
        int nbQualifAvantAjout = AppServiceFacade.getQualificationService().getQualificationInProgressCount(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        QualificationGenerator.createQualification();

        int nbQualifApresAjout = AppServiceFacade.getQualificationService().getQualificationInProgressCount(UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertTrue(nbQualifApresAjout > 0);
        TestCase.assertEquals(nbQualifAvantAjout + 1, nbQualifApresAjout);

    }

    @Test
    public void getQualificationInProgressWithValidNicHandle() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();
        String nicHandle = qualification.getNicHandle();
        Qualification qualificationInProgress = AppServiceFacade.getQualificationService().getQualificationInProgress(nicHandle, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertEquals(nicHandle, qualificationInProgress.getNicHandle());
        //OperationTestCase.assertCompositeOperation(qualification, qualificationInProgress);
    }

    @Test
    public void getQualificationInProgressWithNullNicHandle() throws ServiceException {
        try {
            AppServiceFacade.getQualificationService().getQualificationInProgress(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("nicHandle cannot be null.", e.getMessage());
        }
    }

    @Test
    public void getQualificationInProgressWithInvalidNicHandle() throws ServiceException {
        String nichandle = "Turlututu";
        try {
            AppServiceFacade.getQualificationService().getQualificationInProgress(nichandle, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("nicHandle: '" + nichandle + "' is not a valid nicHandle.", e.getMessage());
        }
    }

    @Test
    public void getQualificationInProgressWithNotFoundExistingNicHandle() throws ServiceException {
        WhoisContact contact = ContactGenerator.createCorporateEntityContact();

        String nichandle = contact.getHandle();
        try {
            AppServiceFacade.getQualificationService().getQualificationInProgress(nichandle, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (NotFoundException e) {
            TestCase.assertEquals("No qualification in progress found with nichandle[" + nichandle + "].", e.getMessage());
        }
    }

    @Test
    public void createAndGetQualification() throws ServiceException {
        Qualification qualification = QualificationGenerator.generateQualification();
        OperationId create = AppServiceFacade.getQualificationService().create(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Qualification actual = AppServiceFacade.getQualificationService().getQualification(create, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(actual.getHolderSnapshotId());
        TestCase.assertEquals(PortfolioStatus.Active, actual.getPortfolioStatus());
        TestCase.assertEquals(EligibilityStatus.NotIdentified, actual.getEligibilityStatus());
        TestCase.assertEquals(ReachStatus.NotIdentified, actual.getReachStatus());
        TestCase.assertEquals(TopLevelOperationStatus.Pending, actual.getTopLevelStatus());

    }

    @Test
    public void createQualificationWithNullSource() throws ServiceException {
        Qualification qualification = QualificationGenerator.generateQualification();

        qualification.setSource(null);

        try {
            AppServiceFacade.getQualificationService().create(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.source cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createQualificationWithNullHolder() throws ServiceException {
        Qualification qualification = QualificationGenerator.generateQualification();

        qualification.setHolderNicHandle(null);

        try {
            AppServiceFacade.getQualificationService().create(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.holderNicHandle cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createQualificationWithInvalidHolder() throws ServiceException {
        Qualification qualification = QualificationGenerator.generateQualification();

        String nicHandle = "Raoul";
        qualification.setHolderNicHandle(nicHandle);

        try {
            AppServiceFacade.getQualificationService().create(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.holderNicHandle: '" + nicHandle + "' is not a valid nicHandle.", e.getMessage());
        }
    }

    @Test
    public void createQualificationWithNotExistingHolder() throws ServiceException {
        Qualification qualification = QualificationGenerator.generateQualification();

        String nicHandle = "ABC123-FRNIC";
        qualification.setHolderNicHandle(nicHandle);

        try {
            AppServiceFacade.getQualificationService().create(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.holderNicHandle: nicHandle[" + nicHandle + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void createQualificationWithNullInitiator() throws ServiceException {
        Qualification qualification = QualificationGenerator.generateQualification();

        qualification.setInitiatorEmailAddress(null);

        try {
            AppServiceFacade.getQualificationService().create(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.initiatorEmail cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createQualificationWithInvalidInitiator() throws ServiceException {
        Qualification qualification = QualificationGenerator.generateQualification();
        String mail = "pouetpouet";
        qualification.setInitiatorEmailAddress(new EmailAddress(mail));

        try {
            AppServiceFacade.getQualificationService().create(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (InvalidFormatException e) {
            TestCase.assertEquals("qualification.initiatorEmail: Invalid value " + mail + " for EmailAddress", e.getMessage());
        }
    }

}
