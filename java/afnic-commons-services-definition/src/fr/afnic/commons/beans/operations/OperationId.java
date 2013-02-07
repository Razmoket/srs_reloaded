package fr.afnic.commons.beans.operations;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.NumberId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class OperationId extends NumberId<Operation> {

    private static final long serialVersionUID = 1L;

    public OperationId(int id) {
        super(id);
    }

    @Override
    public Operation getObjectOwner(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getOperationService().getOperation(this, userId, tld);
    }

    @Override
    public boolean isExisting(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getOperationService().isExistingOperationId(this, userId, tld);
    }

}
