package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.IQualificationOperation;
import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationStartJustification extends QualificationDomainPortfolioOperation {

    public QualificationStartJustification(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public QualificationStartJustification(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.QualificationStartJustification), userId, tld);
    }

    @Override
    public DomainPortfolioOperationType getDomainPortfolioOperationType() {
        return DomainPortfolioOperationType.Freeze;
    }

    public QualificationSource getSource() throws ServiceException {
        return this.getParentOrThrowException(IQualificationOperation.class).getQualification().getSource();
    }
}
