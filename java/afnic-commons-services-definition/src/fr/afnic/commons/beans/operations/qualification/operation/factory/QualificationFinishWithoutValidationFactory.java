package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.SendEmail;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppQualificationFinished;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishWithoutValidation;
import fr.afnic.commons.beans.operations.qualification.operation.TopLevelOperationStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.UpdateContactWhoisQualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationFinishWithoutValidationFactory {

    public CompositeOperation create(OperationConfiguration conf, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld) throws ServiceException {

        QualificationFinishWithoutValidation operation = AppServiceFacade.getOperationService().createAndGet(new QualificationFinishWithoutValidation(conf, userId, tld), userId, tld);
        this.createSubOperations(operation, conf, (Qualification) operation.getTopLevelOperation(), false, comment, mapEmail, userId, tld);

        return operation;
    }

    public void createSubOperations(CompositeOperation topOperation, OperationConfiguration conf, Qualification qualification, boolean withValidation, Map<OperationType, String> comment,
                                    Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld) throws ServiceException {

        OperationConfiguration subOperationConf = conf.setParentId(topOperation.getId())
                                                      .setBlocking(true);

        OperationId previousId = topOperation.getId();

        previousId = topOperation.createAndAddSimpleOperation(new PortfolioStatusUpdate(subOperationConf.setPreviousOperationId(previousId),
                                                                                        qualification.getPortfolioStatus(),
                                                                                        PortfolioStatus.Active, userId, tld));

        previousId = topOperation.createAndAddSimpleOperation(new UpdateContactWhoisQualification(subOperationConf.setPreviousOperationId(previousId), userId, tld));

        if (qualification.getSource() == QualificationSource.Plaint) {
            previousId = topOperation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailFinishedFromComplaintToInitiator),
                                                                                mapEmail.get(OperationType.NotifyEmailFinishedFromComplaintToInitiator), userId, tld));
        } else {
            if (withValidation) {
                previousId = topOperation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailFinishedLvl1ToRegistrar),
                                                                                    mapEmail.get(OperationType.NotifyEmailFinishedLvl1ToRegistrar), userId, tld));
            } else {
                previousId = topOperation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailFinishedLvl1FailedToRegistrar),
                                                                                    mapEmail.get(OperationType.NotifyEmailFinishedLvl1FailedToRegistrar), userId, tld));
            }

            if (qualification.getSource() == QualificationSource.Reporting) {
                previousId = topOperation.createAndAddSimpleOperation(new SendEmail(conf.setPreviousOperationId(previousId), comment.get(OperationType.NotifyEmailFinishedLvl1ToInitiator),
                                                                                    mapEmail.get(OperationType.NotifyEmailFinishedLvl1ToInitiator), userId, tld));
            }

            previousId = topOperation.createAndAddSimpleOperation(new NotifyEppQualificationFinished(subOperationConf.setPreviousOperationId(previousId), userId, tld));
        }

        previousId = topOperation.createAndAddSimpleOperation(new TopLevelOperationStatusUpdate(subOperationConf.setPreviousOperationId(previousId),
                                                                                                qualification.getTopLevelStatus(),
                                                                                                TopLevelOperationStatus.Finished, userId, tld));
    }
}
