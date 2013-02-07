package fr.afnic.commons.beans.operations;

import java.io.Serializable;
import java.util.HashSet;

import com.google.common.base.Preconditions;

import fr.afnic.commons.services.exception.ServiceException;

public class CompositeOperationComputer implements Serializable {

    private CompositeOperation operation;

    private final HashSet<OperationStatus> statusSet = new HashSet<OperationStatus>();
    private final OperationStatus[] priorityStatus = { OperationStatus.Failed,
                                                      OperationStatus.Pending,
                                                      OperationStatus.Warn,
                                                      OperationStatus.Checked,
                                                      OperationStatus.Succed };

    public CompositeOperationComputer() {

    }

    public OperationStatus compute(CompositeOperation operation) throws ServiceException {
        this.operation = Preconditions.checkNotNull(operation, "operation");
        this.computeStatusSet();

        OperationStatus status = null;
        for (OperationStatus currentStatus : this.priorityStatus) {
            if (this.statusSet.contains(currentStatus)) {
                status = currentStatus;
                break;
            }
        }

        if (status == null) {
            return OperationStatus.Succed;
        }

        if (this.operation.isNotBlocking() && status == OperationStatus.Failed) {
            return OperationStatus.Warn;
        }

        return status;
    }

    private void computeStatusSet() throws ServiceException {
        this.statusSet.clear();
        for (Operation currentOperation : this.operation.getSubOperations()) {
            OperationStatus status = currentOperation.getStatus();
            this.statusSet.add(status);
        }
    }
}
