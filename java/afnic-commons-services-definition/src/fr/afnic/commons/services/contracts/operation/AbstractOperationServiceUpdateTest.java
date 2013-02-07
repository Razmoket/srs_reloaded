package fr.afnic.commons.services.contracts.operation;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.test.generator.OperationGenerator;

public abstract class AbstractOperationServiceUpdateTest<STATUS> {

    @Test
    public void updateOperationStatusWithUnknownOperationId() throws ServiceException {
        Operation createOperation = OperationGenerator.createOperation();
        createOperation.setId(new OperationId(OperationGenerator.UNKNOWN_OPERATION_ID));
        try {
            this.changeAndTestStatus(createOperation, this.getStatusList()[0]);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.id: OperationId[" + OperationGenerator.UNKNOWN_OPERATION_ID + "] does not exist.", e.getMessage());
        }
    }

    @Test
    public void updateOperationStatusWithNullOperationId() throws ServiceException {
        Operation createOperation = OperationGenerator.createOperation();
        createOperation.setId(null);
        try {
            this.changeAndTestStatus(createOperation, this.getStatusList()[0]);
            TestCase.fail("No exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("operation.id cannot be null.", e.getMessage());
        }
    }

    @Test
    public void updateOperationStatus() throws ServiceException {
        Operation operation = OperationGenerator.createOperation();
        for (STATUS status : this.getStatusList()) {
            operation = this.changeAndTestStatus(operation, status);
        }
    }

    public abstract STATUS[] getStatusList();

    public abstract Operation changeAndTestStatus(Operation createdOperation, STATUS status) throws ServiceException;
}
