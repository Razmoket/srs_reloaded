package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.QualificationStep;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class NotifyEppQualificationProblem extends SimpleQualificationOperation {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(NotifyEppQualificationProblem.class);

    public NotifyEppQualificationProblem(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public NotifyEppQualificationProblem(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.NotifyEppQualificationProblem), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        LOGGER.debug("NotifyEppQualificationProblem");

        AppServiceFacade.getEppService().notifyOfQualificationStep(QualificationStep.Problem, this.getQualification(), this.userIdCaller, this.tldCaller);

        return OperationStatus.Succed;
    }

}
