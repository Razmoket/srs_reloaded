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
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppOfDomainPortfolioOperation;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationSuppressDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.SuppressHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.SuppressHolderPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.TopLevelOperationStatusUpdate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationSuppressPortfolioFactory implements IQualificationOperationFactory {

    @Override
    public QualificationSuppressDomainPortfolio create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                                throws ServiceException {

        QualificationSuppressDomainPortfolio operation = AppServiceFacade.getOperationService().createAndGet(new QualificationSuppressDomainPortfolio(qualification, userId, tld), userId, tld);

        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setParentId(operation.getId())
                                                            .setBlocking(true)
                                                            .setCreateUserId(userId);

        OperationId previousId = operation.getId();
        previousId = operation.createAndAddSimpleOperation(new SuppressHolderDomainPortfolio(conf.setPreviousOperationId(previousId), userId, tld));

        previousId = operation.createAndAddSimpleOperation(new PortfolioStatusUpdate(conf.setPreviousOperationId(previousId), qualification.getPortfolioStatus(), PortfolioStatus.Suppressed, userId,
                                                                                     tld));
        previousId = operation.createAndAddSimpleOperation(new TopLevelOperationStatusUpdate(conf.setPreviousOperationId(previousId), qualification.getTopLevelStatus(),
                                                                                             TopLevelOperationStatus.Finished, userId, tld));

        previousId = operation.createAndAddSimpleOperation(new NotifyEppOfDomainPortfolioOperation(conf.setPreviousOperationId(previousId), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailSuppressedDomainPortfolioToRegistrar),
                                                                         mapEmail.get(OperationType.NotifyEmailSuppressedDomainPortfolioToRegistrar), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailSuppressedDomainPortfolioToHolder),
                                                                         mapEmail.get(OperationType.NotifyEmailSuppressedDomainPortfolioToHolder), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new SuppressHolderPortfolio(conf.setPreviousOperationId(previousId), userId, tld));

        return operation;
    }
}
