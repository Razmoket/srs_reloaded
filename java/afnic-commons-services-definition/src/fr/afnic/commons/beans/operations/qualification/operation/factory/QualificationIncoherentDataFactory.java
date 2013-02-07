package fr.afnic.commons.beans.operations.qualification.operation.factory;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.IncoherentStatus;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishValorization;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationMarkIncoherent;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationIncoherentDataFactory implements IQualificationOperationFactory {

    @Override
    public QualificationFinishValorization create(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                           throws ServiceException {

        QualificationFinishValorization operation = AppServiceFacade.getOperationService().createAndGet(new QualificationFinishValorization(qualification, userId, tld), userId, tld);

        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setParentId(operation.getId())
                                                            .setBlocking(true)
                                                            .setCreateUserId(userId);

        OperationId previousId = null;

        previousId = operation.createAndAddSimpleOperation(new PortfolioStatusUpdate(conf.setPreviousOperationId(operation.getId()), qualification.getPortfolioStatus(),
                                                                                     PortfolioStatus.PendingFreeze, userId, tld));
        IncoherentStatus incoherent = IncoherentStatus.Coherent;
        if (qualification.isIncoherent()) {
            incoherent = IncoherentStatus.Incoherent;
        }
        operation.createAndAddSimpleOperation(new QualificationMarkIncoherent(conf.setPreviousOperationId(previousId), incoherent,
                                                                              IncoherentStatus.Incoherent, userId, tld));

        return operation;
    }
}
