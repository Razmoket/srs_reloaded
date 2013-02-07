package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.SendEmail;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.operation.BlockHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppOfDomainPortfolioOperation;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationBlockDomainPortfolio;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationBlockDomainPortfolioFactory implements IQualificationOperationFactory {

    @Override
    public QualificationBlockDomainPortfolio create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                             throws ServiceException {

        QualificationBlockDomainPortfolio operation = AppServiceFacade.getOperationService().createAndGet(new QualificationBlockDomainPortfolio(qualification, userId, tld), userId, tld);

        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setParentId(operation.getId())
                                                            .setBlocking(true)
                                                            .setCreateUserId(userId);

        OperationId previousId = operation.getId();
        previousId = operation.createAndAddSimpleOperation(new BlockHolderDomainPortfolio(conf.setPreviousOperationId(previousId), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new PortfolioStatusUpdate(conf.setPreviousOperationId(previousId), qualification.getPortfolioStatus(), PortfolioStatus.Blocked, userId, tld));

        previousId = operation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailBlockedDomainPortfolioToRegistrar),
                                                                         mapEmail.get(OperationType.NotifyEmailBlockedDomainPortfolioToRegistrar), userId, tld));

        previousId = operation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailBlockedDomainPortfolioToHolder),
                                                                         mapEmail.get(OperationType.NotifyEmailBlockedDomainPortfolioToHolder), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new NotifyEppOfDomainPortfolioOperation(conf.setPreviousOperationId(previousId), userId, tld));

        return operation;
    }
}
