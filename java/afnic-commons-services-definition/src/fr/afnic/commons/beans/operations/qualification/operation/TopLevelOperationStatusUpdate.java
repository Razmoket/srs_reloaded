package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class TopLevelOperationStatusUpdate extends StatusUpdate<TopLevelOperationStatus> {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(TopLevelOperationStatusUpdate.class);

    public TopLevelOperationStatusUpdate(UserId userId, TldServiceFacade tld) {
        super(TopLevelOperationStatus.class, userId, tld);
        this.setType(OperationType.TopLevelOperationStatusUpdate);
    }

    public TopLevelOperationStatusUpdate(OperationConfiguration conf, TopLevelOperationStatus oldValue, TopLevelOperationStatus newValue, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.TopLevelOperationStatusUpdate), oldValue, newValue, TopLevelOperationStatus.class, userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        LOGGER.debug(this.getClass().getSimpleName() + " from  " + this.getOldValue() + " to " + this.getNewValue());

        Qualification qualification = this.getQualification();
        this.getTopLevelOperation().setTopLevelStatus(this.getNewValue());
        AppServiceFacade.getQualificationService().updateTopLevelStatus(qualification, this.userIdCaller, this.tldCaller);

        return OperationStatus.Succed;
    }

    public Qualification getQualification() throws ServiceException {
        Qualification qualif = (Qualification) this.getTopLevelOperation();
        Preconditions.checkTrue(this.hasParent(), "parent");
        Preconditions.checkNotNull(qualif, "qualification");
        return qualif;
    }

}
