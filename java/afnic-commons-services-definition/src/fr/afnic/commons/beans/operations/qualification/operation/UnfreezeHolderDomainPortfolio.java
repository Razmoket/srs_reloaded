package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.contact.SimpleContactOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UnfreezeHolderDomainPortfolio extends SimpleContactOperation {

    public UnfreezeHolderDomainPortfolio(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create().setType(OperationType.UnfreezeHolderDomainPortfolio), userId, tld);
    }

    public UnfreezeHolderDomainPortfolio(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.UnfreezeHolderDomainPortfolio), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        AppServiceFacade.getDomainPortfolioService().execute(DomainPortfolioOperationType.Unfreeze,
                                                             this.getContactNicHandle(),
                                                             this.userIdCaller, this.tldCaller);
        return OperationStatus.Succed;
    }
}
