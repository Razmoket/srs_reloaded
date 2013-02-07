package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishWithAfnicValidation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationFinishWithAfnicValidationFactory extends QualificationFinishWithoutValidationFactory {

    @Override
    public CompositeOperation create(OperationConfiguration conf, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld) throws ServiceException {

        QualificationFinishWithAfnicValidation operation = AppServiceFacade.getOperationService().createAndGet(new QualificationFinishWithAfnicValidation(conf, userId, tld), userId, tld);
        this.createSubOperations(operation, conf, (Qualification) operation.getTopLevelOperation(), true, comment, mapEmail, userId, tld);

        return operation;

    }
}
