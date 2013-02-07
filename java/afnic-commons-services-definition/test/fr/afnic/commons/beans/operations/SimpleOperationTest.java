package fr.afnic.commons.beans.operations;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.TestOperation;
import fr.afnic.commons.test.generator.UserGenerator;

public class SimpleOperationTest {

    @Before
    public void init() throws ServiceException {
        new MockAppServiceFacade().use();
    }

    @Test
    public void testSucced() throws Exception {
        SimpleOperation operation = new TestOperation(OperationStatus.Succed, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        operation = AppServiceFacade.getOperationService().createAndGet(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        OperationStatus status = operation.execute();
        TestCase.assertEquals("Bad returned status", OperationStatus.Succed, status);
        TestCase.assertEquals("Bad operation status", OperationStatus.Succed, operation.getStatus());
    }

    @Test
    public void testFailedBlocking() throws Exception {
        SimpleOperation operation = new TestOperation(OperationStatus.Failed, true, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        operation = AppServiceFacade.getOperationService().createAndGet(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        OperationStatus status = operation.execute();
        TestCase.assertEquals("Bad returned status", OperationStatus.Failed, status);
        TestCase.assertEquals("Bad operation status", OperationStatus.Failed, operation.getStatus());
    }

    @Test
    public void testFailedNotBlocking() throws Exception {
        SimpleOperation operation = new TestOperation(OperationStatus.Failed, false, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        operation = AppServiceFacade.getOperationService().createAndGet(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        OperationStatus status = operation.execute();
        TestCase.assertEquals("Bad returned status", OperationStatus.Warn, status);
        TestCase.assertEquals("Bad operation status", OperationStatus.Warn, operation.getStatus());
    }

    @Test
    public void testNullBlocking() throws Exception {
        SimpleOperation operation = new TestOperation(null, true, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        operation = AppServiceFacade.getOperationService().createAndGet(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        OperationStatus status = operation.execute();
        TestCase.assertEquals("Bad returned status", OperationStatus.Failed, status);
        TestCase.assertEquals("Bad operation status", OperationStatus.Failed, operation.getStatus());
        TestCase.assertEquals("Bad operation details", TestOperation.class.getSimpleName() + ".executeSimple() returned null", operation.getDetails());
    }

    @Test
    public void testNullNotBlocking() throws Exception {
        SimpleOperation operation = new TestOperation(null, false, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        operation = AppServiceFacade.getOperationService().createAndGet(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        OperationStatus status = operation.execute();
        TestCase.assertEquals("Bad returned status", OperationStatus.Warn, status);
        TestCase.assertEquals("Bad operation status", OperationStatus.Warn, operation.getStatus());
        TestCase.assertEquals("Bad operation details", TestOperation.class.getSimpleName() + ".executeSimple() returned null", operation.getDetails());
    }

    @Test
    public void testCrashBlocking() throws Exception {
        SimpleOperation operation = new CrashingSimpleOperation(true);
        operation = AppServiceFacade.getOperationService().createAndGet(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        OperationStatus status = operation.execute();
        TestCase.assertEquals("Bad returned status", OperationStatus.Failed, status);
        TestCase.assertEquals("Bad operation status", OperationStatus.Failed, operation.getStatus());
        TestCase.assertNotNull("Bad operation details", operation.getDetails());
        TestCase.assertTrue("Bad operation details:" + operation.getDetails(), operation.getDetails().startsWith("java.lang.Exception: crashed"));
    }

    @Test
    public void testCrashNotBlocking() throws Exception {
        SimpleOperation operation = new CrashingSimpleOperation(false);
        operation = AppServiceFacade.getOperationService().createAndGet(operation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        OperationStatus status = operation.execute();
        TestCase.assertEquals("Bad returned status", OperationStatus.Warn, status);
        TestCase.assertEquals("Bad operation status", OperationStatus.Warn, operation.getStatus());
        TestCase.assertNotNull("Bad operation details", operation.getDetails());
        TestCase.assertTrue("Bad operation details:" + operation.getDetails(), operation.getDetails().startsWith("java.lang.Exception: crashed"));
    }

}
