package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationConfirmAutoMail extends SimpleQualificationOperation {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(QualificationConfirmAutoMail.class);

    public QualificationConfirmAutoMail(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public QualificationConfirmAutoMail(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.ConfirmAutoMail), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        LOGGER.debug("QualificationConfirmAutoMail");
        Qualification qualification = this.getQualification();
        AppServiceFacade.getQualificationService().updateAutoMail(qualification.getQualificationId(), this.userIdCaller, this.tldCaller);

        return OperationStatus.Succed;
    }

}
