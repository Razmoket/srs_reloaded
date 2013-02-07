package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.NotifyEmailWithTemplate;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppOfDomainPortfolioOperation;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationUnblockDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.UnblockHolderDomainPortfolio;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationUnblockDomainPortfolioFactory implements IQualificationOperationFactory {

    @Override
    public QualificationUnblockDomainPortfolio create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                               throws ServiceException {

        QualificationUnblockDomainPortfolio operation = new QualificationUnblockDomainPortfolio(qualification, userId, tld);
        OperationId operationId = AppServiceFacade.getOperationService().create(operation, userId, tld);

        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setParentId(operationId)
                                                            .setBlocking(true)
                                                            .setCreateUserId(userId);

        OperationId previousId = operation.getId();
        previousId = operation.createAndAddSimpleOperation(new UnblockHolderDomainPortfolio(conf.setPreviousOperationId(previousId), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new NotifyEppOfDomainPortfolioOperation(conf.setPreviousOperationId(previousId), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new NotifyEmailWithTemplate(conf.setPreviousOperationId(previousId), OperationType.NotifyEmailUnblockedDomainPortfolioToRegistrar, userId,
                                                                                       tld));
        //if (qualification.getHolderEmailAddress() != null) {
        previousId = operation.createAndAddSimpleOperation(new NotifyEmailWithTemplate(conf.setPreviousOperationId(previousId), OperationType.NotifyEmailUnblockedDomainPortfolioToHolder, userId, tld));
        /*} else {
            previousId = operation.createAndAddSimpleOperation(new NotifyEmailWithTemplate(conf.setPreviousOperationId(previousId), OperationType.NotifyEmailUnblockedDomainPortfolioToHolderProblem));
        }*/
        previousId = operation.createAndAddSimpleOperation(new PortfolioStatusUpdate(conf.setPreviousOperationId(previousId), qualification.getPortfolioStatus(), PortfolioStatus.Frozen, userId, tld));

        return operation;
    }
}
