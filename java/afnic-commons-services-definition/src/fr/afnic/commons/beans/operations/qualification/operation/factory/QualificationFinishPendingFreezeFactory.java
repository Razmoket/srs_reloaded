package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.operation.OperationFactoryFacade;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishPendingFreeze;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationFinishPendingFreezeFactory implements IQualificationOperationFactory {

    @Override
    public QualificationFinishPendingFreeze create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                            throws ServiceException {

        QualificationFinishPendingFreeze operation = AppServiceFacade.getOperationService().createAndGet(new QualificationFinishPendingFreeze(qualification, userId, tld), userId, tld);

        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setParentId(operation.getId())
                                                            .setBlocking(true)
                                                            .setCreateUserId(userId);

        Operation subOperation = null;

        switch (QualificationFinishValorizationFactory.getFinishValoCase(qualification)) {
        case FinishWithValidation:
            subOperation = OperationFactoryFacade.createQualificationFinishWithAfnicValidation(conf.setPreviousOperationId(operation.getId()), comment, mapEmail, userId, tld);
            break;
        case FinishWithoutValidation:
            subOperation = OperationFactoryFacade.createQualificationFinishWithoutValidation(conf.setPreviousOperationId(operation.getId()), comment, mapEmail, userId, tld);
            break;
        case GoIntoPendingFreeze:
            subOperation = OperationFactoryFacade.createQualificationFinishWithoutValidation(conf.setPreviousOperationId(operation.getId()), comment, mapEmail, userId, tld);
            break;
        }

        if (subOperation != null) {
            operation.addSubOperation(subOperation);
        }

        return operation;
    }
}
