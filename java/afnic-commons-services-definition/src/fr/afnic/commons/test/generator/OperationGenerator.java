package fr.afnic.commons.test.generator;

import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.StringUtils;

public final class OperationGenerator {

    public static final int UNKNOWN_OPERATION_ID = 999999999;

    private OperationGenerator() {

    }

    public static Operation createOperation() throws ServiceException {
        Operation createOperationContent = generateSimpleOperationContent();
        return AppServiceFacade.getOperationService().createAndGet(createOperationContent, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    public static CompositeOperation generateCompositeOperationContent() throws ServiceException {
        CompositeOperation operation = new CompositeOperation(OperationConfiguration.create().setType(OperationType.CompositeTest)
                                                                                    .setBlocking(false)
                                                                                    .setCreateUserId(UserGenerator.getRootUserId()), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        operation.setComments("Operation composite");
        return operation;
    }

    public static Operation generateSimpleOperationContent() throws ServiceException {
        TestOperation operation = new TestOperation(OperationStatus.Pending, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        UserId rootUserId = UserGenerator.getRootUserId();

        operation.setCreateUserId(rootUserId);

        operation.setComments(StringUtils.generateWord(500));
        operation.setDetails(StringUtils.generateWord(500));

        return operation;
    }
}
