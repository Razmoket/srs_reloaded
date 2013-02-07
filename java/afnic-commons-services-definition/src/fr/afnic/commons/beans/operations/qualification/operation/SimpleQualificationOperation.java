package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.SimpleOperation;
import fr.afnic.commons.beans.operations.qualification.IQualificationOperation;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public abstract class SimpleQualificationOperation extends SimpleOperation {

    protected SimpleQualificationOperation(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    protected SimpleQualificationOperation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf, userId, tld);
    }

    public Qualification getQualification() throws ServiceException {
        OperationId id = this.getParentOrThrowException(IQualificationOperation.class).getQualification().getId();
        return AppServiceFacade.getQualificationService().getQualification(id, this.userIdCaller, this.tldCaller);
    }
}
