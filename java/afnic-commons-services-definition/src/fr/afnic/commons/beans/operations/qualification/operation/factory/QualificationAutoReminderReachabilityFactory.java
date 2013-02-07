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
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAutoReminderReachability;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationUpdateReminderAutoMail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationAutoReminderReachabilityFactory implements IQualificationOperationFactory {

    @Override
    public QualificationAutoReminderReachability create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                                 throws ServiceException {

        QualificationAutoReminderReachability operation = AppServiceFacade.getOperationService()
                                                                          .createAndGet(new QualificationAutoReminderReachability(qualification,
                                                                                                                                  comment.get(OperationType.QualificationAutoReminderReachability),
                                                                                                                                  userId, tld), userId, tld);

        OperationConfiguration subOperationConf = OperationConfiguration.create()
                                                                        .setParentId(operation.getId())
                                                                        .setBlocking(true)
                                                                        .setCreateUserId(userId);

        OperationId previousId = operation.getId();

        if (qualification.getHolderEmailAddress() != null) {
            previousId = operation.createAndAddSimpleOperation(new QualificationUpdateReminderAutoMail(subOperationConf.setPreviousOperationId(previousId), userId, tld));
            previousId = operation.createAndAddSimpleOperation(new NotifyEmailWithTemplate(subOperationConf.setPreviousOperationId(previousId),
                                                                                           OperationType.NotifyReminderAutoMailReachabilityToHolder, userId, tld));
            previousId = operation.createAndAddSimpleOperation(new QualificationAddReachability(subOperationConf.setPreviousOperationId(previousId), qualification.getReachStatus(),
                                                                                                ReachStatus.PendingEmailReminder, userId, tld));
        } else {
            subOperationConf.setComment("Pas de mail titulaire. Joignabilité automatique KO.");
            previousId = operation.createAndAddSimpleOperation(new QualificationAddReachability(subOperationConf.setPreviousOperationId(previousId), qualification.getReachStatus(),
                                                                                                ReachStatus.PendingPhone, userId, tld));
        }

        return operation;
    }
}
