package fr.afnic.commons.services.contracts.whoiscontact;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.operations.qualification.EligibilityStatus;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationItemStatus;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationSnapshot;
import fr.afnic.commons.beans.operations.qualification.PublicReachMedia;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.QualificationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class WhoisContactServiceGetPublicSnapshotTest {

    @Test
    public void getPublicQualificationSnapshotWithUnknowId() throws ServiceException {
        String unknonwId = "ABC123";
        try {
            AppServiceFacade.getWhoisContactService().getPublicQualificationSnapshot(unknonwId, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

            TestCase.fail("No exception thrown");
        } catch (NotFoundException e) {
            TestCase.assertEquals("No PublicQualificationSnapshot with id " + unknonwId, e.getMessage());
        }
    }

    @Test
    public void getPublicQualificationSnapshotWithReachMediaEmail() throws ServiceException {

        Qualification qualif = QualificationGenerator.createQualification();
        qualif = this.updateTopLevelStatus(qualif, TopLevelOperationStatus.Finished);
        qualif = this.updateReachStatus(qualif, ReachStatus.Email);

        int snapId = AppServiceFacade.getQualificationService().createSnapshot(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        // "object:database_access_error"

        PublicQualificationSnapshot publicSnap = AppServiceFacade.getWhoisContactService().getPublicQualificationSnapshot(this.getPublicId(snapId), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(publicSnap);
        TestCase.assertNotNull(publicSnap.getContact());

        TestCase.assertEquals(PublicReachMedia.EmailAddress, publicSnap.getContact().getReachMedia());

    }

    private String getPublicId(int snapId) {
        PublicQualificationSnapshot publicQualificationSnapshot = new PublicQualificationSnapshot();
        publicQualificationSnapshot.setId(snapId);
        return publicQualificationSnapshot.getPublicId();
    }

    @Test
    public void getPublicQualificationSnapshotWithReachMediaPhone() throws ServiceException {

        Qualification qualif = QualificationGenerator.createQualification();
        qualif = this.updateTopLevelStatus(qualif, TopLevelOperationStatus.Finished);
        qualif = this.updateReachStatus(qualif, ReachStatus.Phone);

        int snapId = AppServiceFacade.getQualificationService().createSnapshot(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        PublicQualificationSnapshot publicSnap = AppServiceFacade.getWhoisContactService().getPublicQualificationSnapshot(this.getPublicId(snapId), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(publicSnap);
        TestCase.assertNotNull(publicSnap.getContact());

        TestCase.assertEquals(PublicReachMedia.Phone, publicSnap.getContact().getReachMedia());
    }

    @Test
    public void getPublicQualificationSnapshotWithReachStatus() throws ServiceException {
        this.assertReachStatus(PublicQualificationItemStatus.NotIdentified, ReachStatus.NotIdentified, TopLevelOperationStatus.Finished);
        this.assertReachStatus(PublicQualificationItemStatus.Pending, ReachStatus.PendingEmail, TopLevelOperationStatus.Finished);
        this.assertReachStatus(PublicQualificationItemStatus.Ko, ReachStatus.NotReachable, TopLevelOperationStatus.Finished);
        this.assertReachStatus(PublicQualificationItemStatus.Ok, ReachStatus.Email, TopLevelOperationStatus.Finished);
        this.assertReachStatus(PublicQualificationItemStatus.Ok, ReachStatus.Phone, TopLevelOperationStatus.Finished);

    }

    private void assertReachStatus(PublicQualificationItemStatus expected, ReachStatus actual, TopLevelOperationStatus topLevelOperationStatus) throws ServiceException {
        Qualification qualif = QualificationGenerator.createQualification();
        qualif = this.updateTopLevelStatus(qualif, topLevelOperationStatus);
        qualif = this.updateReachStatus(qualif, actual);

        int snapId = AppServiceFacade.getQualificationService().createSnapshot(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        PublicQualificationSnapshot publicSnap = AppServiceFacade.getWhoisContactService().getPublicQualificationSnapshot(this.getPublicId(snapId), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(publicSnap);
        TestCase.assertNotNull(publicSnap.getContact());
        TestCase.assertEquals(expected, publicSnap.getContact().getReachStatus());
    }

    @Test
    public void getPublicQualificationSnapshotWithEligibilityStatus() throws ServiceException {

        this.assertEligStatus(PublicQualificationItemStatus.Pending, EligibilityStatus.Active, TopLevelOperationStatus.Running);
        this.assertEligStatus(PublicQualificationItemStatus.Pending, EligibilityStatus.Inactive, TopLevelOperationStatus.Running);
        this.assertEligStatus(PublicQualificationItemStatus.Pending, EligibilityStatus.NotIdentified, TopLevelOperationStatus.Running);

        this.assertEligStatus(PublicQualificationItemStatus.Ok, EligibilityStatus.Active, TopLevelOperationStatus.Finished);
        this.assertEligStatus(PublicQualificationItemStatus.Ok, EligibilityStatus.Inactive, TopLevelOperationStatus.Finished);
        this.assertEligStatus(PublicQualificationItemStatus.Ko, EligibilityStatus.NoMatch, TopLevelOperationStatus.Finished);

        this.assertEligStatus(null, EligibilityStatus.NotIdentified, TopLevelOperationStatus.Finished);
    }

    private void assertEligStatus(PublicQualificationItemStatus expected, EligibilityStatus actual, TopLevelOperationStatus topLevelOperationStatus) throws ServiceException {

        Qualification qualif = QualificationGenerator.createQualification();
        qualif = this.updateTopLevelStatus(qualif, topLevelOperationStatus);
        qualif = this.updateEligibilityStatus(qualif, actual);

        int snapId = AppServiceFacade.getQualificationService().createSnapshot(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        PublicQualificationSnapshot publicSnap = AppServiceFacade.getWhoisContactService().getPublicQualificationSnapshot(this.getPublicId(snapId), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull(publicSnap);
        TestCase.assertNotNull(publicSnap.getContact());

        TestCase.assertEquals(expected, publicSnap.getContact().getEligibilityStatus());
    }

    private Qualification updateTopLevelStatus(Qualification qualification, TopLevelOperationStatus newStatus) throws ServiceException {
        qualification.setTopLevelStatus(newStatus);
        AppServiceFacade.getQualificationService().updateTopLevelStatus(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        return AppServiceFacade.getQualificationService().getQualification(qualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    private Qualification updateReachStatus(Qualification qualification, ReachStatus newStatus) throws ServiceException {
        qualification.setReachStatus(newStatus);
        AppServiceFacade.getQualificationService().updateReachStatus(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        return AppServiceFacade.getQualificationService().getQualification(qualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    private Qualification updateEligibilityStatus(Qualification qualification, EligibilityStatus newStatus) throws ServiceException {
        qualification.setEligibilityStatus(newStatus);
        AppServiceFacade.getQualificationService().updateEligibilityStatus(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        return AppServiceFacade.getQualificationService().getQualification(qualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }
}
