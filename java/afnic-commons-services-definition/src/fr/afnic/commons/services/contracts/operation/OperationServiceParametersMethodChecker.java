package fr.afnic.commons.services.contracts.operation;

import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class OperationServiceParametersMethodChecker {

    public void checkCreateParameters(Operation operation) throws ServiceException {

        if (operation == null) {
            throw new IllegalArgumentException("operation cannot be null");
        }

        if (operation.hasNoCreateUserId()) {
            throw new IllegalArgumentException("operation has no createUserId ");
        }

        if (operation.hasNoMacthingCreateUserId()) {
            throw new IllegalArgumentException("createUserId " + operation.getCreateUserId().getIntValue() + " is not matching user");
        }

        if (operation.getStatus() != null && operation.getStatus() != OperationStatus.Pending) {
            throw new IllegalArgumentException("Operation can only be created in  " + OperationStatus.Pending + " status [initialized with " + operation.getStatus() + "]");
        }

    }

    public void checkUpdateParameters(Operation operation, UserId updateUserId) throws ServiceException {
        if (operation == null) {
            throw new IllegalArgumentException("operation cannot be null");
        }

        if (updateUserId == null) {
            throw new IllegalArgumentException("updateUserId cannot be null");
        }

        if (updateUserId.isNotExisting(UserGenerator.getRootUserId(), TldServiceFacade.Fr)) {
            throw new IllegalArgumentException("updateUserId " + updateUserId.getIntValue() + " is not matching user");
        }

    }
}
