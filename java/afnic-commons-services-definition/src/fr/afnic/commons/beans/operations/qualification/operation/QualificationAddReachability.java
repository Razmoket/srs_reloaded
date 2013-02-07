package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.NotifyEmailWithTemplate;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class QualificationAddReachability extends StatusUpdate<ReachStatus> {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(QualificationAddReachability.class);

    public QualificationAddReachability(UserId userId, TldServiceFacade tld) {
        super(ReachStatus.class, userId, tld);
        this.setType(OperationType.QualificationSetReachability);
    }

    public QualificationAddReachability(OperationConfiguration conf, ReachStatus oldValue, ReachStatus newValue, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.QualificationSetReachability), oldValue, newValue, ReachStatus.class, userId, tld);
    }

    private boolean isOkForNotifyEmailWithTemplate() throws Exception {
        CompositeOperation parentOperation = (CompositeOperation) this.getParent();
        for (Operation op : parentOperation.getSubOperations()) {
            if (op instanceof NotifyEmailWithTemplate) {
                if ((op.getStatus() == OperationStatus.Warn) && (op.getType() == OperationType.NotifyAutoMailReachabilityToHolder)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        if (!this.isOkForNotifyEmailWithTemplate()) {
            // si le destinataire du mail est un mail invalide => joignabilitÃ© KO
            this.setNewValue(ReachStatus.NotReachable);
        }

        LOGGER.debug(this.getClass().getSimpleName() + " from  " + this.getOldValue() + " to " + this.getNewValue());
        Qualification qualification = this.getQualification();
        qualification.setReachStatus(this.getNewValue());
        AppServiceFacade.getQualificationService().updateReachStatus(qualification, this.userIdCaller, this.tldCaller);

        return OperationStatus.Succed;
    }

    public Qualification getQualification() throws ServiceException {
        Qualification qualif = (Qualification) this.getTopLevelOperation();
        Preconditions.checkTrue(this.hasParent(), "parent");
        Preconditions.checkNotNull(qualif, "qualification");
        return qualif;
    }

}
