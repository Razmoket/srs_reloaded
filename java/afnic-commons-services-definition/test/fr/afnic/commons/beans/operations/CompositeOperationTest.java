package fr.afnic.commons.beans.operations;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.TestOperation;
import fr.afnic.commons.test.generator.UserGenerator;

public class CompositeOperationTest {

    private final TestOperation SUCCED = new TestOperation(OperationStatus.Succed, new UserId(22), TldServiceFacade.Fr);
    private final TestOperation PENDING = new TestOperation(OperationStatus.Pending, new UserId(22), TldServiceFacade.Fr);
    private final TestOperation CHECKED = new TestOperation(OperationStatus.Checked, new UserId(22), TldServiceFacade.Fr);
    private final TestOperation FAILED = new TestOperation(OperationStatus.Failed, true, new UserId(22), TldServiceFacade.Fr);
    private final TestOperation WARN = new TestOperation(OperationStatus.Warn, false, new UserId(22), TldServiceFacade.Fr);

    @BeforeClass
    public static void init() throws ServiceException {
        new MockAppServiceFacade().use();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOperationWithNull() throws Exception {
        CompositeOperation operation = this.createCompositeOperation(false);
        Operation ope = null;
        operation.addSubOperation(ope);
    }

    @Test
    public void testExecute() throws Exception {

        this.assertExecuteWithOperations(true, OperationStatus.Succed, this.SUCCED);
        this.assertExecuteWithOperations(true, OperationStatus.Succed, this.SUCCED, this.SUCCED, this.SUCCED);

        this.assertExecuteWithOperations(true, OperationStatus.Checked, this.SUCCED, this.CHECKED, this.CHECKED, this.SUCCED);

        this.assertExecuteWithOperations(true, OperationStatus.Warn, this.SUCCED, this.WARN, this.CHECKED, this.SUCCED);

        this.assertExecuteWithOperations(true, OperationStatus.Pending, this.PENDING);
        this.assertExecuteWithOperations(true, OperationStatus.Pending, this.PENDING, this.PENDING, this.PENDING);
        this.assertExecuteWithOperations(true, OperationStatus.Pending, this.SUCCED, this.WARN, this.PENDING);

        this.assertExecuteWithOperations(true, OperationStatus.Failed, this.SUCCED, this.FAILED, this.PENDING);
        this.assertExecuteWithOperations(true, OperationStatus.Failed, this.SUCCED, this.WARN, this.FAILED, this.PENDING);
        this.assertExecuteWithOperations(true, OperationStatus.Failed, this.SUCCED, this.WARN, this.FAILED, this.PENDING);

        this.assertExecuteWithOperations(false, OperationStatus.Warn, this.SUCCED, this.FAILED, this.PENDING);
    }

    @Test
    public void testExecuteWithUnregistredOperation() throws ServiceException {
        try {
            Operation o = new TestOperation(OperationStatus.Succed, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            o.execute();

            TestCase.fail("Should throw an Exception");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("Bad exception message", "Cannot execute unregistrered operation TestOperation[id is null]", e.getMessage());
        }
    }

    @Test
    public void testExecuteWithNotExistingUser() throws ServiceException {
        try {
            Operation o = new TestOperation(OperationStatus.Succed, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            o.setCreateUserId(UserGenerator.getRootUserId());
            o = AppServiceFacade.getOperationService().createAndGet(o, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            o.execute();

            TestCase.fail("Should throw an Exception");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("user with id UserId: value[123456789] does not exist", e.getMessage());
        }
    }

    @Test
    public void testWithTwoLevel() throws Exception {

        this.assertTwoLevel(OperationStatus.Succed);
        this.assertTwoLevel(OperationStatus.Failed);

    }

    private void assertTwoLevel(OperationStatus status) throws Exception {
        CompositeOperation toplevel = this.createCompositeOperation(true);
        toplevel.setCreateUserId(UserGenerator.getRootUserId());
        toplevel = AppServiceFacade.getOperationService().createAndGet(toplevel, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        CompositeOperation composite = this.createCompositeOperation(true);
        composite.setCreateUserId(UserGenerator.getRootUserId());
        composite.setParentId(toplevel.getId());
        composite = AppServiceFacade.getOperationService().createAndGet(composite, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestOperation simple = new TestOperation(status, true, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        simple.setCreateUserId(UserGenerator.getRootUserId());
        simple.setParentId(composite.getId());
        simple = AppServiceFacade.getOperationService().createAndGet(simple, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        OperationStatus resultStatus = toplevel.execute();
        TestCase.assertEquals(status, resultStatus);
    }

    public void assertExecuteWithOperations(boolean isBlocking, OperationStatus expectedStatus, Operation... operations) throws Exception {

        CompositeOperation operation = this.createCompositeOperation(isBlocking);
        operation.setCreateUserId(UserGenerator.getRootUserId());
        operation = AppServiceFacade.getOperationService().createAndGet(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        for (Operation subOperation : operations) {
            subOperation.setCreateUserId(UserGenerator.getRootUserId());
            subOperation.setParentId(operation.getId());
            AppServiceFacade.getOperationService().create(subOperation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        }

        operation = AppServiceFacade.getOperationService().getOperation(operation.getId(), CompositeOperation.class, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        OperationStatus status = operation.execute();
        TestCase.assertEquals("Bad returned status", expectedStatus, status);
        TestCase.assertEquals("Bad operation status", expectedStatus, operation.getStatus());
    }

    public CompositeOperation createCompositeOperation(boolean isBlocking) throws Exception {
        return new CompositeOperation(new OperationConfiguration().setType(OperationType.CompositeTest)
                                                                  .setBlocking(isBlocking)
                                                                  .setCreateUserId(UserGenerator.getRootUserId()), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

    }
}
