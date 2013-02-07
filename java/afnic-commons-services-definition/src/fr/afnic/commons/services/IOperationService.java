package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.commons.SendEmail;
import fr.afnic.commons.beans.operations.qualification.operation.StatusUpdate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Service concernant les opérations des requetes
 * 
 * @author ginguene
 *
 */
public interface IOperationService {

    public List<Operation> getSubOperations(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public OperationId create(Operation operation, UserId userId, TldServiceFacade tld) throws ServiceException;

    public <OPERATION extends Operation> OPERATION createAndGet(OPERATION operation, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateUser(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException;

    public void updateStatus(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException;

    public void updateComments(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException;

    public void updateDetails(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException;

    public void lockOperation(Operation operation, UserId lockingUserId, TldServiceFacade tld) throws ServiceException;

    public void unlockOperation(Operation operation, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Operation getOperation(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public <OPERATION extends Operation> OPERATION getOperation(OperationId operationId, Class<OPERATION> operationClass, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void attach(Operation operation, UserId updateUserId, Document document, TldServiceFacade tld) throws ServiceException;

    public boolean isExistingOperationId(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Document> getDocuments(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public <STATUS extends Enum<?>> void populateStatusUpdate(StatusUpdate<STATUS> update, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void populateSendEmail(SendEmail sendEmail, UserId userId, TldServiceFacade tld) throws ServiceException;

    public OperationId getOperation(String documentHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

}
