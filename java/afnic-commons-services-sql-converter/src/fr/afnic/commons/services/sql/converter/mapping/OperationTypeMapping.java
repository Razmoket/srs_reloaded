package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class OperationTypeMapping extends ConstantTypeMapping<Integer, OperationType> {

    public OperationTypeMapping() {
        super(Integer.class, OperationType.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, OperationType.Qualification);
        this.addMapping(2, OperationType.QualificationFinishValorization);
        this.addMapping(3, OperationType.QualificationWhoisUpdate);
        this.addMapping(4, OperationType.QualificationStartJustification);
        this.addMapping(5, OperationType.QualificationFinishWithAfnicValidation);
        this.addMapping(6, OperationType.QualificationFinishWithoutValidation);
        this.addMapping(7, OperationType.NotifyEmailFinishedToInitiator);
        this.addMapping(8, OperationType.NotifyEmailFinishedToRegistrar);
        this.addMapping(9, OperationType.NotifyEmailFinishedToHolder);
        this.addMapping(10, OperationType.NotifyEmailProblemToInitiator);
        this.addMapping(11, OperationType.NotifyEmailProblemToRegistrar);
        this.addMapping(12, OperationType.NotifyEmailProblemToHolder);
        this.addMapping(13, OperationType.NotifyEppQualificationStart);
        this.addMapping(14, OperationType.NotifyEppQualificationFinished);
        this.addMapping(16, OperationType.UpdateContactWhoisQualification);
        this.addMapping(17, OperationType.TopLevelOperationStatusUpdate);
        this.addMapping(18, OperationType.FreezeHolderDomainPortfolio);
        this.addMapping(19, OperationType.PortfolioStatusUpdate);
        this.addMapping(20, OperationType.AttachDocument);
        this.addMapping(21, OperationType.Test);
        this.addMapping(22, OperationType.CompositeTest);
        this.addMapping(23, OperationType.NotifyEppOfDomainPortfolioOperation);
        this.addMapping(24, OperationType.QualificationBlockDomainPortfolio);
        this.addMapping(25, OperationType.NotifyEmailBlockedDomainPortfolioToHolder);
        this.addMapping(26, OperationType.NotifyEmailBlockedDomainPortfolioToRegistrar);
        this.addMapping(27, OperationType.BlockHolderDomainPortfolio);
        this.addMapping(28, OperationType.SuppressHolderDomainPortfolio);
        this.addMapping(29, OperationType.QualificationSuppressDomainPortfolio);
        this.addMapping(30, OperationType.NotifyEmailSuppressedDomainPortfolioToHolder);
        this.addMapping(31, OperationType.NotifyEmailSuppressedDomainPortfolioToRegistrar);
        this.addMapping(32, OperationType.NotifyEppQualificationProblem);
        this.addMapping(33, OperationType.SendEmail);
        this.addMapping(34, OperationType.QualificationSetEligibility);
        this.addMapping(35, OperationType.QualificationSetReachability);
        this.addMapping(36, OperationType.QualificationFinishJustification);
        this.addMapping(37, OperationType.UnfreezeHolderDomainPortfolio);
        this.addMapping(38, OperationType.UnblockHolderDomainPortfolio);
        this.addMapping(39, OperationType.QualificationMarkIncoherent);
        this.addMapping(40, OperationType.QualificationAutoReachability);
        this.addMapping(41, OperationType.QualificationAutoReminderReachability);
        this.addMapping(42, OperationType.NotifyAutoMailReachabilityToHolder);
        this.addMapping(43, OperationType.NotifyReminderAutoMailReachabilityToHolder);
        this.addMapping(44, OperationType.UpdateAutoMail);
        this.addMapping(45, OperationType.NotifyEmailFinishedFromComplaintToInitiator);
        this.addMapping(46, OperationType.NotifyEmailFromComplaintToInitiator);
        this.addMapping(47, OperationType.QualificationConfirmAutoReachability);
        this.addMapping(48, OperationType.ConfirmAutoMail);
        this.addMapping(49, OperationType.UpdateReminderAutoMail);
        this.addMapping(50, OperationType.NotifyEmailFinishedLvl1ToInitiator);
        this.addMapping(51, OperationType.NotifyEmailFinishedLvl1ToRegistrar);
        this.addMapping(52, OperationType.NotifyEmailFinishedLvl1FailedToRegistrar);
        this.addMapping(53, OperationType.QualificationFinishPendingFreeze);
        this.addMapping(54, OperationType.SuppressHolderPortfolio);
    }
}
