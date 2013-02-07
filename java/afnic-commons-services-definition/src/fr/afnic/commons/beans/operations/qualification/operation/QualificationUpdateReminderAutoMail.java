package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationUpdateReminderAutoMail extends SimpleQualificationOperation {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(QualificationUpdateReminderAutoMail.class);

    public QualificationUpdateReminderAutoMail(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public QualificationUpdateReminderAutoMail(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.UpdateReminderAutoMail), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        LOGGER.debug("QualificationUpdateReminderAutoMail");
        Qualification qualification = this.getQualification();
        AppServiceFacade.getQualificationService().createQualificationCheckAutoReachability(qualification, true, this.userIdCaller, this.tldCaller);

        return OperationStatus.Succed;
    }

}
