package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAddReachability;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationConfirmAutoMail;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationConfirmAutoReachability;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationConfirmAutoReachabilityFactory implements IQualificationOperationFactory {

    @Override
    public QualificationConfirmAutoReachability create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                                throws ServiceException {

        QualificationConfirmAutoReachability operation = AppServiceFacade.getOperationService().createAndGet(new QualificationConfirmAutoReachability(qualification, null, userId, tld), userId, tld);

        OperationConfiguration subOperationConf = OperationConfiguration.create()
                                                                        .setParentId(operation.getId())
                                                                        .setBlocking(true)
                                                                        .setCreateUserId(userId);

        OperationId previousId = operation.getId();

        previousId = operation.createAndAddSimpleOperation(new QualificationConfirmAutoMail(subOperationConf.setPreviousOperationId(previousId), userId, tld));
        operation.createAndAddSimpleOperation(new QualificationAddReachability(subOperationConf.setPreviousOperationId(previousId), qualification.getReachStatus(),
                                                                               ReachStatus.Email, userId, tld));

        return operation;
    }
}
