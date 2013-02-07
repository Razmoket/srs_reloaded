package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.EligibilityStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class QualificationAddEligibility extends StatusUpdate<EligibilityStatus> {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(QualificationAddEligibility.class);

    public QualificationAddEligibility(UserId userId, TldServiceFacade tld) {
        super(EligibilityStatus.class, userId, tld);
        this.setType(OperationType.QualificationSetEligibility);
    }

    public QualificationAddEligibility(OperationConfiguration conf, EligibilityStatus oldValue, EligibilityStatus newValue, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.QualificationSetEligibility), oldValue, newValue, EligibilityStatus.class, userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        LOGGER.debug(this.getClass().getSimpleName() + " from  " + this.getOldValue() + " to " + this.getNewValue());
        Qualification qualification = this.getQualification();
        qualification.setEligibilityStatus(this.getNewValue());
        AppServiceFacade.getQualificationService().updateEligibilityStatus(qualification, this.userIdCaller, this.tldCaller);

        return OperationStatus.Succed;
    }

    public Qualification getQualification() throws ServiceException {
        Qualification qualif = (Qualification) this.getTopLevelOperation();
        Preconditions.checkTrue(this.hasParent(), "parent");
        Preconditions.checkNotNull(qualif, "qualification");
        return qualif;
    }

}
