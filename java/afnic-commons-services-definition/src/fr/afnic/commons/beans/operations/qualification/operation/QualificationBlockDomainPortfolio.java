package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationBlockDomainPortfolio extends QualificationDomainPortfolioOperation {

    public QualificationBlockDomainPortfolio(UserId userId, TldServiceFacade tld) {
        this(null, userId, tld);
    }

    public QualificationBlockDomainPortfolio(Qualification qualification, UserId userId, TldServiceFacade tld) {
        super(qualification, OperationType.QualificationBlockDomainPortfolio, userId, tld);
    }

    @Override
    public DomainPortfolioOperationType getDomainPortfolioOperationType() {
        return DomainPortfolioOperationType.Block;
    }

}
