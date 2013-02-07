package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.IncoherentStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class QualificationMarkIncoherent extends StatusUpdate<IncoherentStatus> {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(QualificationMarkIncoherent.class);

    public QualificationMarkIncoherent(UserId userId, TldServiceFacade tld) {
        super(IncoherentStatus.class, userId, tld);
        this.setType(OperationType.QualificationMarkIncoherent);
    }

    public QualificationMarkIncoherent(OperationConfiguration conf, IncoherentStatus oldValue, IncoherentStatus newValue, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.QualificationMarkIncoherent), oldValue, newValue, IncoherentStatus.class, userId, tld);
    }

    /**
     * peut uniquement passer le status de cohérent à "incohérent". Ce statut ne peut être retiré
     */
    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        LOGGER.debug(this.getClass().getSimpleName() + " from  " + this.getOldValue() + " to " + this.getNewValue());
        Qualification qualification = this.getQualification();
        qualification.setIncoherent(this.getNewValue() == IncoherentStatus.Incoherent ? true : false);
        AppServiceFacade.getQualificationService().setIncoherent(qualification, this.userIdCaller, this.tldCaller);

        return OperationStatus.Succed;
    }

    public Qualification getQualification() throws ServiceException {
        Qualification qualif = (Qualification) this.getTopLevelOperation();
        Preconditions.checkTrue(this.hasParent(), "parent");
        Preconditions.checkNotNull(qualif, "qualification");
        return qualif;
    }

}
