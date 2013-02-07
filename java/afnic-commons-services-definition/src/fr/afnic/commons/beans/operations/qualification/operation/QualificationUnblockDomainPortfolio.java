package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationUnblockDomainPortfolio extends QualificationDomainPortfolioOperation {

    public QualificationUnblockDomainPortfolio(UserId userId, TldServiceFacade tld) {
        this(null, userId, tld);
    }

    public QualificationUnblockDomainPortfolio(Qualification qualification, UserId userId, TldServiceFacade tld) {
        super(qualification, OperationType.QualificationUnblockDomainPortfolio, userId, tld);
    }

    @Override
    public DomainPortfolioOperationType getDomainPortfolioOperationType() {
        return DomainPortfolioOperationType.Unblock;
    }

}
