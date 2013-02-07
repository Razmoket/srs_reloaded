package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.NotifyEmailWithTemplate;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAddReachability;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAutoReachability;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationUpdateAutoMail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationAutoReachabilityFactory implements IQualificationOperationFactory {

    @Override
    public QualificationAutoReachability create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                         throws ServiceException {

        QualificationAutoReachability operation = AppServiceFacade.getOperationService().createAndGet(new QualificationAutoReachability(qualification,
                                                                                                                                        comment.get(OperationType.QualificationAutoReachability),
                                                                                                                                        userId, tld), userId, tld);

        OperationConfiguration subOperationConf = OperationConfiguration.create()
                                                                        .setParentId(operation.getId())
                                                                        .setBlocking(true)
                                                                        .setCreateUserId(userId);

        OperationId previousId = operation.getId();

        if (qualification.getHolderEmailAddress() != null) {
            previousId = operation.createAndAddSimpleOperation(new QualificationUpdateAutoMail(subOperationConf.setPreviousOperationId(previousId), userId, tld));
            previousId = operation.createAndAddSimpleOperation(new NotifyEmailWithTemplate(subOperationConf.setPreviousOperationId(previousId), OperationType.NotifyAutoMailReachabilityToHolder,
                                                                                           userId, tld));
            previousId = operation.createAndAddSimpleOperation(new QualificationAddReachability(subOperationConf.setPreviousOperationId(previousId), qualification.getReachStatus(),
                                                                                                ReachStatus.PendingEmail, userId, tld));
        } else {
            subOperationConf.setComment("Pas de mail titulaire. Joignabilité automatique KO.");
            previousId = operation.createAndAddSimpleOperation(new QualificationAddReachability(subOperationConf.setPreviousOperationId(previousId), qualification.getReachStatus(),
                                                                                                ReachStatus.PendingPhone, userId, tld));
        }

        return operation;
    }
}
