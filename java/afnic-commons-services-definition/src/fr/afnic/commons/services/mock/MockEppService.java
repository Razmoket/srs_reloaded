/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.mock;

import java.util.Collections;
import java.util.List;

import fr.afnic.commons.beans.epp.EppMessage;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.notifications.Notification;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationStep;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IEppService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockEppService implements IEppService {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(MockEppService.class);

    @Override
    public List<Notification> notifyOfDomainPortfolioOperation(DomainPortfolioOperationType operation, String holderNicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        LOGGER.debug("notifyOfDomainPortfolioOperation(" + holderNicHandle + "," + operation + ")");
        return Collections.emptyList();
    }

    @Override
    public List<Notification> notifyOfQualificationStep(QualificationStep step, Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        return Collections.emptyList();
    }

    @Override
    public List<EppMessage> getEppMessages(String contactSnapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return Collections.emptyList();
    }

}
