package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationUpdateAutoMail extends SimpleQualificationOperation {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(QualificationUpdateAutoMail.class);

    public QualificationUpdateAutoMail(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public QualificationUpdateAutoMail(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.UpdateAutoMail), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        LOGGER.debug("QualificationUpdateAutoMail");
        Qualification qualification = this.getQualification();
        AppServiceFacade.getQualificationService().createQualificationCheckAutoReachability(qualification, false, this.userIdCaller, this.tldCaller);

        return OperationStatus.Succed;
    }

}
