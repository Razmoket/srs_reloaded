package fr.afnic.commons.services.contracts.operation;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.OperationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class OperationServiceUpdateTest extends AbstractOperationServiceUpdateTest<OperationStatus> {

    @Override
    public OperationStatus[] getStatusList() {
        return OperationStatus.values();
    }

    @Override
    public Operation changeAndTestStatus(Operation createdOperation, OperationStatus status) throws ServiceException {
        createdOperation.setStatus(status);
        AppServiceFacade.getOperationService().updateStatus(createdOperation, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);

        Operation operation = AppServiceFacade.getOperationService().getOperation(createdOperation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(status, operation.getStatus());
        // Deux updates sont effectués: update status + update user
        TestCase.assertEquals(createdOperation.getObjectVersion() + 2, operation.getObjectVersion());

        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), operation.getUpdateUserId());
        return operation;
    }

    @Test
    public void updateOperationComments() throws ServiceException {
        Operation createOperation = OperationGenerator.createOperation();
        String comment = "Nouveau commentaire";
        createOperation.setComments(comment);
        AppServiceFacade.getOperationService().updateComments(createOperation, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);

        Operation operation = AppServiceFacade.getOperationService().getOperation(createOperation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(comment, operation.getComments());
        // Deux updates sont effectués: update comments + update user
        TestCase.assertEquals(createOperation.getObjectVersion() + 2, operation.getObjectVersion());
        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), operation.getUpdateUserId());

    }

    @Test
    public void updateOperationDetails() throws ServiceException {
        Operation createOperation = OperationGenerator.createOperation();
        String detail = "Nouveau detail";
        createOperation.setDetails(detail);
        AppServiceFacade.getOperationService().updateDetails(createOperation, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);

        Operation operation = AppServiceFacade.getOperationService().getOperation(createOperation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(detail, operation.getDetails());
        // Deux updates sont effectués: update details + update user
        TestCase.assertEquals(createOperation.getObjectVersion() + 2, operation.getObjectVersion());
        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), operation.getUpdateUserId());

    }

    @Test
    public void updateOperationUser() throws ServiceException {
        Operation createOperation = OperationGenerator.createOperation();

        AppServiceFacade.getOperationService().updateUser(createOperation, UserGenerator.generateVisitorUser().getId(), TldServiceFacade.Fr);

        Operation operation = AppServiceFacade.getOperationService().getOperation(createOperation.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(UserGenerator.generateVisitorUser().getId(), operation.getUpdateUserId());
        TestCase.assertEquals(UserGenerator.getRootUserId(), operation.getCreateUserId());
        TestCase.assertEquals(createOperation.getObjectVersion() + 1, operation.getObjectVersion());
    }

    @Test
    public void updateOperationWithInvalidUser() throws ServiceException {
        Operation createOperation = OperationGenerator.createOperation();
        int userId = 99999;
        try {
            AppServiceFacade.getOperationService().updateUser(createOperation, new UserId(userId), TldServiceFacade.Fr);
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("updateUserId: UserId[" + userId + "] does not exist.", e.getMessage());
        }
    }

}
