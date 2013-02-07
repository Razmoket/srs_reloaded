package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.operations.qualification.operation.OperationFactoryFacade;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishValorization;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationFinishValorizationFactory implements IQualificationOperationFactory {

    public enum SubFinishValorizationOperation {
        FinishWithValidation, FinishWithoutValidation, GoIntoPendingFreeze;
    };

    @Override
    public QualificationFinishValorization create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                           throws ServiceException {

        QualificationFinishValorization operation = AppServiceFacade.getOperationService().createAndGet(new QualificationFinishValorization(qualification, userId, tld), userId, tld);

        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setParentId(operation.getId())
                                                            .setBlocking(true)
                                                            .setCreateUserId(userId);

        Operation subOperation = null;

        switch (getFinishValoCase(qualification)) {
        case FinishWithValidation:
            subOperation = OperationFactoryFacade.createQualificationFinishWithAfnicValidation(conf.setPreviousOperationId(operation.getId()), comment, mapEmail, userId, tld);
            break;
        case FinishWithoutValidation:
            subOperation = OperationFactoryFacade.createQualificationFinishWithoutValidation(conf.setPreviousOperationId(operation.getId()), comment, mapEmail, userId, tld);
            break;
        case GoIntoPendingFreeze:
            operation.createAndAddSimpleOperation(new PortfolioStatusUpdate(conf.setPreviousOperationId(operation.getId()), qualification.getPortfolioStatus(),
                                                                            PortfolioStatus.PendingFreeze, userId, tld));
        }

        if (subOperation != null) {
            operation.addSubOperation(subOperation);
        }

        return operation;
    }

    public static SubFinishValorizationOperation getFinishValoCase(Qualification qualification) {
        if ((qualification.getSource() != QualificationSource.Reporting) && (qualification.getSource() != QualificationSource.Control) && (qualification.getSource() != QualificationSource.Survey)) {
            if (qualification.isEligible() || qualification.isReachable()) {
                return SubFinishValorizationOperation.FinishWithValidation;
            } else {
                return SubFinishValorizationOperation.FinishWithoutValidation;
            }
        } else {
            if (qualification.isEligible() && qualification.isReachable()) {
                return SubFinishValorizationOperation.FinishWithValidation;
            } else {
                return SubFinishValorizationOperation.GoIntoPendingFreeze;
            }
        }
    }
}
