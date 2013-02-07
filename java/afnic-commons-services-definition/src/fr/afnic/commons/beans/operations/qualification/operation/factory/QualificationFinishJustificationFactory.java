package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.SendEmail;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppQualificationFinished;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishJustification;
import fr.afnic.commons.beans.operations.qualification.operation.TopLevelOperationStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.UnblockHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.UnfreezeHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.UpdateContactWhoisQualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationFinishJustificationFactory implements IQualificationOperationFactory {

    @Override
    public QualificationFinishJustification create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                            throws ServiceException {

        QualificationFinishJustification operation = AppServiceFacade.getOperationService().createAndGet(new QualificationFinishJustification(qualification, userId, tld), userId, tld);

        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setParentId(operation.getId())
                                                            .setBlocking(true)
                                                            .setCreateUserId(userId);

        OperationId previousId = operation.getId();
        if ((qualification.getPortfolioStatus() == PortfolioStatus.Blocked) || (qualification.getPortfolioStatus() == PortfolioStatus.PendingSuppress)) {
            previousId = operation.createAndAddSimpleOperation(new UnblockHolderDomainPortfolio(conf.setPreviousOperationId(previousId), userId, tld));
        } else {
            previousId = operation.createAndAddSimpleOperation(new UnfreezeHolderDomainPortfolio(conf.setPreviousOperationId(previousId), userId, tld));
        }
        previousId = operation.createAndAddSimpleOperation(new PortfolioStatusUpdate(conf.setPreviousOperationId(previousId),
                                                                                     qualification.getPortfolioStatus(),
                                                                                     PortfolioStatus.Active, userId, tld));

        previousId = operation.createAndAddSimpleOperation(new UpdateContactWhoisQualification(conf.setPreviousOperationId(previousId), userId, tld));

        previousId = operation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailFinishedToRegistrar),
                                                                         mapEmail.get(OperationType.NotifyEmailFinishedToRegistrar), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailFinishedToHolder),
                                                                         mapEmail.get(OperationType.NotifyEmailFinishedToHolder), userId, tld));

        if (qualification.isExternalSource()) {
            previousId = operation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailFinishedToInitiator),
                                                                             mapEmail.get(OperationType.NotifyEmailFinishedToInitiator), userId, tld));
        }

        previousId = operation.createAndAddSimpleOperation(new NotifyEppQualificationFinished(conf, userId, tld));
        previousId = operation.createAndAddSimpleOperation(new TopLevelOperationStatusUpdate(conf.setPreviousOperationId(previousId),
                                                                                             qualification.getTopLevelStatus(),
                                                                                             TopLevelOperationStatus.Finished, userId, tld));

        return operation;
    }
}
