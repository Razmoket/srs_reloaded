package fr.afnic.commons.services.contracts.qualification;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.CorporateEntityWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.qualification.EligibilityStatus;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationSnapshot;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.QualificationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;

public class QualificationServiceSnapshotTest {

    @Test
    public void createSnapshotWithNullQualification() throws ServiceException, GeneratorException {
        try {
            AppServiceFacade.getQualificationService().createSnapshot(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createSnapshotWithQualificationWithNullId() throws ServiceException, GeneratorException {
        Qualification qualif = QualificationGenerator.createQualification();
        qualif.setId(null);

        try {
            AppServiceFacade.getQualificationService().createSnapshot(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.id cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createAndGetSnapshotWithDefaultValues() throws ServiceException {

        Qualification qualification = QualificationGenerator.generateQualification();

        qualification.setEligibilityStatus(null);
        qualification.setReachStatus(null);

        qualification = AppServiceFacade.getQualificationService().createAndGet(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        int snapId = AppServiceFacade.getQualificationService().createSnapshot(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        QualificationSnapshot qualificationSnapshot = AppServiceFacade.getQualificationService().getQualificationSnapshot(snapId, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertNotNull(qualificationSnapshot);
        TestCase.assertEquals(snapId, qualificationSnapshot.get("ID_QUALIFICATION_SNAPSHOT"));

        TestCase.assertEquals(EligibilityStatus.NotIdentified, qualificationSnapshot.get("ID_ELIGIBILITY_STATUS"));
        TestCase.assertEquals(ReachStatus.NotIdentified, qualificationSnapshot.get("ID_REACH_STATUS"));

    }

    @Test
    public void createAndGetSnapshotWithReachStatus() throws ServiceException {
        for (ReachStatus reachStatus : ReachStatus.values()) {
            this.assertSnapshotStatus(reachStatus, EligibilityStatus.Active, PortfolioStatus.Active, TopLevelOperationStatus.Pending);
        }
    }

    @Test
    public void createAndGetSnapshotWithEligibilityStatus() throws ServiceException {

        for (PortfolioStatus portfolioStatus : PortfolioStatus.values()) {
            this.assertSnapshotStatus(ReachStatus.Email, EligibilityStatus.Active, portfolioStatus, TopLevelOperationStatus.Pending);
        }
    }

    @Test
    public void createAndGetSnapshotWithTopLevelOperationStatus() throws ServiceException {
        for (TopLevelOperationStatus topLevelOperationStatus : TopLevelOperationStatus.values()) {
            this.assertSnapshotStatus(ReachStatus.Email, EligibilityStatus.Active, PortfolioStatus.Active, topLevelOperationStatus);
        }
    }

    @Test
    public void createAndGetSnapshotWithPortfolioStatus() throws ServiceException {
        for (EligibilityStatus eligStatus : EligibilityStatus.values()) {
            this.assertSnapshotStatus(ReachStatus.Email, eligStatus, PortfolioStatus.Active, TopLevelOperationStatus.Pending);
        }
    }

    @Test
    public void createAndGetSnapshotWithPP() throws ServiceException, GeneratorException {
        String handle = ContactGenerator.createCorporateEntityContact().getHandle();
        Qualification qualif = QualificationGenerator.generateQualification(handle);

        this.createAndAssertSnapshot(qualif);
    }

    @Test
    public void createAndGetSnapshotWithPM() throws ServiceException, GeneratorException {
        String handle = ContactGenerator.createCorporateEntityContact().getHandle();
        Qualification qualif = QualificationGenerator.generateQualification(handle);

        this.createAndAssertSnapshot(qualif);
    }

    private void assertSnapshotStatus(ReachStatus reachStatus, EligibilityStatus eligStatus, PortfolioStatus portfolioStatus, TopLevelOperationStatus topLevelOperationStatus) throws ServiceException {

        Qualification qualif = QualificationGenerator.createQualification();

        qualif.setReachStatus(reachStatus);
        AppServiceFacade.getQualificationService().updateReachStatus(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        qualif.setEligibilityStatus(eligStatus);
        AppServiceFacade.getQualificationService().updateEligibilityStatus(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        qualif.setPortfolioStatus(portfolioStatus);
        AppServiceFacade.getQualificationService().updatePortfolioStatus(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        qualif.setTopLevelStatus(topLevelOperationStatus);
        AppServiceFacade.getQualificationService().updateTopLevelStatus(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        this.createAndAssertSnapshot(qualif);
    }

    private void createAndAssertSnapshot(Qualification qualif) throws ServiceException {
        OperationId qualifId = AppServiceFacade.getQualificationService().create(qualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Qualification gettedQualif = AppServiceFacade.getQualificationService().getQualification(qualifId, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        int snapId = AppServiceFacade.getQualificationService().createSnapshot(gettedQualif, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        QualificationSnapshot qualificationSnapshot = AppServiceFacade.getQualificationService().getQualificationSnapshot(snapId, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertNotNull(qualificationSnapshot);
        TestCase.assertEquals(snapId, qualificationSnapshot.get("ID_QUALIFICATION_SNAPSHOT"));
        this.assertSnapshot(gettedQualif, qualificationSnapshot);

    }

    private void assertSnapshot(Qualification expected, QualificationSnapshot actual) throws ServiceException {
        TestCase.assertEquals(expected.getEligibilityStatus(), actual.get("ID_ELIGIBILITY_STATUS"));
        TestCase.assertEquals(expected.getReachStatus(), actual.get("ID_REACH_STATUS"));
        TestCase.assertEquals(expected.getPortfolioStatus(), actual.get("ID_PORTFOLIO_STATUS"));
        TestCase.assertEquals(expected.getTopLevelStatus(), actual.get("ID_QUALIFICATION_STATUS"));

        WhoisContact holder = expected.getHolder();
        TestCase.assertNotNull(holder);
        if (holder instanceof CorporateEntityWhoisContact) {
            CorporateEntityWhoisContact corp = (CorporateEntityWhoisContact) holder;
            CorporateEntity legalStructure = corp.getLegalStructure();
            TestCase.assertEquals(legalStructure.getOrganizationTypeAsString(), actual.get("ORG_TYPE"));
            TestCase.assertEquals(legalStructure.getSirenAsString(), actual.get("SIREN"));
            TestCase.assertEquals(legalStructure.getSiretAsString(), actual.get("SIRET"));
            TestCase.assertEquals(legalStructure.getTradeMarkAsString(), actual.get("TRADEMARK"));
            TestCase.assertEquals(legalStructure.getWaldecAsString(), actual.get("WALDEC"));
            //TestCase.assertEquals(expected.getEligibilityStatus(), actual.get("DUNS"));

        } else {
            TestCase.assertNull(actual.get("ORG_TYPE"));
            TestCase.assertNull(actual.get("SIREN"));
            TestCase.assertNull(actual.get("SIRET"));
            TestCase.assertNull(actual.get("TRADEMARK"));
            TestCase.assertNull(actual.get("WALDEC"));

        }
        TestCase.assertEquals(holder.getName(), actual.get("NAME"));
    }

}
