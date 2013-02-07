package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.AttachDocument;
import fr.afnic.commons.beans.operations.commons.NotifyEmailWithTemplate;
import fr.afnic.commons.beans.operations.commons.SendEmail;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.operation.BlockHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.FreezeHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppOfDomainPortfolioOperation;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppQualificationFinished;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppQualificationProblem;
import fr.afnic.commons.beans.operations.qualification.operation.NotifyEppQualificationStart;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAddEligibility;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAddReachability;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAutoReachability;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAutoReminderReachability;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationBlockDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationConfirmAutoMail;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationConfirmAutoReachability;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishJustification;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishPendingFreeze;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishValorization;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishWithAfnicValidation;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationFinishWithoutValidation;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationMarkIncoherent;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationStartJustification;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationSuppressDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationUnblockDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationUpdateAutoMail;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationUpdateReminderAutoMail;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationWhoisUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.SuppressHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.SuppressHolderPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.TopLevelOperationStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.UnblockHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.UnfreezeHolderDomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.UpdateContactWhoisQualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.TestOperation;

public class OperationFactory {

    public static Operation create(OperationType type, UserId userId, TldServiceFacade tld) throws ServiceException {
        switch (type) {
        case Qualification:
            return new Qualification(userId, tld);

        case Test:
            return new TestOperation(userId, tld);

        case CompositeTest:
            return new CompositeOperation(userId, tld);

        case QualificationFinishValorization:
            return new QualificationFinishValorization(userId, tld);

        case QualificationFinishPendingFreeze:
            return new QualificationFinishPendingFreeze(userId, tld);

        case UpdateContactWhoisQualification:
            return new UpdateContactWhoisQualification(userId, tld);

        case PortfolioStatusUpdate:
            return new PortfolioStatusUpdate(userId, tld);

        case TopLevelOperationStatusUpdate:
            return new TopLevelOperationStatusUpdate(userId, tld);

        case QualificationStartJustification:
            return new QualificationStartJustification(userId, tld);

        case NotifyEppOfDomainPortfolioOperation:
            return new NotifyEppOfDomainPortfolioOperation(userId, tld);

        case NotifyEmailFinishedToInitiator:
        case NotifyEmailFinishedToRegistrar:
        case NotifyEmailFinishedToHolder:
        case NotifyEmailBlockedDomainPortfolioToRegistrar:
        case NotifyEmailBlockedDomainPortfolioToHolder:
        case NotifyEmailUnblockedDomainPortfolioToRegistrar:
        case NotifyEmailUnblockedDomainPortfolioToHolder:
        case NotifyEmailSuppressedDomainPortfolioToRegistrar:
        case NotifyEmailSuppressedDomainPortfolioToHolder:
        case NotifyEmailProblemToInitiator:
        case NotifyEmailProblemToRegistrar:
        case NotifyEmailProblemToHolder:
        case NotifyEmailFinishedFromComplaintToInitiator:
        case NotifyEmailFromComplaintToInitiator:
        case NotifyAutoMailReachabilityToHolder:
        case NotifyReminderAutoMailReachabilityToHolder:
        case NotifyEmailFinishedLvl1ToInitiator:
        case NotifyEmailFinishedLvl1ToRegistrar:
        case NotifyEmailFinishedLvl1FailedToRegistrar:
            return new NotifyEmailWithTemplate(type, userId, tld);

        case SendEmail:
            return new SendEmail(userId, tld);

        case QualificationFinishWithAfnicValidation:
            return new QualificationFinishWithAfnicValidation(userId, tld);

        case QualificationFinishWithoutValidation:
            return new QualificationFinishWithoutValidation(userId, tld);

        case QualificationBlockDomainPortfolio:
            return new QualificationBlockDomainPortfolio(userId, tld);

        case QualificationSuppressDomainPortfolio:
            return new QualificationSuppressDomainPortfolio(userId, tld);

        case QualificationUnblockDomainPortfolio:
            return new QualificationUnblockDomainPortfolio(userId, tld);

        case QualificationWhoisUpdate:
            return new QualificationWhoisUpdate(userId, tld);

        case FreezeHolderDomainPortfolio:
            return new FreezeHolderDomainPortfolio(userId, tld);

        case UnfreezeHolderDomainPortfolio:
            return new UnfreezeHolderDomainPortfolio(userId, tld);

        case UnblockHolderDomainPortfolio:
            return new UnblockHolderDomainPortfolio(userId, tld);

        case SuppressHolderDomainPortfolio:
            return new SuppressHolderDomainPortfolio(userId, tld);

        case SuppressHolderPortfolio:
            return new SuppressHolderPortfolio(userId, tld);

        case NotifyEppQualificationFinished:
            return new NotifyEppQualificationFinished(userId, tld);

        case NotifyEppQualificationStart:
            return new NotifyEppQualificationStart(userId, tld);

        case NotifyEppQualificationProblem:
            return new NotifyEppQualificationProblem(userId, tld);

        case BlockHolderDomainPortfolio:
            return new BlockHolderDomainPortfolio(userId, tld);

        case QualificationSetEligibility:
            return new QualificationAddEligibility(userId, tld);

        case QualificationSetReachability:
            return new QualificationAddReachability(userId, tld);

        case QualificationFinishJustification:
            return new QualificationFinishJustification(userId, tld);

        case QualificationMarkIncoherent:
            return new QualificationMarkIncoherent(userId, tld);

        case QualificationAutoReachability:
            return new QualificationAutoReachability(userId, tld);

        case QualificationAutoReminderReachability:
            return new QualificationAutoReminderReachability(userId, tld);

        case UpdateAutoMail:
            return new QualificationUpdateAutoMail(userId, tld);

        case UpdateReminderAutoMail:
            return new QualificationUpdateReminderAutoMail(userId, tld);

        case QualificationConfirmAutoReachability:
            return new QualificationConfirmAutoReachability(userId, tld);

        case ConfirmAutoMail:
            return new QualificationConfirmAutoMail(userId, tld);

        case AttachDocument:
            return new AttachDocument(userId, tld);
        default:
            throw new NotImplementedException(" create(" + type + ") failed. No implementation defined.");
        }
    }
}
