package fr.afnic.commons.beans.operations.qualification;

import java.util.Map;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.operation.factory.IQualificationOperationFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationAutoReachabilityFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationAutoReminderReachabilityFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationBlockDomainPortfolioFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationConfirmAutoReachabilityFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationFinishJustificationFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationFinishPendingFreezeFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationFinishValorizationFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationIncoherentDataFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationStartJustificationFactory;
import fr.afnic.commons.beans.operations.qualification.operation.factory.QualificationSuppressPortfolioFactory;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/***
 * Liste des opérations applicables directement à une qualification et qui n'ont pas besoin de parametre autre que la qualification.
 * 
 * @author ginguene
 *
 */
public enum QualificationOperationType {

    FinishValorization(new QualificationFinishValorizationFactory()),
    FinishPendingFreeze(new QualificationFinishPendingFreezeFactory()),
    IncoherentData(new QualificationIncoherentDataFactory()),
    StartJustification(new QualificationStartJustificationFactory()),
    FinishJustification(new QualificationFinishJustificationFactory()),
    BlockDomainPortfolio(new QualificationBlockDomainPortfolioFactory()),
    //UnblockDomainPortfolio(new QualificationUnblockDomainPortfolioFactory()),
    SuppressDomainPortfolio(new QualificationSuppressPortfolioFactory()),
    AutoReachability(new QualificationAutoReachabilityFactory()),
    AutoReminderReachability(new QualificationAutoReminderReachabilityFactory()),
    ConfirmAutoReachability(new QualificationConfirmAutoReachabilityFactory());

    private IQualificationOperationFactory factory;

    public Operation createOperation(Qualification qualification, Map<OperationType, String> comment, Map<OperationType, Email> mapEmail, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getFactory().create(qualification, comment, mapEmail, userId, tld);
    }

    private QualificationOperationType(IQualificationOperationFactory factory) {
        this.factory = factory;
    }

    protected IQualificationOperationFactory getFactory() {
        return this.factory;
    }

}
