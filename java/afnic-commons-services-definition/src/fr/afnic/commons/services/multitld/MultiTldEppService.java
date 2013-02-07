package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.epp.EppMessage;
import fr.afnic.commons.beans.notifications.Notification;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationStep;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IEppService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldEppService implements IEppService {

    protected MultiTldEppService() {
        super();
    }

    @Override
    public List<Notification> notifyOfDomainPortfolioOperation(DomainPortfolioOperationType operation, String holderNicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getEppService().notifyOfDomainPortfolioOperation(operation, holderNicHandle, userId, tld);
    }

    @Override
    public List<Notification> notifyOfQualificationStep(QualificationStep step, Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getEppService().notifyOfQualificationStep(step, qualification, userId, tld);
    }

    @Override
    public List<EppMessage> getEppMessages(String contactSnapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getEppService().getEppMessages(contactSnapshotId, userId, tld);
    }
}
