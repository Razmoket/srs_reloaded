package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationSuppressDomainPortfolio extends QualificationDomainPortfolioOperation {

    public QualificationSuppressDomainPortfolio(UserId userId, TldServiceFacade tld) {
        this(null, userId, tld);
    }

    public QualificationSuppressDomainPortfolio(Qualification qualification, UserId userId, TldServiceFacade tld) {
        super(qualification, OperationType.QualificationSuppressDomainPortfolio, userId, tld);
    }

    @Override
    public DomainPortfolioOperationType getDomainPortfolioOperationType() {
        return DomainPortfolioOperationType.Suppress;
    }

}
