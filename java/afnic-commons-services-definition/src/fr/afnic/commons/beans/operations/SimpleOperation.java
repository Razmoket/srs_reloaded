package fr.afnic.commons.beans.operations;

import java.util.List;

import fr.afnic.AfnicException;
import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.StackTraceUtils;

public abstract class SimpleOperation extends Operation {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SimpleOperation.class);

    protected SimpleOperation(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    protected SimpleOperation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf, userId, tld);
        this.setStatus(OperationStatus.Pending);
    }

    @Override
    protected final OperationStatus executeImpl() {

        try {
            Preconditions.checkNotNull(this.userIdCaller, "userId");
            Preconditions.checkTrue(this.userIdCaller.isExisting(this.userIdCaller, this.tldCaller), "userId.isMatching()");

            this.status = this.executeSimpleImpl();

            if (this.status == null) {
                this.status = OperationStatus.Failed;
                this.setDetails(this.getClass().getSimpleName() + ".executeSimple() returned null");
            }

        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName() + ".executeSimple() failed  with operationId=" + this.getId(), e);
            this.status = OperationStatus.Failed;

            String details = "";
            if (e instanceof AfnicException) {
                details = ((AfnicException) e).getFirstCauseMessage() + "\n\n";
            }

            details += StackTraceUtils.getStackTrace(e);
            if (details.length() > 4000) {
                this.setDetails(details.substring(0, 3999));
            } else {
                this.setDetails(details);
            }
            try {
                AppServiceFacade.getOperationService().updateDetails(this, this.userIdCaller, this.tldCaller);
            } catch (Exception e1) {
                LOGGER.error(this.getClass().getSimpleName() + ".executeSimple() failed  with update details " + this.getId(), e1);
            }
        }
        OperationStatus computeAndUpdateStatus = this.computeAndUpdateStatus();

        return computeAndUpdateStatus;
    }

    @Override
    protected final OperationStatus redoExecuteImpl() {
        return this.executeImpl();
    }

    /**
     * Calcul le statut de l'opération en changeant l'attribut statut de l'opération.<br/>
     * Retourne la nouvelle valeur du statut.
     */
    @Override
    protected void computeStatus() {
        if (this.status == OperationStatus.Failed && this.isNotBlocking()) {
            this.status = OperationStatus.Warn;
        }
        this.setStatus(this.status);
    }

    @Override
    public List<Document> getDocuments() throws ServiceException {
        return AppServiceFacade.getOperationService().getDocuments(this.id, this.userIdCaller, this.tldCaller);
    }

    protected abstract OperationStatus executeSimpleImpl() throws Exception;

}