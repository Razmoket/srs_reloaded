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
import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.operations.qualification.operation.FreezeHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppOfDomainPortfolioOperation;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppQualificationProblem;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppQualificationStart;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationStartJustification;
import fr.afnic.commons.beans.operations.qualification.operation.TopLevelOperationStatusUpdate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationStartJustificationFactory implements IQualificationOperationFactory {

    @Override
    public QualificationStartJustification create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                           throws ServiceException {

        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setBlocking(true)
                                                            .setCreateUserId(userId)
                                                            .setParentId(qualification.getId());

        QualificationStartJustification operation = AppServiceFacade.getOperationService().createAndGet(new QualificationStartJustification(conf, userId, tld), userId, tld);

        OperationConfiguration subOperationConf = conf.setParentId(operation.getId())
                                                      .setBlocking(true);

        OperationId previousId = operation.getId();

        if (operation.getSource() == QualificationSource.Plaint) {
            previousId = operation.createAndAddSimpleOperation(new NotifyEppQualificationStart(subOperationConf.setPreviousOperationId(previousId), userId, tld));
        }
        previousId = operation.createAndAddSimpleOperation(new FreezeHolderDomainPortfolio(subOperationConf.setPreviousOperationId(previousId), userId, tld));

        previousId = operation.createAndAddSimpleOperation(new PortfolioStatusUpdate(subOperationConf.setPreviousOperationId(previousId), qualification.getPortfolioStatus(), PortfolioStatus.Frozen,
                                                                                     userId, tld));
        previousId = operation.createAndAddSimpleOperation(new TopLevelOperationStatusUpdate(subOperationConf.setPreviousOperationId(previousId),
                                                                                             qualification.getTopLevelStatus(),
                                                                                             TopLevelOperationStatus.PendingResponse, userId, tld));

        previousId = operation.createAndAddSimpleOperation(new NotifyEppOfDomainPortfolioOperation(subOperationConf.setPreviousOperationId(previousId), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new NotifyEppQualificationProblem(subOperationConf.setPreviousOperationId(previousId), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new SendEmail(subOperationConf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailProblemToRegistrar),
                                                                         mapEmail.get(OperationType.NotifyEmailProblemToRegistrar), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new SendEmail(subOperationConf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailProblemBisToRegistrar),
                                                                         mapEmail.get(OperationType.NotifyEmailProblemBisToRegistrar), userId, tld));
        previousId = operation.createAndAddSimpleOperation(new SendEmail(subOperationConf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailProblemToHolder),
                                                                         mapEmail.get(OperationType.NotifyEmailProblemToHolder), userId, tld));

        if (qualification.isExternalSource()) {
            previousId = operation.createAndAddSimpleOperation(new SendEmail(subOperationConf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailProblemToInitiator),
                                                                             mapEmail.get(OperationType.NotifyEmailProblemToInitiator), userId, tld));
        }

        return operation;
    }

    public QualificationStartJustification create(OperationConfiguration conf, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                           throws ServiceException {

        Qualification qualification = AppServiceFacade.getQualificationService().getQualification(conf.getParentId(), userId, tld);
        return this.create(qualification, comment, mapEmail, userId, tld);
    }
}
