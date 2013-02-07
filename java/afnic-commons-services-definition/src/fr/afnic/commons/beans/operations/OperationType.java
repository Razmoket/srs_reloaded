package fr.afnic.commons.beans.operations;

public enum OperationType {

    //Opérations composées
    Qualification,
    QualificationFinishValorization,
    QualificationFinishPendingFreeze,
    QualificationFinishJustification,
    QualificationBlockDomainPortfolio,
    QualificationSuppressDomainPortfolio,
    QualificationUnblockDomainPortfolio,
    QualificationWhoisUpdate,
    QualificationStartJustification,
    QualificationFinishWithAfnicValidation,
    QualificationFinishWithoutValidation,

    FreezeHolderDomainPortfolio,
    UnfreezeHolderDomainPortfolio,
    UnblockHolderDomainPortfolio,
    BlockHolderDomainPortfolio,
    SuppressHolderDomainPortfolio,
    SuppressHolderPortfolio,

    //Opérations de bases
    NotifyEmailStartValorizationToInitiator,
    NotifyEmailStartValorizationToRegistrar,

    NotifyEmailFinishedToInitiator,
    NotifyEmailFinishedToRegistrar,
    NotifyEmailFinishedToHolder,

    NotifyEmailFinishedFromComplaintToInitiator,
    NotifyEmailFromComplaintToInitiator,

    NotifyEmailProblemToInitiator,
    NotifyEmailProblemBisToRegistrar,
    NotifyEmailProblemToRegistrar,
    NotifyEmailProblemToHolder,

    NotifyEmailBlockedDomainPortfolioToRegistrar,
    NotifyEmailBlockedDomainPortfolioToHolder,

    NotifyEmailUnblockedDomainPortfolioToRegistrar,
    NotifyEmailUnblockedDomainPortfolioToHolder,

    NotifyEmailSuppressedDomainPortfolioToRegistrar,
    NotifyEmailSuppressedDomainPortfolioToHolder,

    NotifyEmailFinishedLvl1ToInitiator,
    NotifyEmailFinishedLvl1ToRegistrar,
    NotifyEmailFinishedLvl1FailedToRegistrar,

    NotifyEppOfDomainPortfolioOperation,

    NotifyEppQualificationStart,
    NotifyEppQualificationProblem,
    NotifyEppQualificationFinished,

    UpdateContactWhoisQualification,
    TopLevelOperationStatusUpdate,

    QualificationSetReachability,
    QualificationSetEligibility,
    QualificationMarkIncoherent,

    QualificationAutoReachability,
    QualificationAutoReminderReachability,
    NotifyAutoMailReachabilityToHolder,
    NotifyReminderAutoMailReachabilityToHolder,
    UpdateAutoMail,
    UpdateReminderAutoMail,
    QualificationConfirmAutoReachability,
    ConfirmAutoMail,

    PortfolioStatusUpdate,
    AttachDocument,

    Test,
    CompositeTest,

    SendEmail;

    public boolean isStatusUpdate() {
        return this == TopLevelOperationStatusUpdate
               || this == PortfolioStatusUpdate
               || this == QualificationSetReachability
               || this == QualificationSetEligibility
               || this == QualificationMarkIncoherent;
    }

    public boolean isSendEmail() {
        return this == SendEmail;
    }

    public boolean isQualification() {
        return this == Qualification;
    }
}
