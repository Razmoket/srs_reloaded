package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.SimpleOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public abstract class StatusUpdate<STATUS extends Enum<?>> extends SimpleOperation {

    private STATUS oldValue;
    private STATUS newValue;

    private final Class<STATUS> statusClass;

    protected StatusUpdate(Class<STATUS> statusClass, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        this.statusClass = statusClass;
    }

    protected StatusUpdate(OperationConfiguration conf, STATUS oldValue, STATUS newValue, Class<STATUS> statusClass, UserId userId, TldServiceFacade tld) {
        super(conf, userId, tld);
        this.newValue = Preconditions.checkNotNull(newValue, "newValue");
        this.oldValue = oldValue;
        this.statusClass = statusClass;

    }

    public STATUS getOldValue() {
        return this.oldValue;
    }

    public STATUS getNewValue() {
        return this.newValue;
    }

    public void setOldValue(STATUS oldValue) {
        this.oldValue = oldValue;
    }

    public void setNewValue(STATUS newValue) {
        this.newValue = Preconditions.checkNotNull(newValue, "newValue");
    }

    public Class<STATUS> getStatusClass() {
        return this.statusClass;
    }

    @Override
    public void populate() throws ServiceException {
        AppServiceFacade.getOperationService().populateStatusUpdate(this, this.userIdCaller, this.tldCaller);
    }

}
