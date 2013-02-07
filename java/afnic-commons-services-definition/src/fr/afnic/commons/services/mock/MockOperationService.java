package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.google.common.base.Objects;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.SimpleOperation;
import fr.afnic.commons.beans.operations.commons.SendEmail;
import fr.afnic.commons.beans.operations.qualification.operation.StatusUpdate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.constraints.StringConstraint;
import fr.afnic.commons.services.IOperationService;
import fr.afnic.commons.services.contracts.operation.OperationServiceParametersMethodChecker;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class MockOperationService implements IOperationService {

    private static int nextOperationId = 1;

    private final HashMap<OperationId, Operation> operationMap = new HashMap<OperationId, Operation>();
    private final HashMap<OperationId, ArrayList<Document>> documentMap = new HashMap<OperationId, ArrayList<Document>>();

    private final OperationServiceParametersMethodChecker methodChecker = new OperationServiceParametersMethodChecker();

    private static OperationId getNextOperationId() {
        return new OperationId(nextOperationId++);
    }

    public Operation getStoredOperation(OperationId id) {
        return this.operationMap.get(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <OPERATION extends Operation> OPERATION createAndGet(OPERATION operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        return (OPERATION) this.getOperation(this.create(operation, userId, tld), userId, tld);
    }

    @Override
    public OperationId create(Operation operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getCreateUserId(), "operation.createUserId", userId, tld);
        Preconditions.checkNotLongerThan(operation.getComments(), StringConstraint.MAX_LENGTH, "operation.comments");
        Preconditions.checkNotLongerThan(operation.getDetails(), StringConstraint.MAX_LENGTH, "operation.details");
        Preconditions.checkNotNull(operation.getType(), "operation.type");

        OperationId nextOperationId = getNextOperationId();

        Operation savedOperation = operation.copy();

        savedOperation.setId(nextOperationId);
        savedOperation.setCreateDate(new Date());
        savedOperation.setUpdateDate(savedOperation.getCreateDate());
        savedOperation.setUpdateUserId(savedOperation.getCreateUserId());
        this.operationMap.put(nextOperationId, savedOperation);

        return nextOperationId;
    }

    @Override
    public Operation getOperation(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operationId, "operationId");
        Operation operation = this.operationMap.get(operationId);

        if (operation == null) {
            throw new NotFoundException("operationId [" + operationId.getIntValue() + "] not found.");
        } else if (operation instanceof SimpleOperation) {
            //((SimpleOperation) operation).setDocuments(this.getDocumentList(operationId));
        }
        return operation.copy();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <OPERATION extends Operation> OPERATION getOperation(OperationId operationId, Class<OPERATION> operationClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        Operation operation = this.getOperation(operationId, userId, tld);
        if (!operationClass.equals(operation.getClass())) {
            throw new ServiceException("Operation " + operationId + " is " + operation.getClass().getSimpleName() + "[expected: " + operationClass.getSimpleName() + "]");
        } else {
            return (OPERATION) operation;
        }
    }

    @Override
    public List<Operation> getSubOperations(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operationId, "operationId");

        List<Operation> subOperations = new ArrayList<Operation>();
        for (Operation operation : this.operationMap.values()) {
            if (Objects.equal(operation.getParentId(), operationId)) {
                subOperations.add(operation);
            }
        }
        return subOperations;
    }

    private List<Document> getDocumentList(OperationId operationId) {
        if (!this.documentMap.containsKey(operationId)) {
            this.documentMap.put(operationId, new ArrayList<Document>());
        }
        return this.documentMap.get(operationId);
    }

    @Override
    public void attach(Operation operation, UserId updateUserId, Document document, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(document, "document");
        Preconditions.checkNotNull(document.getHandle(), "document.handle");

        this.getDocumentList(operation.getId()).add(document);
        document.sort();

    }

    @Override
    public void updateUser(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);

        Operation savedOperation = this.operationMap.get(operation.getId());
        savedOperation.setObjectVersion(savedOperation.getObjectVersion() + 1);
        savedOperation.setUpdateUserId(updateUserId);
    }

    @Override
    public void updateStatus(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);

        Operation savedOperation = this.operationMap.get(operation.getId());
        savedOperation.setStatus(operation.getStatus());
        savedOperation.setObjectVersion(savedOperation.getObjectVersion() + 1);

        this.updateUser(savedOperation, updateUserId, tld);

    }

    @Override
    public void updateComments(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);

        Operation savedOperation = this.operationMap.get(operation.getId());
        savedOperation.setComments(operation.getComments());
        savedOperation.setObjectVersion(savedOperation.getObjectVersion() + 1);

        this.updateUser(savedOperation, updateUserId, tld);

    }

    @Override
    public void updateDetails(Operation operation, UserId updateUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(updateUserId, "updateUserId", updateUserId, tld);
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", updateUserId, tld);

        Operation savedOperation = this.operationMap.get(operation.getId());
        savedOperation.setDetails(operation.getDetails());
        savedOperation.setObjectVersion(savedOperation.getObjectVersion() + 1);

        this.updateUser(savedOperation, updateUserId, tld);

    }

    @Override
    public void lockOperation(Operation operation, UserId lockingUserId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkIsExistingIdentifier(lockingUserId, "lockingUserId", lockingUserId, tld);
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", lockingUserId, tld);

        Operation savedOperation = this.operationMap.get(operation.getId());
        savedOperation.setLockingDate(new Date());
        savedOperation.setLockingUserId(lockingUserId);
        savedOperation.setObjectVersion(savedOperation.getObjectVersion() + 1);

    }

    @Override
    public void unlockOperation(Operation operation, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operation, "operation");
        Preconditions.checkIsExistingIdentifier(operation.getId(), "operation.id", userId, tld);

        Operation savedOperation = this.operationMap.get(operation.getId());
        savedOperation.setLockingDate(null);
        savedOperation.setLockingUserId(null);

    }

    @Override
    public boolean isExistingOperationId(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(operationId, "operationId");
        return this.operationMap.containsKey(operationId);
    }

    @Override
    public List<Document> getDocuments(OperationId operationId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return Collections.unmodifiableList(this.getDocumentList(operationId));
    }

    @Override
    public <STATUS extends Enum<?>> void populateStatusUpdate(StatusUpdate<STATUS> update, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void populateSendEmail(SendEmail sendEmail, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public OperationId getOperation(String documentHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }
}
