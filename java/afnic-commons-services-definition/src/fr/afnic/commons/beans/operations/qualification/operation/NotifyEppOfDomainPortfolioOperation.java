package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.SimpleOperation;
import fr.afnic.commons.beans.operations.contact.IContactOperation;
import fr.afnic.commons.beans.operations.domainportfolio.IDomainPortfolioOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class NotifyEppOfDomainPortfolioOperation extends SimpleOperation {

    public NotifyEppOfDomainPortfolioOperation(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public NotifyEppOfDomainPortfolioOperation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.NotifyEppOfDomainPortfolioOperation), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        AppServiceFacade.getEppService().notifyOfDomainPortfolioOperation(this.getParentOrThrowException(IDomainPortfolioOperation.class).getDomainPortfolioOperationType(),
                                                                          this.getParentOrThrowException(IContactOperation.class).getNicHandle(), this.userIdCaller, this.tldCaller);
        return OperationStatus.Succed;
    }

    @Override
    protected Class<?>[] getRequiredParentClass() {
        return new Class<?>[] { IContactOperation.class, IDomainPortfolioOperation.class };
    }
}
