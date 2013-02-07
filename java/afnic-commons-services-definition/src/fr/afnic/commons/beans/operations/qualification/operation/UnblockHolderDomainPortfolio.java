package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.contact.SimpleContactOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UnblockHolderDomainPortfolio extends SimpleContactOperation {

    public UnblockHolderDomainPortfolio(UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create().setType(OperationType.UnblockHolderDomainPortfolio), userId, tld);
    }

    public UnblockHolderDomainPortfolio(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.UnblockHolderDomainPortfolio), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        AppServiceFacade.getDomainPortfolioService().execute(DomainPortfolioOperationType.Unblock,
                                                             this.getContactNicHandle(),
                                                             this.userIdCaller, this.tldCaller);
        return OperationStatus.Succed;
    }
}
