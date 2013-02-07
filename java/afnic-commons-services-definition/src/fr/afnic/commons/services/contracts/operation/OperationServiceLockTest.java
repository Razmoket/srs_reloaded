package fr.afnic.commons.services.contracts.operation;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.OperationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class OperationServiceLockTest {

    @Test
    public void unlockOperationTest() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        AppServiceFacade.getOperationService().unlockOperation(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Operation ope = AppServiceFacade.getOperationService().getOperation(operation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertFalse(ope.isLocked());
    }

    @Test
    public void lockOperationTest() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        AppServiceFacade.getOperationService().lockOperation(operation, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);

        Operation ope = AppServiceFacade.getOperationService().getOperation(operation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertTrue(ope.isLocked());
        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), ope.getLockingUserId());
    }

    @Test
    public void lockUnlockOperationTest() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        AppServiceFacade.getOperationService().lockOperation(operation, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);

        Operation ope = AppServiceFacade.getOperationService().getOperation(operation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertTrue(ope.isLocked());
        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), ope.getLockingUserId());

        AppServiceFacade.getOperationService().unlockOperation(ope, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        ope = AppServiceFacade.getOperationService().getOperation(operation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertFalse(ope.isLocked());
        TestCase.assertNull(ope.getLockingUserId());
    }

    @Test
    public void unlockOperationWithNullOperationId() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        operation.setId(null);
        try {
            AppServiceFacade.getOperationService().unlockOperation(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.id cannot be null.", e.getMessage());
        }
    }

    @Test
    public void unlockOperationWithUnknownOperationId() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        operation.setId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID));
        try {
            AppServiceFacade.getOperationService().unlockOperation(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.id: OperationId[" + OperationGenerator.UNKNOWN_OPERATION_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void lockOperationWithNullOperationId() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        operation.setId(null);
        try {
            AppServiceFacade.getOperationService().lockOperation(operation, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.id cannot be null.", e.getMessage());
        }
    }

    @Test
    public void lockOperationWithUnknownOperationId() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        operation.setId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID));
        try {
            AppServiceFacade.getOperationService().lockOperation(operation, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.id: OperationId[" + OperationGenerator.UNKNOWN_OPERATION_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void lockOperationWithNullUserId() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        try {
            AppServiceFacade.getOperationService().lockOperation(operation, null, TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("lockingUserId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void lockOperationWithUnknownUserId() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        try {
            AppServiceFacade.getOperationService().lockOperation(operation, new UserId(UserGenerator.UNKNOWN_USER_ID), TldServiceFacade.Fr);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("lockingUserId: UserId[" + UserGenerator.UNKNOWN_USER_ID + "] does not exist.", e.getMessage());
        }
    }
}
