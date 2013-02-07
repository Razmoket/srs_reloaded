package fr.afnic.commons.services.contracts.operation;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.OperationGenerator;
import fr.afnic.commons.test.generator.TestOperation;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.utils.StringUtils;

public class OperationServiceCreateAndGetTest {

    @Test
    public void isExistingOperationIdWithNull() throws ServiceException {
        try {
            AppServiceFacade.getOperationService().isExistingOperationId(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operationId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void isExistingOperationIdWithNonExisting() throws ServiceException {
        TestCase.assertFalse(AppServiceFacade.getOperationService().isExistingOperationId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID), UserGenerator.getRootUserId(), TldServiceFacade.Fr));
    }

    @Test
    public void isExistingOperationIdWithExisting() throws ServiceException {
        Operation createOperation = OperationGenerator.createOperation();
        TestCase.assertTrue(AppServiceFacade.getOperationService().isExistingOperationId(createOperation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr));
    }

    @Test
    public void getNonExistantOperationId() throws ServiceException {
        try {
            AppServiceFacade.getOperationService().getOperation(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (NotFoundException e) {
            TestCase.assertEquals("operationId [" + OperationGenerator.UNKNOWN_OPERATION_ID + "] not found.", e.getMessage());
        }
    }

    @Test
    public void getNullOperationId() throws ServiceException {
        try {
            OperationId id = null;
            AppServiceFacade.getOperationService().getOperation(id, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operationId cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createOperationWithNullUserId() throws ServiceException {
        Operation createOperationContent = OperationGenerator.generateSimpleOperationContent();

        createOperationContent.setCreateUserId(null);

        try {
            AppServiceFacade.getOperationService().create(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.createUserId cannot be null.", e.getMessage());
        }

    }

    @Test
    public void createQualificationWithInvalidUser() throws ServiceException {
        Operation createOperationContent = OperationGenerator.generateSimpleOperationContent();

        createOperationContent.setCreateUserId(new UserId(UserGenerator.UNKNOWN_USER_ID));

        try {
            AppServiceFacade.getOperationService().create(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.createUserId: UserId[" + UserGenerator.UNKNOWN_USER_ID + "] does not exist.", e.getMessage());
        }

    }

    @Test
    public void createOperationTooLongComments() throws ServiceException {
        Operation createOperationContent = OperationGenerator.generateSimpleOperationContent();
        String comments = StringUtils.generateWord(4001);
        createOperationContent.setComments(comments);
        try {
            AppServiceFacade.getOperationService().create(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.comments is too long expected 4000 get 4001.", e.getMessage());
        }
    }

    @Test
    public void createOperationCommentsWithSpecialCharacters() throws ServiceException {
        Operation createOperationContent = OperationGenerator.generateSimpleOperationContent();
        String comments = StringUtils.generateSpecialCharacters();
        createOperationContent.setComments(comments);
        OperationId create = AppServiceFacade.getOperationService().create(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Operation operation = AppServiceFacade.getOperationService().getOperation(create, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        this.checkSimpleOperation(createOperationContent, operation);
    }

    @Test
    public void createOperationTooLongDetails() throws ServiceException {
        Operation createOperationContent = OperationGenerator.generateSimpleOperationContent();
        String comments = StringUtils.generateWord(4001);
        createOperationContent.setDetails(comments);
        try {
            AppServiceFacade.getOperationService().create(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.details is too long expected 4000 get 4001.", e.getMessage());
        }
    }

    @Test
    public void createOperationDetailsWithSpecialCharacters() throws ServiceException {
        Operation createOperationContent = OperationGenerator.generateSimpleOperationContent();
        String comments = StringUtils.generateSpecialCharacters();
        createOperationContent.setDetails(comments);
        OperationId create = AppServiceFacade.getOperationService().create(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Operation operation = AppServiceFacade.getOperationService().getOperation(create, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        this.checkSimpleOperation(createOperationContent, operation);
    }

    @Test
    public void createOperation() throws ServiceException {
        Operation createOperationContent = OperationGenerator.generateSimpleOperationContent();
        OperationId create = AppServiceFacade.getOperationService().create(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Operation operation = AppServiceFacade.getOperationService().getOperation(create, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        this.checkSimpleOperation(createOperationContent, operation);

        TestCase.assertEquals(0, operation.getObjectVersion());
    }

    private void checkSimpleOperation(Operation createdOperation, Operation gettedOperation) {
        OperationTestCase.assertOperation(createdOperation, gettedOperation);

        TestCase.assertFalse(gettedOperation.hasParent());

    }

    @Test
    public void getSubOperationTestWithEmptyResult() throws ServiceException {
        Operation createOperationContent = OperationGenerator.generateSimpleOperationContent();
        OperationId create = AppServiceFacade.getOperationService().create(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        List<Operation> subOperations = AppServiceFacade.getOperationService().getSubOperations(create, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertNotNull(subOperations);
        TestCase.assertTrue(subOperations.isEmpty());
    }

    @Test
    public void getSubOperationTestWithResult() throws ServiceException {
        CompositeOperation compositeOperation = OperationGenerator.generateCompositeOperationContent();
        CompositeOperation createCompositeOperation = AppServiceFacade.getOperationService().createAndGet(compositeOperation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setParentId(createCompositeOperation.getId())
                                                            .setCreateUserId(UserGenerator.getRootUserId());

        OperationId simpleOne = AppServiceFacade.getOperationService().create(new TestOperation(conf, UserGenerator.getRootUserId(), TldServiceFacade.Fr), UserGenerator.getRootUserId(),
                                                                              TldServiceFacade.Fr);
        OperationId simpleTwo = AppServiceFacade.getOperationService().create(new TestOperation(conf, UserGenerator.getRootUserId(), TldServiceFacade.Fr), UserGenerator.getRootUserId(),
                                                                              TldServiceFacade.Fr);

        List<Operation> subOperations = AppServiceFacade.getOperationService().getSubOperations(createCompositeOperation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertNotNull(subOperations);
        TestCase.assertEquals(2, subOperations.size());

        TestCase.assertEquals(simpleOne, subOperations.get(0).getId());
        TestCase.assertEquals(simpleTwo, subOperations.get(1).getId());

    }

    @Test
    public void createCompositeOperation() throws ServiceException {
        CompositeOperation compositeOperation = OperationGenerator.generateCompositeOperationContent();
        CompositeOperation createCompositeOperation = AppServiceFacade.getOperationService().createAndGet(compositeOperation, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setParentId(createCompositeOperation.getId())
                                                            .setCreateUserId(UserGenerator.getRootUserId());

        AppServiceFacade.getOperationService().create(new TestOperation(conf, UserGenerator.getRootUserId(), TldServiceFacade.Fr), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        AppServiceFacade.getOperationService().create(new TestOperation(conf, UserGenerator.getRootUserId(), TldServiceFacade.Fr), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        CompositeOperation resultOperation = AppServiceFacade.getOperationService().getOperation(createCompositeOperation.getId(), CompositeOperation.class, UserGenerator.getRootUserId(),
                                                                                                 TldServiceFacade.Fr);
        TestCase.assertEquals(2, resultOperation.getSubOperations().size());
    }

}
