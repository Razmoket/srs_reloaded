package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class PortfolioStatusUpdate extends StatusUpdate<PortfolioStatus> {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(PortfolioStatusUpdate.class);

    public PortfolioStatusUpdate(OperationConfiguration conf, PortfolioStatus oldValue, PortfolioStatus newValue, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.PortfolioStatusUpdate), oldValue, newValue, PortfolioStatus.class, userId, tld);

    }

    public PortfolioStatusUpdate(UserId userId, TldServiceFacade tld) {
        super(PortfolioStatus.class, userId, tld);
        this.setType(OperationType.PortfolioStatusUpdate);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {

        LOGGER.debug(this.getClass().getSimpleName() + " from  " + this.getOldValue() + " to " + this.getNewValue());
        Qualification qualification = this.getQualification();
        qualification.setPortfolioStatus(this.getNewValue());
        AppServiceFacade.getQualificationService().updatePortfolioStatus(qualification, this.userIdCaller, this.tldCaller);
        this.updateTopLevelOperation();
        return OperationStatus.Succed;
    }

    public Qualification getQualification() throws ServiceException {
        Qualification qualif = (Qualification) this.getTopLevelOperation();
        Preconditions.checkTrue(this.hasParent(), "parent");
        Preconditions.checkNotNull(qualif, "qualification");
        return qualif;
    }

}
