package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.contact.SimpleContactOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class BlockHolderDomainPortfolio extends SimpleContactOperation {

    public BlockHolderDomainPortfolio(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public BlockHolderDomainPortfolio(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.BlockHolderDomainPortfolio), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        AppServiceFacade.getDomainPortfolioService().execute(DomainPortfolioOperationType.Block,
                                                             this.getContactNicHandle(), this.userIdCaller, this.tldCaller);
        return OperationStatus.Succed;
    }
}
