package fr.afnic.commons.services.contracts.qualification;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.qualification.EligibilityStatus;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.OperationGenerator;
import fr.afnic.commons.test.generator.QualificationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class QualificationServiceUpdateTest {

    @Test
    public void updateTopLevelStatus() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();
        for (TopLevelOperationStatus status : TopLevelOperationStatus.values()) {
            qualification = this.changeAndTestTopLevelStatus(qualification, status);
        }
    }

    @Test
    public void setIncoherent() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();

        TestCase.assertFalse(qualification.isIncoherent());

        AppServiceFacade.getQualificationService().setIncoherent(qualification, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);
        Qualification qualif = AppServiceFacade.getQualificationService().getQualification(qualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertEquals(qualification.getObjectVersion() + 1, qualif.getObjectVersion());
        TestCase.assertTrue(qualif.isIncoherent());
        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), qualif.getUpdateUserId());

    }

    @Test
    public void setIncoherentWithInvalidUserId() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();
        TestCase.assertFalse(qualification.isIncoherent());
        try {
            AppServiceFacade.getQualificationService().setIncoherent(qualification, new UserId(UserGenerator.UNKNOWN_USER_ID), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("updateUserId: UserId[" + UserGenerator.UNKNOWN_USER_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void setIncoherentWithUnknownOperationId() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();
        TestCase.assertFalse(qualification.isIncoherent());
        qualification.setId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID));
        try {
            AppServiceFacade.getQualificationService().setIncoherent(qualification, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId: OperationId[" + OperationGenerator.UNKNOWN_OPERATION_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void setIncoherentWithNullOperationId() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();
        TestCase.assertFalse(qualification.isIncoherent());
        qualification.setId(null);
        try {
            AppServiceFacade.getQualificationService().setIncoherent(qualification, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void updateEligibilityStatusWithUnknownOperationId() throws ServiceException {
        Qualification createOperation = QualificationGenerator.createQualification();
        createOperation.setId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID));
        try {
            this.changeAndTestEligibilityStatus(createOperation, EligibilityStatus.Active);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId: OperationId[" + OperationGenerator.UNKNOWN_OPERATION_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void updateEligibilityStatusWithNullOperationId() throws ServiceException {
        Qualification createQualification = QualificationGenerator.createQualification();
        createQualification.setId(null);
        try {
            this.changeAndTestEligibilityStatus(createQualification, EligibilityStatus.NoMatch);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void updatePortfolioStatusWithUnknownOperationId() throws ServiceException {
        Qualification createOperation = QualificationGenerator.createQualification();
        createOperation.setId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID));
        try {
            this.changeAndTestPortfolioStatus(createOperation, PortfolioStatus.Active);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId: OperationId[" + OperationGenerator.UNKNOWN_OPERATION_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void updatePortfolioStatusWithNullOperationId() throws ServiceException {
        Qualification createQualification = QualificationGenerator.createQualification();
        createQualification.setId(null);
        try {
            this.changeAndTestPortfolioStatus(createQualification, PortfolioStatus.PendingBlock);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void updateReachStatusWithUnknownOperationId() throws ServiceException {
        Qualification createOperation = QualificationGenerator.createQualification();
        createOperation.setId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID));
        try {
            this.changeAndTestReachStatus(createOperation, ReachStatus.Email);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId: OperationId[" + OperationGenerator.UNKNOWN_OPERATION_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void updateReachStatusWithNullOperationId() throws ServiceException {
        Qualification createQualification = QualificationGenerator.createQualification();
        createQualification.setId(null);
        try {
            this.changeAndTestReachStatus(createQualification, ReachStatus.PendingEmail);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void updateTopLevelStatusWithUnknownOperationId() throws ServiceException {
        Qualification createOperation = QualificationGenerator.createQualification();
        createOperation.setId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID));
        try {
            this.changeAndTestTopLevelStatus(createOperation, TopLevelOperationStatus.Finished);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId: OperationId[" + OperationGenerator.UNKNOWN_OPERATION_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void updateTopLevelStatusWithNullOperationId() throws ServiceException {
        Qualification createQualification = QualificationGenerator.createQualification();
        createQualification.setId(null);
        try {
            this.changeAndTestTopLevelStatus(createQualification, TopLevelOperationStatus.Running);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("qualification.operationId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void updateEligibilityStatus() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();
        for (EligibilityStatus status : EligibilityStatus.values()) {
            qualification = this.changeAndTestEligibilityStatus(qualification, status);
        }
    }

    @Test
    public void updatePortfolioStatus() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();
        for (PortfolioStatus status : PortfolioStatus.values()) {
            qualification = this.changeAndTestPortfolioStatus(qualification, status);
        }
    }

    @Test
    public void updateReachStatus() throws ServiceException {
        Qualification qualification = QualificationGenerator.createQualification();
        for (ReachStatus status : ReachStatus.values()) {
            qualification = this.changeAndTestReachStatus(qualification, status);
        }
    }

    private Qualification changeAndTestTopLevelStatus(Qualification createdQualification, TopLevelOperationStatus status) throws ServiceException {
        createdQualification.setTopLevelStatus(status);
        AppServiceFacade.getQualificationService().updateTopLevelStatus(createdQualification, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);
        Qualification operation = AppServiceFacade.getQualificationService().getQualification(createdQualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(status, operation.getTopLevelStatus());
        TestCase.assertEquals(createdQualification.getObjectVersion() + 1, operation.getObjectVersion());
        return operation;
    }

    private Qualification changeAndTestEligibilityStatus(Qualification createdQualification, EligibilityStatus status) throws ServiceException {
        createdQualification.setEligibilityStatus(status);
        AppServiceFacade.getQualificationService().updateEligibilityStatus(createdQualification, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);
        Qualification operation = AppServiceFacade.getQualificationService().getQualification(createdQualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(status, operation.getEligibilityStatus());
        TestCase.assertEquals(createdQualification.getObjectVersion() + 1, operation.getObjectVersion());
        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), operation.getUpdateUserId());
        return operation;
    }

    private Qualification changeAndTestReachStatus(Qualification createdQualification, ReachStatus status) throws ServiceException {
        createdQualification.setReachStatus(status);
        AppServiceFacade.getQualificationService().updateReachStatus(createdQualification, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);
        Qualification operation = AppServiceFacade.getQualificationService().getQualification(createdQualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(status, operation.getReachStatus());
        TestCase.assertEquals(createdQualification.getObjectVersion() + 1, operation.getObjectVersion());
        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), operation.getUpdateUserId());
        return operation;
    }

    private Qualification changeAndTestPortfolioStatus(Qualification createdQualification, PortfolioStatus status) throws ServiceException {
        createdQualification.setPortfolioStatus(status);
        AppServiceFacade.getQualificationService().updatePortfolioStatus(createdQualification, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);
        Qualification operation = AppServiceFacade.getQualificationService().getQualification(createdQualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(status, operation.getPortfolioStatus());
        TestCase.assertEquals(createdQualification.getObjectVersion() + 1, operation.getObjectVersion());
        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), operation.getUpdateUserId());
        return operation;
    }

}
