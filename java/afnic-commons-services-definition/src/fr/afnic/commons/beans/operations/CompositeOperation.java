package fr.afnic.commons.beans.operations;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class CompositeOperation extends Operation {

    private final CompositeOperationComputer statusComputer = new CompositeOperationComputer();

    private final List<Operation> operations = new ArrayList<Operation>();

    public CompositeOperation(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        this.setType(OperationType.CompositeTest);
    }

    public CompositeOperation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf, userId, tld);
        this.setStatus(OperationStatus.Pending);
    }

    @Override
    protected final OperationStatus executeImpl() throws ServiceException {
        if (this.getId() == null) {
            throw new IllegalArgumentException("Cannot execute unregistrered operation " + this.getClass().getSimpleName() + " [id is null] ");
        }

        for (Operation currentOperation : this.getSubOperations()) {
            currentOperation.execute();
            if (this.cannotContinueNextOperation(currentOperation)) {
                break;
            }
        }
        return this.computeAndUpdateStatus();
    }

    @Override
    protected final OperationStatus redoExecuteImpl() throws ServiceException {
        if (this.getId() == null) {
            throw new IllegalArgumentException("Cannot execute unregistrered operation " + this.getClass().getSimpleName() + " [id is null] ");
        }

        for (Operation currentOperation : this.getSubOperations()) {
            if (!((currentOperation.getStatus().equals(OperationStatus.Succed)) || (currentOperation.getStatus().equals(OperationStatus.Checked)))) {
                currentOperation.redoExecute();
            }
            if (this.cannotContinueNextOperation(currentOperation)) {
                break;
            }
        }
        return this.computeAndUpdateStatus();
    }

    @Override
    protected void computeStatus() throws ServiceException {
        try {
            this.status = this.statusComputer.compute(this);
        } catch (Exception e) {
            throw new ServiceException("computeStatus() failed for " + this.id, e);
        }
    }

    private boolean cannotContinueNextOperation(Operation currentOperation) {
        return currentOperation.getStatus() == OperationStatus.Failed
               && currentOperation.isBlocking();
    }

    public List<Operation> getSubOperations() throws ServiceException {
        return AppServiceFacade.getOperationService().getSubOperations(this.id, this.userIdCaller, this.tldCaller);
    }

    public void addSubOperation(Operation operation) {
        Preconditions.checkNotNull(operation, "operation");
        if (operation == this) {
            throw new IllegalArgumentException("An operation cannot be added to its own subOperations");
        }
        this.operations.add(operation);
    }

    public void setSubOperation(List<Operation> operations) {
        Preconditions.checkNotNull(operations, "operations");
        this.operations.clear();
        this.operations.addAll(operations);
    }

    @Override
    public List<Document> getDocuments() throws ServiceException {
        List<Document> documents = new ArrayList<Document>();
        for (Operation subOperation : this.getSubOperations()) {
            if (subOperation != null) {
                List<Document> subDoc = subOperation.getDocuments();
                if (subDoc != null) {
                    documents.addAll(subDoc);
                }
            }
        }
        documents.addAll(AppServiceFacade.getOperationService().getDocuments(this.getId(), this.userIdCaller, this.tldCaller));
        return documents;
    }

    public OperationId createAndAddSimpleOperation(SimpleOperation operation) throws ServiceException {
        Operation createdOperation = AppServiceFacade.getOperationService().createAndGet(operation, this.userIdCaller, this.tldCaller);
        this.addSubOperation(createdOperation);
        return createdOperation.getId();

    }
}
