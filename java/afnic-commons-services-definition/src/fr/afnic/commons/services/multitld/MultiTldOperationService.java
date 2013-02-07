package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.commons.SendEmail;
import fr.afnic.commons.beans.operations.qualification.operation.StatusUpdate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IOperationService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldOperationService implements IOperationService {

    protected MultiTldOperationService() {
        super();
    }

    @Override
    public List<Operation> getSubOperations(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationService().getSubOperations(operationId, userId, tld);
    }

    @Override
    public OperationId create(Operation operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationService().create(operation, userId, tld);
    }

    @Override
    public <OPERATION extends Operation> OPERATION createAndGet(OPERATION operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationService().createAndGet(operation, userId, tld);
    }

    @Override
    public void updateUser(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationService().updateUser(operation, updateUserId, tld);
    }

    @Override
    public void updateStatus(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationService().updateStatus(operation, updateUserId, tld);
    }

    @Override
    public void updateComments(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationService().updateComments(operation, updateUserId, tld);
    }

    @Override
    public void updateDetails(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationService().updateDetails(operation, updateUserId, tld);
    }

    @Override
    public void lockOperation(Operation operation, UserId lockingUserId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationService().lockOperation(operation, lockingUserId, tld);
    }

    @Override
    public void unlockOperation(Operation operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationService().unlockOperation(operation, userId, tld);
    }

    @Override
    public Operation getOperation(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationService().getOperation(operationId, userId, tld);
    }

    @Override
    public <OPERATION extends Operation> OPERATION getOperation(OperationId operationId, Class<OPERATION> operationClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationService().getOperation(operationId, operationClass, userId, tld);
    }

    @Override
    public void attach(Operation operation, UserId updateUserId, Document document, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationService().attach(operation, updateUserId, document, tld);
    }

    @Override
    public boolean isExistingOperationId(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationService().isExistingOperationId(operationId, userId, tld);
    }

    @Override
    public List<Document> getDocuments(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationService().getDocuments(operationId, userId, tld);
    }

    @Override
    public <STATUS extends Enum<?>> void populateStatusUpdate(StatusUpdate<STATUS> update, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationService().populateStatusUpdate(update, userId, tld);
    }

    @Override
    public void populateSendEmail(SendEmail sendEmail, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getOperationService().populateSendEmail(sendEmail, userId, tld);
    }

    @Override
    public OperationId getOperation(String documentHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getOperationService().getOperation(documentHandle, userId, tld);
    }
}
